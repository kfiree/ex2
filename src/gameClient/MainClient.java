package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class MainClient  implements Runnable{
    private static window gameWindow;
    private static Arena arena;
    private static int movesCounter;

    public static void main(String[] a) {
        Thread client = new Thread(new MainClient());
        client.start();
    }

    @Override
    public void run() {
        // choose a game out of 24 games available [0,23]
        int scenario_num = 11;
        game_service game = Game_Server_Ex2.getServer(scenario_num);

        directed_weighted_graph g = json2graph(game);

        init(game);

        game.startGame();

        long sleepTime=100;

        while(game.isRunning()) {

            moveAgents(game, g);
            try {
                gameWindow.repaint();
                Thread.sleep(sleepTime);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        double FinalScore=0;
        for(CL_Agent agent: arena.getAgents()){
            FinalScore += agent.getValue();
        }
        System.out.println("Final Score is - "+FinalScore+".");
        System.out.println( movesCounter +" moves has been made");
        System.exit(0);
    }

    private static void moveAgents(game_service game, directed_weighted_graph g) {
        //set agents
        String updatedArena = game.move();
        List<CL_Agent> agentList = Arena.getAgents(updatedArena, g);
        arena.setAgents(agentList);

        //set pokemons
        List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
        arena.setPokemons(pokemons);

        for(int i=0;i<agentList.size();i++) {
            CL_Agent agent = agentList.get(i);
            int id = agent.getID();
            int dest = agent.getNextNode();
            int src = agent.getSrcNode();

            if(dest==-1) {
                dest = nextNode(g, agent);
                calculateAgentsPath(game, agent);
                if(agent.path!=null){

                    game.chooseNextEdge(i, agent.path.get(0).getKey());

                    System.out.println("Agent: "+agent.getID()+", val: "+agent.getValue()+"   turned to node: "+agent.getNextNode());
                }
            }
        }
    }
    public static DS_DWGraph json2graph (game_service game){

        JSONObject jsonObj;
        DS_DWGraph g = new DS_DWGraph();
        try {
            jsonObj = new JSONObject(game.getGraph());
            JSONArray nodeJsonObj = jsonObj.getJSONArray("Nodes");
            JSONArray edgeJsonObj = jsonObj.getJSONArray("Edges");

            for(int i=0;i<nodeJsonObj.length();i++) {
                JSONObject node_dataObj = nodeJsonObj.getJSONObject(i);
                int key = node_dataObj.getInt("id");
                node_data n = new nodeData(key);
                g.addNode(n);
            }

            for(int i=0;i<edgeJsonObj.length();i++) {
                JSONObject edge_dataObj = edgeJsonObj.getJSONObject(i);
                int src = edge_dataObj.getInt("src");
                int dest = edge_dataObj.getInt("dest");
                double w = edge_dataObj.getDouble("w");
                g.connect(src, dest, w);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return g;
    }

    private static int nextNode(directed_weighted_graph g, CL_Agent agent) {
        List<node_data> path = agent.getPath();
        if(path!=null){
            if(!path.isEmpty()) {
                if (agent.getSrcNode() == path.get(0).getKey()) {
                    path.remove(0);
                }

                return path.get(0).getKey();
            }
        }
        return -1;
    }

    //todo check time left?
    private static void calculateAgentsPath(game_service game, CL_Agent agent) {
        directed_weighted_graph g = json2graph(game);

        dw_graph_algorithms ga = new Algo_DWGraph();
        ga.init(g);

        List<CL_Pokemon> pokemons = arena.getPokemons();
        List<CL_Agent> agents = arena.getAgents();
        PriorityQueue<PokemonEntry> pokemonQueue = agent.getPokemonsVal();
        init(game);
        for (int a = 0; a < pokemons.size(); a++) {
            Arena.updateEdge(pokemons.get(a), g);
        }
        for (CL_Pokemon pokemon : pokemons) {

            edge_data edge = pokemon.get_edge();
            //TODO maybe check dist to dest instead
            if(edge!=null){
                double distToPokemon = ga.shortestPathDist(agent.getSrcNode(), pokemon.get_edge().getDest());
                double huntValue = pokemon.getValue() / distToPokemon;

                pokemonQueue.add(new PokemonEntry(huntValue, pokemon));
            }
        }

        List<CL_Agent> hAgents = arena.getAgents();
        Iterator<CL_Agent> itr = hAgents.iterator();
        while(itr.hasNext()){
            CL_Agent iAgent = itr.next();
            PokemonEntry pEntry = agent.getPokemonsVal().poll();
            if(pEntry!=null) {

                CL_Pokemon pokemon = pEntry.getPokemon();

                if (pokemon.persecutedBy == -1) {
                    iAgent.setPath(ga.shortestPath(iAgent.getSrcNode(), pokemon.get_edge().getSrc()));
                    hAgents.remove(iAgent);
                } else {
                    iAgent.getPokemonsVal().peek().getValue();
                    CL_Agent currAgentAfter = agents.get(pokemon.getPersecutedBy());
                    //if new agent have better hunt Value
                    if (currAgentAfter.getPokemonsVal().peek().getValue() < iAgent.getPokemonsVal().peek().getValue()) {
                        //clear old
                        currAgentAfter.getPokemonsVal().poll();
                        hAgents.add(currAgentAfter);
                        //set new persecuted
                        iAgent.setPath(ga.shortestPath(iAgent.getSrcNode(), pokemon.get_edge().getSrc()));
                        hAgents.remove(iAgent);
                    } else {
                        //if this agent have worst huntVal then sent him to another pokemon
                        iAgent.getPokemonsVal().poll();
                        //sent agent to the end of list
                        hAgents.remove(iAgent);
                        hAgents.add(iAgent);
                    }
                }

            }
        }
    }


    private static void init (game_service game){

        String pokemonString = game.getPokemons();
//        directed_weighted_graph g = game.getJava_Graph_Not_to_be_used();
        directed_weighted_graph g = json2graph(game);

        arena = new Arena();
        arena.setGraph(g);
        arena.setPokemons(Arena.json2Pokemons(pokemonString));
        gameWindow = new window("test Ex2");
        gameWindow.setSize(1000, 700);
        gameWindow.panel.update(arena);
        gameWindow.panel.setTimeLeft(game.timeToEnd());


        gameWindow.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameServerJ = line.getJSONObject("GameServer");
            int agentsNum = gameServerJ.getInt("agents");
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());

            for (int i = 0; i < pokemons.size(); i++) {
                Arena.updateEdge(pokemons.get(i), g);
            }

            for (int i = 0; i < agentsNum; i++) {
                int ind = i % pokemons.size();
                CL_Pokemon pokemon = pokemons.get(ind);
                int dest = pokemon.get_edge().getDest();
                if (pokemon.getType() < 0) {
                    dest = pokemon.get_edge().getSrc();
                }

                game.addAgent(dest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

        directed_weighted_graph g = game.getJava_Graph_Not_to_be_used();
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

        String graphJ = game.getGraph();

        GsonBuilder builder = new GsonBuilder();
        DS_DWGraph g = new DS_DWGraph();
        builder.registerTypeAdapter(g.getClass(), new GameGraph_Deserializer() );
        Gson gson = builder.create();
        g = gson.fromJson(graphJ, g.getClass());

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
        directed_weighted_graph g = game.getJava_Graph_Not_to_be_used();

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
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < pokemons.size(); a++) {
                Arena.updateEdge(pokemons.get(a), g);
            }
            for (int a = 0; a < rs; a++) {
                int ind = a % pokemons.size();
                CL_Pokemon c = pokemons.get(ind);
                int nn = c.get_edge().getDest();
                if (c.getType() < 0) {
                    nn = c.get_edge().getSrc();
                }

                game.addAgent(nn);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainClient  implements Runnable{
    private static window gameWindow;
    private static Arena arena;
    private boolean loggedIn;

    public static void main(String[] a) {
        Thread client = new Thread(new MainClient());
        client.start();
    }

    @Override
    public void run() {
        // choose a game out of 24 games available [0,23]
        int scenario_num = 23;
        game_service game = Game_Server_Ex2.getServer(scenario_num);


        directed_weighted_graph g = json2graph(game);

        gameWindow = new window("Kfir&Tehila's Arena", game);
        gameWindow.setSize(1000, 700);

        init(game);

        game.startGame();

        long sleepTime = 80;

        while (game.isRunning()) {

            moveAgents(game, g);
            try {
                gameWindow.repaint();
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double FinalScore = 0;
        for (CL_Agent agent : arena.getAgents()) {
            FinalScore += agent.getValue();
        }

        System.out.println("Final Score is - " + FinalScore + ".");
        System.out.println(" game string - " + game.toString());
        System.exit(0);
    }

    private static void moveAgents(game_service game, directed_weighted_graph g) {
        game.move();


        //set agents
        String agentsString = game.getAgents();
        List<CL_Agent> agentList = arena.getAgents(agentsString, g);
        arena.setAgents(agentList);

        //set pokemons
        String pokemonsJ = game.getPokemons();
        ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(pokemonsJ);
        arena.setPokemons(pokemons);

        for(int i=0;i<agentList.size();i++) {
            CL_Agent agent = agentList.get(i);
            int id = agent.getID();
            int nextNode = agent.getNextNode();
            int src = agent.getSrcNode();

            if(nextNode==-1) {
                calculateAgentsPath(game, agent);
                if(agent.path!=null){
                    nextNode = getNextNode(g, agent);
                    if(src==nextNode){
                        nextNode = getNextNode(g, agent);
                    }
                    game.chooseNextEdge(i, nextNode);
                    game.move();

                }
            }
            game.move();
            System.out.println("Agent: "+agent.getID()+", val: "+agent.getValue()+"   turned to node: "+agent.getNextNode());
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
                String POS = node_dataObj.getString("pos");
                String [] XY = POS.split(",");
                double x = Double.parseDouble(XY[0]);
                double y = Double.parseDouble(XY[1]);
                geoLocation pos = new geoLocation (x,y,0);
                node_data n = new nodeData(key);
                n.setLocation(pos);
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

    private static int getNextNode(directed_weighted_graph g, CL_Agent agent) {
        List<node_data> path = agent.getPath();
        //TODO change to 1 if
        if(path!=null && !path.isEmpty()){
            node_data nextNode = path.remove(0);
            agent.setPath(path, null);

            return nextNode.getKey();
        }
        return -1;
    }

    //todo check time left?
    //TODO synchronize
    private static void calculateAgentsPath(game_service game, CL_Agent agent) {
        directed_weighted_graph g = json2graph(game);

        dw_graph_algorithms ga = new Algo_DWGraph();
        ga.init(g);

        String pokemonsString = game.getPokemons();
        List<CL_Pokemon> pokemons = arena.json2Pokemons(pokemonsString);
        for (int i = 0; i < pokemons.size(); i++) {
            Arena.updateEdge(pokemons.get(i), g);
        }

        PriorityQueue<PokemonEntry> pokemonQueue = agent.getPokemonsVal();

        for (CL_Pokemon pokemon : pokemons) {

            edge_data edge = pokemon.get_edge();
            //TODO maybe check dist to dest instead
            if(edge!=null){
                double distToPokemon = ga.shortestPathDist(agent.getSrcNode(), pokemon.get_edge().getSrc());
                double huntValue=0;
                if(agent.getSpeed()>0) {
                    huntValue = pokemon.getValue() / (distToPokemon / agent.getSpeed());
                }

                pokemonQueue.add(new PokemonEntry(huntValue, pokemon));
                //TODO set agent Queue
            }
        }
        LinkedList<CL_Agent> agentsList  = new LinkedList<>();
        agentsList.add(agent);

        while(!agentsList.isEmpty()){
            //TODO calculate speed
            //TODO check if already on the hunt
            CL_Agent iAgent = agentsList.removeFirst();
            PokemonEntry pEntry = agent.getPokemonsVal().poll();

            if (pEntry != null) {

                CL_Pokemon pokemon = pEntry.getPokemon();
                double thisHuntValue = pEntry.getValue();
                if (pokemon.persecutedBy == -1) {
                    List<node_data> pathToPSrc = ga.shortestPath(iAgent.getSrcNode(), pokemon.getSrc());
                    //TODO tehila add pokemon.getDest
                    node_data dest = g.getNode(pokemon.get_edge().getDest());
                    iAgent.setPath(pathToPSrc, dest);
                    pokemon.setPersecutedBy(iAgent.getID());
                    iAgent.setHuntValue(thisHuntValue);
                } else {
                    if(iAgent.getPokemonsVal().isEmpty()){

                    }
                    CL_Agent currAgentAfter = arena.getAgents().get(pokemon.getPersecutedBy());

                    double otherHuntValue = currAgentAfter.getHuntValue();

                    //if new agent have better hunt Value
                    if (otherHuntValue < thisHuntValue) {
                        //clear old
                        currAgentAfter.getPokemonsVal().poll();
                        agentsList.add(currAgentAfter);

                        //set new persecuted
                        iAgent.setPath(ga.shortestPath(iAgent.getSrcNode(), pokemon.get_edge().getSrc()), g.getNode(pokemon.get_edge().getDest()));
                        pokemon.setPersecutedBy(iAgent.getID());
                        iAgent.setHuntValue(thisHuntValue);
                    } else {
                        //if this agent have worst huntVal then sent him to another pokemon
                        iAgent.getPokemonsVal().poll();
                        //sent agent to the end of list
                        agentsList.add(iAgent);
                    }
                }

            }

        }

    }

    private static void init (game_service game){

        String pokemonString = game.getPokemons();
        directed_weighted_graph g = json2graph(game);

        arena = new Arena();
        arena.setGraph(g);



//        if(!gameWindow.isLoggedIn())
//        arena.setPokemons(Arena.json2Pokemons(pokemonString));
        gameWindow.panel.update(arena);
        gameWindow.show();

        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject gameServerJ = line.getJSONObject("GameServer");
            int agentsNum = gameServerJ.getInt("agents");

            List<CL_Pokemon> pokemons = Arena.json2Pokemons(game.getPokemons());

            for (int i = 0; i < pokemons.size(); i++) {
                Arena.updateEdge(pokemons.get(i), g);
            }

            Comparator cmp = new geoLoCompPokemon();
            CL_Pokemon minP =  Collections.min(pokemons , cmp);
            CL_Pokemon maxP= Collections.max(pokemons , cmp);

            Collections.sort(pokemons);
            geoLocation firsAxes = new geoLocation(0, 0, 0);

            double min = minP.getLocation().distance(firsAxes);
            double max = maxP.getLocation().distance(firsAxes);
            double rang = ((max-min)/agentsNum);

            ArrayList <CL_Pokemon> pStartWith = new ArrayList<>();

            int i =0;

                while( i<pokemons.size() && i != agentsNum ) {
                    CL_Pokemon curr = pokemons.get(i);
                    if (curr.getLocation().distance(minP.getLocation()) < rang+min) {
                        pStartWith.add(curr);
                        rang += rang;
                        pokemons.remove(i);
                        i++;
                    }
                    else {
                        i++;
                    }
                }

            i=0;
            int size = pStartWith.size();
            while (size < agentsNum){
                CL_Pokemon curr = pokemons.get(i);
                if (! pStartWith.contains(curr)){
                    pStartWith.add(curr);
                    size++;
                }
                i++;
            }
            for (int j = 0; j < agentsNum; j++) {
                CL_Pokemon startDest = pStartWith.get(j);
                int nodeStart = startDest.getSrc();
//                game.addAgent(nodeStart);
                game.addAgent(1); //test
            }


            arena.setPokemons(pokemons);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

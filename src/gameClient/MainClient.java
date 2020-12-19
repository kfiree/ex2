package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class MainClient  implements Runnable {
    private static window gameWindow;
    private static Arena arena;
    private boolean loggedIn;
    private static List<CL_Pokemon> persecutedList = new ArrayList<>();
    private static HashMap<CL_Pokemon, Integer> targetsMap = new HashMap(); // HashMap<pokemon,purser.id>

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

        gameWindow = new window("Kfir&Tehila's Arena", game);
        gameWindow.setSize(1000, 700);

        init(game);

        game.login(207404567);
        game.startGame();

        long sleepTime = 115;

        while(game.isRunning()) {
//            synchronized (Thread.currentThread()) {
                game.move();
                moveAgents(game, g);
//            }
            try {
                gameWindow.repaint();
                Thread.sleep(sleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //        for(CL_Agent agent: arena.getAgents()){
        //        while (game.isRunning()) {
        //
        //            moveAgents(game, g);
        //            try {
        //                gameWindow.repaint();
        //                Thread.sleep(sleepTime);
        //            } catch (Exception e) {
        //                e.printStackTrace();
        //            }
        //        }
        double FinalScore = 0;
        for (CL_Agent agent : arena.getAgents()) {
            FinalScore += agent.getValue();
        }

        System.out.println("Final Score is - " + FinalScore + ".");
        System.out.println(" game string - " + game.toString());
        System.exit(0);
    }

    private static void moveAgents(game_service game, directed_weighted_graph g) {

        //set agents
        String agentsString = game.getAgents();
        List<CL_Agent> agentList = arena.getAgents(agentsString, g);
        arena.setAgents(agentList);


        //set pokemons
        String pokemonsJ = game.getPokemons();
        ArrayList<CL_Pokemon> pokemons = Arena.json2Pokemons(pokemonsJ);
        arena.setPokemons(pokemons);

        for (CL_Pokemon oldP : persecutedList) {
            for (CL_Pokemon pokemon : pokemons) {
                if (pokemon.getLocation().x() == oldP.getLocation().x() && pokemon.getLocation().y() == oldP.getLocation().y() && oldP.getPersecutedBy() != -1) {
                    pokemon.setPersecutedBy(oldP.getPersecutedBy());
                }
            }
        }
        for (int i = 0; i < agentList.size(); i++) {
            CL_Agent agent = agentList.get(i);
            int id = agent.getID();
            int nextNode = agent.getNextNode();
            int src = agent.getSrcNode();

            if (nextNode == -1) {
                calculateAgentsPath(game, agent);
                if (agent.path != null) {
                    nextNode = getNextNode(g, agent);
                    if (src == nextNode) {
                        nextNode = getNextNode(g, agent);
                    }
                    game.chooseNextEdge(i, nextNode);

                }
            }
            System.out.println("Agent: "+agent.getID()+", val: "+agent.getValue()+"   turned to node: "+agent.getNextNode());
        }
    }

    public static DS_DWGraph json2graph(game_service game) {

        JSONObject jsonObj;
        DS_DWGraph g = new DS_DWGraph();
        try {
            jsonObj = new JSONObject(game.getGraph());
            JSONArray nodeJsonObj = jsonObj.getJSONArray("Nodes");
            JSONArray edgeJsonObj = jsonObj.getJSONArray("Edges");

            for (int i = 0; i < nodeJsonObj.length(); i++) {
                JSONObject node_dataObj = nodeJsonObj.getJSONObject(i);
                int key = node_dataObj.getInt("id");
                String POS = node_dataObj.getString("pos");
                String[] XY = POS.split(",");
                double x = Double.parseDouble(XY[0]);
                double y = Double.parseDouble(XY[1]);
                geoLocation pos = new geoLocation(x, y, 0);
                node_data n = new nodeData(key);
                n.setLocation(pos);
                g.addNode(n);
            }

            for (int i = 0; i < edgeJsonObj.length(); i++) {
                JSONObject edge_dataObj = edgeJsonObj.getJSONObject(i);
                int src = edge_dataObj.getInt("src");
                int dest = edge_dataObj.getInt("dest");
                double w = edge_dataObj.getDouble("w");
                g.connect(src, dest, w);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g;
    }

    private static int getNextNode(directed_weighted_graph g, CL_Agent agent) {
        List<node_data> path = agent.getPath();
        //TODO change to 1 if & write implementation in agent
        if (path != null && !path.isEmpty()) {
            node_data nextNode = path.remove(0);
            agent.setPath(path);

            return nextNode.getKey();
        }
        return -1;
    }

    //todo check time left?
    //TODO synchronize
    private static synchronized void calculateAgentsPath(game_service game, CL_Agent agent) {
        //set graph
        directed_weighted_graph g = json2graph(game);
        dw_graph_algorithms ga = new Algo_DWGraph();
        ga.init(g);

        //set Pokemons and targetsMap
        updatePokemons(game, ga, agent);

        //find target for idle agents
        List<CL_Agent> agents = arena.getAgents();
        LinkedList<CL_Agent> idleAgents = new LinkedList<>();
        idleAgents.add(agent);

        //ToDelete
        {
            for (int i = 0; i < agents.size(); i++) {
                CL_Agent agent1 = agents.get(i);
                List<node_data> path1 = agent1.getPath();
                for (int j = i + 1; j < agents.size(); j++) {
                    CL_Agent agent2 = agents.get(j);
                    List<node_data> path2 = agent2.getPath();
                    if (!path1.isEmpty() && !path2.isEmpty()) {
                        node_data target1 = path1.get(path1.size() - 1);
                        node_data target2 = path2.get(path2.size() - 1);
                        if (target1 == target2) {
                            System.out.println("at " + game.timeToEnd() + ", " + agent1.getID() + " and " + agent2.getID() + " both went to " + target1.getKey());
                        }
                    }
                }
            }
            ArrayList<Integer> targetsCheck = new ArrayList<>();
            for (int i = 0; i < agents.size(); i++) {
                targetsCheck.add(-1);
            }

            for (int i = 0; i < agents.size(); i++) {
                List<node_data> path = agents.get(i).getPath();
                if (path.size() > 0) {
                    node_data node_data = path.get(path.size() - 1);
                    targetsCheck.set(i, node_data.getKey());
                }
            }
        }

        while (!idleAgents.isEmpty()) {
            CL_Agent iAgent = idleAgents.removeFirst();

            //check first target in agent's queue
            PokemonEntry pEntry = agent.pollTarget();
            if (pEntry != null) {
                CL_Pokemon pokemon = pEntry.getPokemon();
                double agentHuntVal = pEntry.getValue();

                // 1) pokemon have no pursuer
                int pursuerID = getPursuerID(pokemon);
                if(pursuerID == -1) {    //TODO tehila add pokemon.getDest
                    //check if some other agent will get pokemon faster
                    if(agentHuntVal > agentsBestVal(pokemon, ga, iAgent.getID())){
                        //set new pursuer
                        updateHunt(pokemon, iAgent, agentHuntVal, ga);
                    }
                    //here
                } else {
                    //get pokemon's pursuer
                    CL_Agent pursuer = agents.get(pursuerID);

                    // 2) iAgent have better hunt Value
                    if (pursuer.getHuntValue() < agentHuntVal) {
                        //clear pursuer path and add him to idleAgents
                        pursuer.resetPath();
                        idleAgents.add(pursuer);

                        //set new pursuer
                        updateHunt(pokemon, iAgent, agentHuntVal, ga);

                    } else {
                        // 3) iAgent don't have better hunt Value
                        idleAgents.add(iAgent);
                    }

                }

            }

        }
    }

    private static int getPursuerID(CL_Pokemon pokemon){
        for(Map.Entry E: targetsMap.entrySet()){
            CL_Pokemon P = (CL_Pokemon) E.getKey();
            if(P.sameAs(pokemon)){
                return (int) E.getValue();
            }
        }
        return -1;
    }

    //try to predict other agents next move
    private static double agentsBestVal(CL_Pokemon target, dw_graph_algorithms ga, int currID){
        List<CL_Agent> agents = arena.getAgents();

        double agentsBestVal = 0;
        for(CL_Agent agent : agents){
            if(agent.getID()!=currID){
                List<node_data> path = agent.getPath();
                if (!path.isEmpty()) {
                    double nextPath = ga.shortestPathDist(path.get(path.size()-1).getKey(), target.get_edge().getSrc());
                    double nextHuntValue = target.getValue() / (nextPath / agent.getSpeed());//huntValue = value/journey time
                    nextHuntValue += agent.getHuntValue();

                    agentsBestVal = Math.max(nextHuntValue, agentsBestVal);
                }
            }
        }
        return agentsBestVal;
    }

    private static void updatePokemons(game_service game, dw_graph_algorithms ga, CL_Agent agent){
        //set pokemons
        String pokemonsString = game.getPokemons();
        List<CL_Pokemon> pokemons = arena.json2Pokemons(pokemonsString);
        for (int i = 0; i < pokemons.size(); i++){
            Arena.updateEdge(pokemons.get(i), ga.getGraph());
        }

        //update Targets Map
        updateTargetsMap(pokemons);

        //order pokemons in Queue by their huntValue
        for (CL_Pokemon pokemon : pokemons) {
            edge_data edge = pokemon.get_edge();
            if (edge != null) {
                double pathDist = ga.shortestPathDist(agent.getSrcNode(), pokemon.get_edge().getSrc());
                if (agent.getSpeed() > 0) {
                    double huntValue = pokemon.getValue() / (pathDist/agent.getSpeed());//huntValue = value/journey time
                    agent.addToQueue(pokemon, huntValue);
                }
            }
        }
    }


    private static void updateHunt(CL_Pokemon pokemon, CL_Agent agent, double huntVal, dw_graph_algorithms ga){
        //build path
        node_data dest = ga.getGraph().getNode(pokemon.get_edge().getDest());
        List<node_data> path2P = ga.shortestPath(agent.getSrcNode(), pokemon.getSrc());
        path2P.add(dest);
        agent.setPath(path2P);

        //update hunt info
        agent.setPath(path2P);
        targetsMap.put(pokemon, agent.getID());
        agent.setHuntValue(huntVal);

    }

    //TODO finish this
    //    private static void getClusters(int partsNum){
    //        directed_weighted_graph g = arena.getGraph();
    //        dw_graph_algorithms ga = new Algo_DWGraph();
    //        ga.init(g);
    //        Collection<node_data> nodes = g.getV();
    //        Iterator<node_data> itr = nodes. iterator();
    //        double maxX, maxY, minX, minY;
    //        geo_location nodeLocation;
    //
    //        node_data node;
    //        if(itr.hasNext()) {
    //            node = itr.next();
    //            nodeLocation = node.getLocation();
    //
    //            double x = nodeLocation.x();
    //            double y = nodeLocation.y();
    //
    //            minX = x;
    //            maxX = x;
    //            maxY = y;
    //            minY = y;
    //
    //            while (itr.hasNext()) {
    //                x = nodeLocation.x();
    //                y = nodeLocation.y();
    //                node = itr.next();
    //                nodeLocation = node.getLocation();
    //
    //                if (x > maxX) {
    //                    maxX = x;
    //                } else if (x < minX) {
    //                    minX = x;
    //                }
    //
    //                if (y > maxY) {
    //                    maxY = y;
    //                } else if (y < minY) {
    //                    minY = y;
    //                }
    //            }
    //
    //            double centerX = (maxX - minX) / 2 + minX;
    //            double centerY = (maxY - minY) / 2 + minY;
    //            double radious = Math.min(maxX - minX,maxY - minY);
    //            ArrayList<geo_location> clusterCenters = new ArrayList<>();
    //            for (int i = 1; i <= partsNum; i++) {
    //                double angle = i*(360/partsNum);
    //                double pX = centerX + radious*Math.cos(angle);
    //                double pY = centerY + radious*Math.sin(angle);
    //                geo_location p = new geoLocation(pX, pY, 0);
    //                clusterCenters.add(p);
    //            }
    //
    //            HashMap<Integer, node_data> nodeMap = new HashMap<>();
    //            for(node_data n:nodes){
    //                int
    //            }
    //        }
    //    }

    private static void updateTargetsMap(List<CL_Pokemon> pokemons){

        List<CL_Pokemon> targets = new ArrayList<>();
        List<CL_Pokemon> toClean = new ArrayList<>();
        targets.addAll(targetsMap.keySet());

        //clean duplicates from targetsMap
        for(int i=0; i<targets.size()-1; i++){
            CL_Pokemon P1 = targets.get(i);
            for(int j=i+1; j<targets.size(); j++){
                CL_Pokemon P2 = targets.get(j);
                if(P1.sameAs(P2)){
                    if(targetsMap.get(P1) != -1) {
                        toClean.add(P1);
                    }else{toClean.add(P2);}
                }
            }
        }

        //clean persecutedList from pokemon that was already taken
        boolean Taken = false;
        for(int i=0; i<targets.size(); i++){
            CL_Pokemon target = targets.get(i);
            for(int j=0; j<pokemons.size(); j++){
                //remove target if wasn't captured yet
                if(target.sameAs(pokemons.get(j))){
                    Taken = true;
                }
            }
            if(!Taken){
                toClean.add(target);
                Taken = false;
            }
        }

        //remove duplicates and non-existed pokemons from targetMap
        for(CL_Pokemon p : toClean) {
            targetsMap.remove(p);
        }
    }

    private static void init(game_service game){

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

            //set pokemons and add them to targetsMap
            for (int i = 0; i < pokemons.size(); i++) {
                Arena.updateEdge(pokemons.get(i), g);
                targetsMap.put(pokemons.get(i), -1);
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
                game.addAgent(nodeStart);
                //                game.addAgent(1); //toDelete
            }


            arena.setPokemons(pokemons);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


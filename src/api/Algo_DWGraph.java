package api;

import java.util.*;

public class Algo_DWGraph implements dw_graph_algorithms{

    private directed_weighted_graph graph = new DS_DWGraph();
    private static int visitedTag = 1;

    @Override
    public void init(directed_weighted_graph g) {
        if(g == null)
            this.graph=new DS_DWGraph();
        else
            this.graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }

    @Override
    public directed_weighted_graph copy() {

        return new DS_DWGraph(this.graph);
    }

    //TODO rewrite isConnected,
    //TODO rewrite path's method
    @Override
    public boolean isConnected() {
        if(graph.nodeSize()<2){
            return true;
        }
        //add src to queue - nodesQ, and set his tag to visitedTag (indicates that the node were visited)
        Queue<node_data> nodesQ = new LinkedList<>();
        node_data srcNode = graph.getNode(this.graph.getV().iterator().next().getKey());
        nodesQ.add(srcNode);
        srcNode.setTag(visitedTag);
        int visitedCounter=1;

        //iterate over graph
        while(!nodesQ.isEmpty()) {
            node_data node = nodesQ.poll();

            for(edge_data next : graph.getE(node.getKey())) {
                if(next.getTag() != visitedTag) {
                    nodesQ.add(graph.getNode(next.getDest()));
                    next.setTag(visitedTag); //mark as visited
                    visitedCounter++;
                }
            }
        }
        visitedTag++;

        return visitedCounter==graph.nodeSize();
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        //if no such path return -1
        if(this.graph.getNode(src) ==null || this.graph.getNode(dest)==null)
            return -1;

        //set path info
        setPathInfo(src, dest);

        //return path distance
        return graph.getNode(dest).getWeight();
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {

        //if node doesn't exists return null
        if(this.graph.getNode(src) ==null || this.graph.getNode(dest)==null)
            return null;

        List<node_data> path = new ArrayList<>();

        //if dest doesn't exist return null
        node_data targetNode = graph.getNode(dest);
        if(targetNode == null)
            return null;

        //set path info
        setPathInfo(src, dest);

        //if there is no path from ex1.src to dest return null
        if(targetNode.getInfo()==null)
            return null;

        path.add(graph.getNode(dest));

        //reconstruct path using nodes's info (represent previous)
        node_data node = graph.getNode(dest);
        while(node.getKey()!= src && node.getInfo()!=null){
            //get prev in path
            int nextKey = Integer.parseInt(node.getInfo());
            node = graph.getNode(nextKey);
            path.add(node);
        }
        Collections.reverse(path);

        return path;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }

    /**
     * update all node's data (info and tag) using dijkstra's algorithm
     * node's tag = distance between node to source node
     * node's info = key of previous node in the shortest path to destination
     *
     * @param src start node
     * @param dest end (target) node
     */

    //TODO change to kashir mamash
    private void setPathInfo(int src, int dest) {

        PriorityQueue<node_data> nieQ = new PriorityQueue<>();
        HashMap<Integer, node_data> unvisited = new HashMap<>();

        //reset nodes data and add to unvisited
        for(node_data node : graph.getV()) {
            node.setTag(-1);
            node.setInfo(null);
            unvisited.put(node.getKey(), node);
        }

        //set first node data
        graph.getNode(src).setTag(0);
        nieQ.add(graph.getNode(src));
        graph.getNode(src).setTag(0);

        //iterate the nodes
        while(!nieQ.isEmpty()){
            //get node with shortest path
            node_data node = nieQ.poll();
            double prevDist = node.getTag();

            //TODO check if node's weight is used right
            // update node's neighbours data
            for (edge_data next : graph.getE(node.getKey())) {
                //update new path is is shorter
                double curDist = prevDist + next.getWeight();
                if(next.getTag()==-1 || next.getTag() > curDist) {
                    graph.getNode(next.getDest()).setWeight(curDist);
                    next.setInfo(String.valueOf((node.getKey())));
                }

                //if unvisited add to queue
                int edgeDest = next.getDest();
                if (unvisited.get(edgeDest)!=null) {
                    nieQ.add(graph.getNode(edgeDest));
                }

            }
            unvisited.remove(node.getKey());
        }
    }
}

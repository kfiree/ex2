package api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class represents a directed Weighted Graph Theory Algorithms
 * The implementation relies on using BFS, Kosaraju and Dijkstra's algorithms
 */
public class Algo_DWGraph implements dw_graph_algorithms {

    private directed_weighted_graph graph = new DS_DWGraph();
    /**
     * Init the graph on which this set of algorithms operates on.
     */
    @Override
    public void init(directed_weighted_graph g) {
        if (g == null)
            this.graph = new DS_DWGraph();
        else
            this.graph = g;

    }
    /**
     * Return the underlying graph of which this class works.
     */
    @Override
    public directed_weighted_graph getGraph() {
        return this.graph;
    }
    /**
     * Compute a deep copy of this weighted graph.
     */
    @Override
    public directed_weighted_graph copy() {
        return new DS_DWGraph(this.graph);
    }


    @Override
    /**
     * Returns true if and only if there is a valid path from each node to each
     * other nodes- witch means there is a valid path from node to each other nodes,
     * and after switch directions of edges still there is a valid path from same node to each other nodes.
     */
    public boolean isConnected() {

        //if graph is empty or with only one node return true
        if (this.graph.nodeSize() < 2) {
            return true;
        }
        //if there is no path from node to each others return false
        if (!BFS()) {
            return false;
        }
        //switch directions of edges and return if there is still path from node to each others
        return OppositeBFS((DS_DWGraph)this.graph);
    }

    /**
     * travers the graph through nodes neighbors using BFS algorithm
     * node's tag: if the node visited or not. visited=-1, not visited=1.
     * @return true if the graph connected
     */
    private boolean BFS() {
        Queue<node_data> queue = new LinkedList<>();
        int visitedCounter = 0;

        //reset all tags and weight of nodes in graph: T=-1 W=0
        //tag represent visited
        resetTagAndWeight(this.graph);

        //add the first node in graph to queue, mark as visited, update the visitedCounter
        Collection<node_data> G = this.graph.getV();
        Iterator<node_data> itr = G.iterator();
        if (itr.hasNext()) {
            node_data src = itr.next();
            queue.add(src);
            visitedCounter++;
            src.setTag(1);

            while (!queue.isEmpty()) {
                //src is the first element in queue
                src = queue.poll();

                //go through all edges connect to src
                Collection<edge_data> NA = this.graph.getE(src.getKey());
                if (NA != null) {
                    for (edge_data edge : NA) {

                        //dst is the node connect to src with edge
                        node_data dst = this.graph.getNode(edge.getDest());

                        //if dst not visited: add to queue, mark as visited, update the visitedCounter
                        if (dst.getTag() == -1) {
                            dst.setTag(1);
                            queue.add(dst);
                            visitedCounter++;
                        }
                    }
                }
            }
        }
        //the graph connected if visitedCounter == num of nodes in graph
        return visitedCounter==this.graph.nodeSize();
    }
    /**
     * travers the graph through nodes neighbors
     * with opposite direction of edges, using BFS algorithm
     * node's tag: if the node visited or not. visited=-1, not visited=1.
     * @return true if the graph with opposite direction connected
     */
    private boolean OppositeBFS(DS_DWGraph g) {
        Queue<node_data> queue = new LinkedList<>();
        int visitedCounter = 0;

        //reset all tags and weight of nodes in graph: T=-1 W=0
        //tag represent visited
        resetTagAndWeight(g);

        //add the first node in graph to queue, mark as visited, update the visitedCounter
        Collection<node_data> G = g.getV();
        Iterator<node_data> itr = G.iterator();
        if (itr.hasNext()) {
            node_data src = itr.next();
            queue.add(src);
            visitedCounter++;
            src.setTag(1);

            while (!queue.isEmpty()) {
                //src is the first element in queue
                src = queue.poll();

                //go through all edges with opposite direction connect to src
                Collection<edge_data> NA = g.getOppositE(src.getKey());
                if (NA != null) {
                    for (edge_data edge : NA) {

                        //dst is the node connect to src with switched direction of edge
                        node_data dst = g.getNode(edge.getDest());

                        //if dst not visited: add to queue, mark as visited, update the visitedCounter
                        if (dst.getTag() == -1) {
                            dst.setTag(1);
                            queue.add(dst);
                            visitedCounter++;
                        }
                    }

                }
            }
        }
        //the graph connected if visitedCounter == num of nodes in graph
        return visitedCounter==g.nodeSize();
    }


    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public double shortestPathDist(int src, int dest) {

        node_data start = this.graph.getNode(src);
        node_data end = this.graph.getNode(dest);

        //if src or dest not in graph there is no path
        if (end == null || start == null){
            return -1;
        }
        //the path from node to itself is 0
        if (src == dest ){
            return 0;
        }

        //update node's data (weight and tag)
        //Weight: distance between node to src in shortest path
        //tag: key of previous node in the path from src
        dijkstra(src, dest);

        //if the weight of node dest is 0: there is no path
        if (end.getWeight() != 0) {
            return end.getWeight();
        }
        return -1;

    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {

        node_data start = this.graph.getNode(src);
        node_data end = this.graph.getNode(dest);

        //if src or dest not in graph there is no path
        if (end == null || start == null){
            return null;
        }
        LinkedList <node_data> list = new LinkedList<>();

        //the path from node to itself
        if (src == dest){
            list.add(start);
            return list;
        }

        //update node's data (weight and tag)
        //Weight: distance between node to src in shortest path
        //tag: key of previous node in the path from src
        dijkstra(src, dest);

        //if the weight of node dest is 0: there is no path
        if (end.getWeight() != 0) {
            //go through all previous nodes from dest to src and add to list
            while (end.getTag() != end.getKey() ){ //the tag of src is src
                list.addFirst(end);
                end = this.graph.getNode(end.getTag());
            }
                list.addFirst(start);
            return list;
        }
        return null;
    }


    /**
     * travers the graph through nodes neighbors using dijkstra's algorithm
     * the Priority Queue prior the nodes by the dist from node src
     * update node's data (weight and tag):
     * node's Weight: distance between node to src
     * node's tag: if the node visited or not. not visited=-1, visited= key of previous node in the path from src
     * @param src  start node
     * @param dest end (target) node
     */

    private void dijkstra (int src, int dest) {

        node_data start = graph.getNode(src);
        node_data end = graph.getNode(dest);

        //reset all tags and weight of nodes in graph: T=-1 W=0
        //tag represent visited and node father
        //weight represent the dist of node from src
        resetTagAndWeight(this.graph);

        //if nodes in graph
        if ((start != null) && (end != null)) {

            PriorityQueue<node_data> pQueue = new PriorityQueue<node_data>();
            node_data nodeCurr;

            //add src to queue
            pQueue.add(start);
            //set node father of src as src itself
            start.setTag(src);

            //make destEdges as hash represent all edges connect to dest
            //if there is no edge- there is no path
            //if all edges are visited- we surly know the shortest path because using pQueue
            HashMap<edge_data, Integer> destEdges = new HashMap<>();
            DS_DWGraph g = (DS_DWGraph)this.graph;
            Collection<edge_data> OE = g.getOppositE(dest);
            Collection<edge_data> E = graph.getE(dest);
            for (edge_data e : OE) {
                destEdges.put(e, dest);
            }
            for (edge_data e : E) {
                destEdges.put(e, dest);
            }

            //if there is nodes in pQueue and there is destEdges not visited
            while ((!pQueue.isEmpty() && (!destEdges.isEmpty()))) {

                //nodeCurr is the first element in pQueue
                nodeCurr = pQueue.poll();

                //go through all edges connect to nodeCurr
                Collection<edge_data> Edges = this.graph.getE(nodeCurr.getKey());
                if (Edges != null) {
                    for (edge_data edge : Edges) {

                        //dst is the node connect to nodeCurr with edge
                        node_data dst = this.graph.getNode(edge.getDest());

                        //if dst is not the father of nodeCurr
                        if (nodeCurr.getTag() != dst.getKey()) {

                            //if dst not visited
                            if (dst.getTag() == -1) {
                                dst.setTag(nodeCurr.getKey()); //set father as nodeCurr
                                dst.setWeight(nodeCurr.getWeight() + edge.getWeight()); //set the weight as dist
                                pQueue.add(dst); //add to pQueue
                            }
                            //if dst visited: compare the path from src->dst with the path src->nodeCurr->dst
                            //if the new path is smaller:
                            else if (dst.getWeight() > nodeCurr.getWeight()+edge.getWeight()){
                                dst.setTag(nodeCurr.getKey()); //update father as nodeCurr
                                dst.setWeight(nodeCurr.getWeight()+edge.getWeight()); //update the new weight
                            }
                        }
                        //if the edge connect to dst: remove it from destEdges hash
                        if (destEdges.get(edge) != null) {
                            destEdges.remove(edge);
                        }
                    }
                }
            }
        }
    }

    /**
     * reset all Tag and Info by given graph
     * @param g - directed_weighted_graph
     */
    private void resetTagAndWeight(directed_weighted_graph g) {
        Collection<node_data> G = g.getV();
        for (node_data n : G) {
            n.setTag(-1);
            n.setWeight(0);
        }
    }
    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.graph);
        try
        {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
        }
        catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {

        DS_DWGraph g = new DS_DWGraph();

        try
        {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(g.getClass(), new DS_DWGraphJsonDeserializer() );
            Gson gson = builder.create();

            FileReader reader = new FileReader(file);
            g = gson.fromJson(reader, g.getClass());
        }
        catch (FileNotFoundException e) {
            return false;
        }
        this.graph =g;
        return true;
    }


}

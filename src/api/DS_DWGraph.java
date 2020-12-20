package api;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.HashMap;
/**
 * This class represents a directional weighted graph.
 * The gtaph support a large number of nodes (over 100,000).
 * The graph implementation relies on using Hash Map represent the nodes of graph
 * and another Hash Map represent the edges of graph
 */
public class  DS_DWGraph implements  directed_weighted_graph{

    @SerializedName("Nodes")
    private HashMap<Integer, node_data> nodes = new HashMap<>();
    @SerializedName("Edges")
    private HashMap<Integer, HashMap<Integer, edge_data>> edgesFromNode = new HashMap<>(); //edges<src,<dest, edges starting (source) >>
    private HashMap<Integer, HashMap<Integer, edge_data>> edgesToNode = new HashMap<>(); //edges<dest,<src, edges ending (dest)>>


    private int modeCounter, edgeCounter;

    /**
     * empty constructor
     */
    public DS_DWGraph() {
        this.modeCounter =0;
        this.edgeCounter =0;
    }

    /**
     * copy constructor
     */
    public DS_DWGraph(directed_weighted_graph other) {
        //set graph's nodes
        Collection<node_data> oNodes = other.getV();
        if (oNodes != null) {
            for (node_data node : oNodes) {
                addNode(new nodeData(node));
            }
        }
        for (int nodeKey : edgesFromNode.keySet()) {
            Collection<edge_data> nodeNei = other.getE(nodeKey);
                if (nodeNei != null){
                    for (edge_data edge : nodeNei ) {
                        connect(nodeKey, edge.getDest(), edge.getWeight());
                    }
            }
        }
        //set counters
        this.modeCounter = other.getMC();
        this.edgeCounter = other.edgeSize();
    }
    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }
    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     */
    @Override
    public edge_data getEdge(int src, int dest) {

        if (nodes.get(src) != null && src!=dest) {

            return edgesFromNode.get(src).get(dest);
        }
        return null;
    }
    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     */
    @Override
    public void addNode(node_data node) {
        int size = nodes.size();
        if(node!=null && nodes.get(node.getKey()) == null) {
            nodes.put(node.getKey(), node);
            edgesFromNode.put(node.getKey(), new HashMap<>());
            edgesToNode.put(node.getKey(), new HashMap<>());
            if (size != nodes.size()) {
                modeCounter++;
            }
        }
    }
    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if( (nodes.get(src)!=null) && (nodes.get(dest)!=null) && (src != dest) && (w>=0) ) {
            //if edge not exist
            if (getEdge(src, dest) == null) {
                edgeCounter++;
                modeCounter++;
            }
            //if edge exist but w changes
            else if ((getEdge(src, dest).getWeight()) != w) {
                modeCounter++;
            }
            //add new edge to edges
            edge_data newEdge = new edgeData(src, dest, w);
            edge_data oppositNewEdge = new edgeData(dest, src, w);
            this.edgesFromNode.get(src).put(dest, newEdge);
            this.edgesToNode.get(dest).put(src, oppositNewEdge);
        }

    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     * @return Collection(node_data)
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }
    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     * @return Collection(edge_data)
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (nodes.get(node_id) != null) {
            return edgesFromNode.get(node_id).values();
        }
        return null;
    }

    /**
     * This method return collection representing all the edges getting in to
     * the given node (all the edges ending (dest) at the given node).
     */
    public Collection<edge_data> getOppositE (int node_id) {
        if (nodes.get(node_id) != null) {
            return edgesToNode.get(node_id).values();
        }
        return null;
    }
    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {

        node_data removedNode = nodes.remove(key);

        if (edgesToNode.get(key) != null) {
            modeCounter++; //if removedNode != null

            //remove the edges that key is the dest
            Collection<Integer> removeNA = edgesToNode.get(key).keySet();
            if (removeNA != null) {
                for (int src : removeNA) {
                    edgesFromNode.get(src).remove(key);
                    modeCounter++;
                }
            }
            //remove the edges that key is the src
            Collection<Integer> removeONA = edgesFromNode.get(key).keySet();
            if (removeONA != null) {
                for (int src : removeNA) {
                    edgesToNode.get(src).remove(key);
                }
            }

            int size_edgesFromNode = edgesFromNode.remove(key).size();  //remove the edges that key is the src
            int size_edgesToNode = edgesToNode.remove(key).size();
            int size = size_edgesFromNode + size_edgesToNode; //num of edges key is dest+key is src

            modeCounter += size_edgesFromNode;
            edgeCounter -= size;
        }

        return removedNode;
    }
    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {

        if(nodes.get(src)!=null && nodes.get(dest)!=null){
            edge_data removedEdge = edgesFromNode.get(src).remove(dest);
            edgesToNode.get(dest).remove(src);

            if(removedEdge!=null) { //if there is an edge between src and dest
                modeCounter++;
                edgeCounter--;
            }
            return removedEdge;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {

        directed_weighted_graph g = (DS_DWGraph) o;

        if (g.nodeSize()!= nodeSize() || g.edgeSize()!= edgeSize()){
            return false;
        }
        if(! (g.getV().containsAll(getV()))) {
            return false;
        }
            for (node_data node :getV()){
                if(!(g.getE(node.getKey()).containsAll(getE(node.getKey())))){
                    return false;
                }
            }
        return true;
    }


    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     */
    @Override
    public int nodeSize() {
        return nodes.size();
    }
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     */
    @Override
    public int edgeSize() {
        return edgeCounter;
    }
    /**
     * Returns the Mode Count - for testing changes in the graph.
     */
    @Override
    public int getMC() {
        return modeCounter;
    }


}

package api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class  DS_DWGraph implements  directed_weighted_graph{

    //TODO add equals overRide for tests
    //TODO check for better mapping for edges
    private HashMap<Integer, node_data> nodes = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, edge_data>> edgesFromNode = new HashMap<>(); //edges<src,<dest, edges starting (source) >>
    private HashMap<Integer, HashMap<Integer, edge_data>> edgesToNode = new HashMap<>(); //edges<dest,<src, edges ending (dest)>>

    private int modeCounter, edgeCounter;
    //empty constructor
    public DS_DWGraph() {
        this.modeCounter =0;
        this.edgeCounter =0;
    }

    //copy constructor
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

    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    //TODO check if can do get on null (if src not in graph)
    public edge_data getEdge(int src, int dest) {

        return edgesFromNode.get(src).get(dest);
    }

    @Override
    public void addNode(node_data node) {
        //TODO check if edges.set is safe (what if key bigger then nodes
        //TODO check edges.set if node exist
        if(node!=null) {
            nodes.put(node.getKey(), node);
            edgesFromNode.put(node.getKey(), new HashMap<>());
            edgesToNode.put(node.getKey(), new HashMap<>());

            modeCounter++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        //TODO split to 2 if for time save
        if( (nodes.get(src)!=null) && (nodes.get(dest)!=null) && (w>0) ){


            //if edge not already exist
            if (getEdge (src, dest) == null) {
                //add new edge to edges
                edge_data newEdge = new edge(src, dest, w);
                edge_data oppositNewEdge = new edge(dest, src, w);
                this.edgesFromNode.get(src).put(dest, newEdge);
                this.edgesToNode.get(dest).put(src, oppositNewEdge);
                //manage counters
                edgeCounter++;
                modeCounter++;
            }
            //if edge exist but w changes
            else if (getEdge (src, dest).getWeight() != w){
                edge_data newEdge = new edge(src, dest, w);
                edge_data oppositNewEdge = new edge(dest, src, w);
                this.edgesFromNode.get(src).put(dest, newEdge);
                this.edgesToNode.get(dest).put(src, oppositNewEdge);
                modeCounter++;

            }
        }
    }

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

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
     * @param node_id
     * @return
     */
    public Collection<edge_data> getOppositE (int node_id) {
        if (nodes.get(node_id) != null) {
            return edgesToNode.get(node_id).values();
        }
        return null;
    }

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

            int size_edgesFromNode = edgesFromNode.remove(key).size();  //remove the edges that key is the src
            int size_edgesToNode = removeNA.size();
            int size = size_edgesFromNode + size_edgesToNode; //num of edges key is dest+key is src

            modeCounter += size_edgesFromNode;
            edgeCounter -= size;
        }

        return removedNode;
    }

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
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeCounter;
    }

    @Override
    public int getMC() {
        return modeCounter;
    }

    private class edge implements edge_data{
        private int src, dest, tag;
        private double weight;
        private String info;

        //empty constructor
        public edge() {
        }

        //copy constructor
        public edge(edge_data other) {
            this.src = other.getSrc();
            this.dest = other.getDest();
            this.weight = other.getWeight();
            this.info = other.getInfo();
            this.tag = other.getTag();
        }

        public edge(int src, int dest, double weight) {

            if (weight >0) {
                //edge from node to itself
                if (src == dest){
                    this.src = src;
                    this.dest = dest;
                    this.weight = 0;
                }
                else {
                    this.src = src;
                    this.dest = dest;
                    this.weight = weight;
                }
            }
        }

        @Override
        public int getSrc() {
            return this.src;
        }

        @Override
        public int getDest() {
            return this.dest;
        }

        @Override
        public double getWeight() {
            return this.weight;
        }

        @Override
        public String getInfo() {
            return this.info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return this.tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }
    }
}

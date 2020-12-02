package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class  DS_DWGraph implements  directed_weighted_graph{

    //TODO add equals overRide for tests
    //TODO check for better mapping for edges
    private HashMap<Integer, node_data> nodes = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, edge_data>> edges = new HashMap<>(); //edges<src,<dest, edge>>
    private int modeCounter, edgeCounter;

    //empty constructor
    public DS_DWGraph() {}

    //copy constructor
    public DS_DWGraph(directed_weighted_graph other) {
        //set graph's nodes
        Collection<node_data> oNodes = other.getV();
        if (oNodes != null) {
            for (node_data node : oNodes) {
                addNode(new nodeData(node));
            }
        }

        //TODO check if iterator better then for each
        for (int nodeKey : edges.keySet()) {
            Collection<edge_data> nodeNei = other.getE(nodeKey);
            Iterator<edge_data> iterator = nodeNei.iterator();
            while (iterator.hasNext()) {
                edge_data edge = iterator.next();
                connect(nodeKey, edge.getDest(), edge.getWeight());
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
    public edge_data getEdge(int src, int dest) {

        return edges.get(src).get(dest);
    }

    @Override
    public void addNode(node_data node) {
        //TODO check if edges.set is safe (what if key bigger then nodes
        //TODO check edges.set if node exist
        if(node!=null) {
            nodes.put(node.getKey(), node);
            edges.put(node.getKey(), new HashMap<>());

            modeCounter++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        //TODO split to 2 if for time save
        if( (nodes.get(src)!=null) && (nodes.get(dest)!=null) && (src!=dest) && (w>0) ){

            //if edge not already exist
            if (getEdge (src, dest) == null) {
                //add new edge to edges
                edge_data newEdge = new edge(src, dest, w);
                this.edges.get(src).put(dest, newEdge);
                //manage counters
                edgeCounter++;
                modeCounter++;
            }
            //if edge exist but w changes
            else if (getEdge (src, dest).getWeight() != w){
                edge_data newEdge = new edge(src, dest, w);
                this.edges.get(src).put(dest, newEdge);
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
        return edges.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        //TODO check if null check needed

        HashMap<Integer, edge_data> neigh = edges.remove(key);
        node_data removedNode = nodes.remove(key);

        edgeCounter -= neigh.size();
        modeCounter++;

        return removedNode;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {

        if(nodes.get(src)!=null){
            edge_data removedEdge = edges.get(src).remove(dest);

            if(removedEdge!=null) { //if there is an edge between src and dest
                modeCounter++;
                edgeCounter--;

                return removedEdge;
            }
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
        public edge() {}

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

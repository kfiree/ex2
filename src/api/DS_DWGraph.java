package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class  DS_DWGraph implements  directed_weighted_graph{
    //TODO add equals overRide for tests

    private HashMap<Integer, node_data> nodes = new HashMap<>();
    //TODO check for better mapping
    //arraylist<hashmap<dest, edge>>, index = src
    private HashMap<Integer, HashMap<Integer, edge_data>> edges = new HashMap<>();
//    private HashMap<Integer, edge_data> edges = new HashMap<>();
    private int modeCounter, edgeCounter;

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
        if(nodes.containsKey(src) && nodes.containsKey(dest)) {
            edge_data newEdge = new edge(src, dest, w);
            this.edges.get(src).put(dest, newEdge);

            edgeCounter++;
            modeCounter++;
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
        if(nodes.containsKey(src)) {
            edge_data removedEdge = edges.get(src).remove(dest);
            //if there is an edge between src and dest
            if(removedEdge!=null) {
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

        public edge(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight=weight;
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

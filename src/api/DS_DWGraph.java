package api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class  DS_DWGraph implements  directed_weighted_graph{
    private HashMap<Integer, node_data> nodes = new HashMap<>();
    //TODO check for better mapping
    private ArrayList<HashMap<Integer, edge_data>> edges = new ArrayList<>();
//    private HashMap<Integer, edge_data> edges = new HashMap<>();
    private int modeCounter, edgeCounter;

    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        return edges.get(src);
    }

    @Override
    public void addNode(node_data node) {
        if(node!=null)
            nodes.put(node.getKey(), node);
    }

    @Override
    public void connect(int src, int dest, double w) {
        edge_data newEdge = new edge(src, dest, w);
        this.edges.put(src, newEdge);
    }

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        return null;
    }

    @Override
    public node_data removeNode(int key) {
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return 0;
    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int getMC() {
        return 0;
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

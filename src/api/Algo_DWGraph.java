package api;

import java.util.List;

public class Algo_DWGraph implements dw_graph_algorithms{

    private directed_weighted_graph graph = new DS_DWGraph();

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

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}

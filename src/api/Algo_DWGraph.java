package api;

import java.util.*;

public class Algo_DWGraph implements dw_graph_algorithms {

    private directed_weighted_graph graph = new DS_DWGraph();

    @Override
    public void init(directed_weighted_graph g) {
        if (g == null)
            this.graph = new DS_DWGraph();
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

        if (this.graph.nodeSize() < 2) {
            return true;
        }
        if (!BFS()) {
            return false;
        }
        return OppositeBFS((DS_DWGraph)this.graph);
    }

    private boolean BFS() {
        Queue<node_data> queue = new LinkedList<>();
        int visitedCounter = 0;

        resetTagAndWeight(this.graph);

        Collection<node_data> G = this.graph.getV();
        Iterator<node_data> itr = G.iterator();
        if (itr.hasNext()) {
            node_data src = itr.next();
            queue.add(src);
            visitedCounter++;
            src.setTag(1);

            while (!queue.isEmpty()) {
                Collection<edge_data> NA = graph.getE(src.getKey());
                if (NA != null) {
                    for (edge_data edge : NA) {
                        node_data dst = graph.getNode(edge.getDest());
                        if (dst.getTag() == -1) {
                            dst.setTag(1);
                            queue.add(dst);
                            visitedCounter++;
                        }
                    }
                    queue.poll();
                }
                src = queue.peek();
            }
        }
        return visitedCounter==graph.nodeSize();
    }


    private boolean OppositeBFS(DS_DWGraph g) {
        Queue<node_data> queue = new LinkedList<>();
        int visitedCounter = 0;
        resetTagAndWeight(g);

        Collection<node_data> G = g.getV();
        Iterator<node_data> itr = G.iterator();
        if (itr.hasNext()) {
            node_data src = itr.next();
            queue.add(src);
            visitedCounter++;
            src.setTag(1);

            while (!queue.isEmpty()) {
                Collection<edge_data> NA = g.getOppositE(src.getKey());
                if (NA != null) {
                    for (edge_data edge : NA) {
                        node_data dst = g.getNode(edge.getDest());
                        if (dst.getTag() == -1) {
                            dst.setTag(1);
                            queue.add(dst);
                            visitedCounter++;
                        }
                    }
                    queue.poll();
                }
                src = queue.peek();
            }
        }
        return visitedCounter==g.nodeSize();
    }



    @Override
    public double shortestPathDist(int src, int dest) {

        node_data start = this.graph.getNode(src);
        node_data end = this.graph.getNode(dest);

        if (end == null || start == null){
            return -1;
        }
        if (src == dest ){
            return 0;
        }
        dijkstra(src, dest);

        if (end.getWeight() != 0) {
            return end.getWeight();
        }
        return -1;

    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {

        node_data start = this.graph.getNode(src);
        node_data end = this.graph.getNode(dest);

        if (end == null || start == null){
            return null;
        }
        LinkedList <node_data> list = new LinkedList<>();

        if (src == dest){
            list.add(start);
            return list;
        }

        dijkstra(src, dest);

        if (end.getWeight() != 0) {
            while (end.getTag() != end.getKey() ){
                list.addFirst(end);
                end = this.graph.getNode(end.getTag());
            }
            return list;
        }
        return null;
    }


    /**
     * update node's data (weight and tag) using dijkstra's algorithm
     * node's Weight = distance between node to source node
     * node's tag = key of previous node in the shortest path to destination
     *
     * @param src  start node
     * @param dest end (target) node
     */

    private void dijkstra (int src, int dest) {

        node_data start = graph.getNode(src);
        node_data end = graph.getNode(dest);

        resetTagAndWeight(this.graph);

        if ((start != null) && (end != null)) {

            PriorityQueue<node_data> pQueue = new PriorityQueue<node_data>();
            node_data nodeCurr;

            pQueue.add(start);
            start.setTag(src);

            HashMap<edge_data, Integer> destEdges = new HashMap<>();
            Collection<edge_data> E = graph.getE(dest);
            for (edge_data e : E) {
                destEdges.put(e, dest);
            }

            while ((!pQueue.isEmpty() && (!destEdges.isEmpty()))) {

                nodeCurr = pQueue.poll();

                Collection<edge_data> Edges = this.graph.getE(nodeCurr.getKey());
                if (Edges != null) {
                    for (edge_data edge : Edges) {
                        node_data dst = this.graph.getNode(edge.getDest());
                        if (nodeCurr.getTag() != dst.getKey()) {
                            if (dst.getTag() == -1) {
                                dst.setTag(nodeCurr.getKey());
                                dst.setWeight(nodeCurr.getWeight() + edge.getWeight());
                                pQueue.add(dst);
                            }
                            else if (dst.getWeight() > nodeCurr.getWeight()+edge.getWeight()){
                                dst.setTag(nodeCurr.getKey());
                                dst.setWeight(nodeCurr.getWeight()+edge.getWeight());
                            }
                        }
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

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }



}

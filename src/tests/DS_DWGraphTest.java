package tests;
import api.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class DS_DWGraphTest {
    //this function based on Graph_Ex0_Test2 graph_creator function
    public static directed_weighted_graph makeGraph(int v_size, int e_size, int seed) {

        Random rand = new Random();
        directed_weighted_graph g = new DS_DWGraph();
        rand = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            node_data node = new nodeData();
            g.addNode(node);
        }

        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = rand.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }



    //this functions based on Graph_Ex0_Test2 nextRnd function to make rand num in range
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        Random rand = new Random();
        double d = rand.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    //this function based on Graph_Ex0_Test2 nodes function
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
    directed_weighted_graph g = makeGraph(2,1,1) ;
    Collection<node_data> N = g.getV();
    Iterator <node_data> itr = N.iterator();

    directed_weighted_graph fullg = makeGraph(5,20,1) ;
    directed_weighted_graph emptyg = new DS_DWGraph();

    /**
     * simple test  getNode from graph
     * checks:
     * get un exist node
     * get node from empty graph
     */
    @Test
    void getNode() {

        if(itr.hasNext()) {
            node_data s = itr.next();
            int[] test = {-3, s.getKey(), -1};

            assertAll(
                    () -> assertNull(g.getNode(test[0]), "get node un exist"),
                    () -> assertNotNull(g.getNode(test[1]), "get real node"),
                    () -> assertNull(g.getNode(test[2]), "get negative node "),
                    () -> assertNull(emptyg.getNode(0), "get node from empty graph")
            );
        }
    }
    /**
     * simple test for getEdge between nodes in graph
     * checks:
     * edge from node to himself
     * edge from empty graph
     * edge from one of node not exist
     */
    @Test
    void getEdge() {
        if (itr.hasNext()) {
            node_data s = itr.next();
            edge_data e = g.getEdge(s.getKey(),s.getKey()+1);

            assertAll(
                    () -> assertNull(g.getEdge(0, -1), "one of nodes not in graph"),
                    () -> assertNull(emptyg.getEdge(0, 0), "empty graph"),
                    () -> assertNull(g.getEdge(s.getKey(), s.getKey()), "itself")
            );
            if (e == null) {
                assertNotNull(g.getEdge(s.getKey()+1, s.getKey()));
            } else
                assertNotNull(g.getEdge(s.getKey(), s.getKey()+1));
        }
    }

    @Test
    void addNode() {
        if (itr.hasNext()) {
            node_data s = itr.next();
            int size = g.nodeSize();
            node_data n = g.getNode(s.getKey());
            assertAll(
                    () -> g.addNode(n),
                    () -> assertEquals(size, g.nodeSize(), "add already exist"),
                    () -> g.addNode(new nodeData()),
                    () -> assertNotEquals(size, g.nodeSize())
            );

        }
    }

    @Test
    void connect() {
        int size = g.edgeSize();
        if (itr.hasNext()) {
            node_data s = itr.next();
            assertAll(
                    () -> g.connect(s.getKey(), s.getKey()+1, -1),
                    () -> assertEquals(size, g.edgeSize(), "connect with negativ w"),
                    () -> g.connect(s.getKey(), s.getKey()+g.nodeSize(), 1),
                    () -> assertEquals(size, g.edgeSize(), "connect with node not exist"),
                    () -> g.connect(s.getKey(), s.getKey()+1, 1),
                    () -> assertEquals(1, g.getEdge(s.getKey(), s.getKey()+1).getWeight()),
                    () -> g.connect(s.getKey(), s.getKey()+1, 0),
                    () -> assertEquals(0, g.getEdge(s.getKey(), s.getKey()+1).getWeight(), "nodes connected but w change")
            );

            int size2 = g.edgeSize();
            int mc = g.getMC();

            assertAll(
                    () -> g.connect(s.getKey(), s.getKey(), 7),
                    () -> assertEquals(size2, g.edgeSize(), "connect node with itself"),
                    () -> g.connect(s.getKey(), s.getKey()+1, 1),
                    () -> assertNotEquals(mc, g.getMC()),
                    () -> emptyg.connect(1, 2, 1),
                    () -> assertEquals(0, emptyg.edgeSize(), "empty graph")

            );
        }
    }

    @Test
    void getV() {
        directed_weighted_graph g1 = makeGraph(5,0,1) ;
        directed_weighted_graph n = new DS_DWGraph();

        Collection <node_data> G = g1.getV();
        Collection <node_data> N = n.getV();

        assertEquals(G.size(),g1.nodeSize());
        assertTrue(N.isEmpty());

    }
    @Test
    void getE() {
        DS_DWGraph gg = (DS_DWGraph) fullg;
        Collection <node_data> G = fullg.getV();
        node_data[] nodes = new node_data[G.size()];
        G.toArray(nodes); // O(n) operation

        for (int i = nodes[0].getKey(); i<nodes[0].getKey()+ G.size(); i++){
            assertEquals(4, fullg.getE(i).size());
        }
        int j = nodes[0].getKey()+ G.size();
        assertNull(fullg.getE(j));

         assertNull(emptyg.getE(0));

    }

    @Test
    void removeNode() {

        Collection <node_data> G = fullg.getV();
        node_data[] nodes = new node_data[G.size()];
        G.toArray(nodes); // O(n) operation
        int i = nodes[0].getKey();

        int size = fullg.nodeSize();
        int edgSize = fullg.edgeSize();
        directed_weighted_graph n = new DS_DWGraph();
        node_data nn = fullg.removeNode(100);
        assertAll(
                ()-> assertNull(nn),
                ()-> assertEquals(size, fullg.nodeSize(), "remove node not exist"),
                ()-> fullg.removeNode(i),
                ()-> assertNotEquals(size, fullg.nodeSize()),
                ()-> assertEquals(edgSize-8, fullg.edgeSize()),
                ()-> assertNull(n.removeNode(0),"empty graph")

        );
    }

    @Test
    void removeEdge() {
        Collection <node_data> G = fullg.getV();
        node_data[] nodes = new node_data[G.size()];
        G.toArray(nodes); // O(n) operation
        int i = nodes[0].getKey();
        int s = fullg.getE(i).size();
        int edgSize = fullg.edgeSize();
       assertAll(
                ()-> fullg.removeEdge(i-1,i),
                ()-> assertEquals(edgSize, fullg.edgeSize(), "remove edge from node not exist"),
                ()-> fullg.removeEdge(i,i+1),
                ()-> assertNotEquals(edgSize, fullg.edgeSize()),
                ()-> assertEquals(s-1, fullg.getE(i).size()),
                ()-> assertEquals(edgSize-1, fullg.edgeSize()),
                ()-> emptyg.removeEdge(0,1),
                ()-> assertEquals(0, emptyg.getMC())

        );
    }


    public static void toString(directed_weighted_graph g){
        String neiString;
        int nodeSize= g.nodeSize();
        for (node_data node : g.getV()) {
            neiString = "[";
            for(edge_data ni : g.getE(node.getKey())) {
                neiString +=  ni.getDest() +"," ;
            }

            System.out.println("node := "+ node.getKey() + ", Nei := " +  neiString + "]");

        }
        System.out.println("###############################################################");
    }
}
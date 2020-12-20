package tests;

import api.*;
import api.directed_weighted_graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Algo_DWGraphTest {

    /**
     * This is a simple test class to test WGraph_Algo
     * the test checks functionality & performance
     */
    dw_graph_algorithms ga = new Algo_DWGraph();
    directed_weighted_graph g = DS_DWGraphTest.makeGraph(5, 20, 1);
    Collection<node_data> N = g.getV();
    Iterator<node_data> itr = N.iterator();
    directed_weighted_graph g1 = new DS_DWGraph();
    directed_weighted_graph emptyg = new DS_DWGraph();

    @BeforeEach
    void start() {
        ga = new Algo_DWGraph();
        g = DS_DWGraphTest.makeGraph(5, 20, 1);
        N = g.getV();
        itr = N.iterator();
        g1 = new DS_DWGraph();
        emptyg = new DS_DWGraph();
    }
    /**
     * test for init
     * checks:
     * get by init the graph
     * use shallow copy
     */
    @Test
    void init() {
        ga.init(g);
        assertTrue(ga.isConnected());
        if (itr.hasNext()) {
            node_data n = itr.next();
            int i = n.getKey();
            while (itr.hasNext()) {
                g.removeEdge(n.getKey(), itr.next().getKey());
            }
            boolean ch = ga.isConnected();
            assertFalse(ga.isConnected());
        }

    }
    /**
     * test for getGraph
     * check:
     * the graph init and the graph getGraph are equals
     */
    @Test
    void getGraph() {
        ga.init(g);
        g1 = ga.getGraph();
        assertEquals(g, g1);
    }

    /**
     * test for copy
     * checks:
     * copy two graphs by deep copy
     * copy empty graphs
     */
    @Test
    void copy() {
        ga.init(g);
        g1 = ga.copy();
        assertEquals(g, g1);
        if (itr.hasNext()) {
            node_data n = itr.next();
            g.removeNode(n.getKey());
            assertNotEquals(g, g1, "remove node should not affect deep copy");

            ga.init(emptyg);
            g1 = ga.copy();
            assertEquals(g1, emptyg, "copy empty graph- empty is equal");
            emptyg.addNode(n);
            assertNotEquals(g1, emptyg, "add node should not affect deep copy");

        }
    }

    /**
     * test for isConnected
     * checks:
     * empty graph - is connected
     * graph with only one node - is connected
     * Complete graph - is connected
     * insert new node to  Complete graph - not connected
     */
    @Test
    void isConnected() {
        ga.init(g);
        assertTrue(ga.isConnected(), "Complete graph");

        g.addNode(new nodeData());
        assertFalse(ga.isConnected(), "insert node- not Complete graph");

        ga.init(emptyg);
        assertTrue(ga.isConnected(), "empty graph");
        emptyg.addNode(new nodeData());
        assertTrue(ga.isConnected(), "graph with one node");
        emptyg.addNode(new nodeData());
        assertFalse(ga.isConnected(), "two nodes not connect");
        g = ga.copy();
        for (node_data n : g.getV()) {
            g.connect(n.getKey(), n.getKey() + 1, 1);
            g.connect(n.getKey() + 1, n.getKey(), 1);
        }
        assertFalse(ga.isConnected(), "deep copy- two nodes not connect");
        ga.init(g);
        assertTrue(ga.isConnected(), "init- after connect nodes");

    }
    /**
     * test for shortestPathDist
     * checks:
     * empty graph
     * path with node not exist
     * shortestPathDist change if w changes
     */
    @Test
    void shortestPathDist() {
        ga.init(g1);
        assertEquals(-1, ga.shortestPathDist(1, 5), "empty graph");

        for (int i = 0; i < 11; i++) {
            g1.addNode(new nodeData());
        }
        Collection<node_data> G1 = g1.getV();
        node_data [] NG1 = new node_data[g1.nodeSize()];
        G1.toArray(NG1);
        int[] ans = new int[g1.nodeSize()];
        for(int i=0;i<g1.nodeSize();i++) {ans[i] = NG1[i].getKey();}
        Arrays.sort(ans);


            for (int i = ans[0]; i < ans[0]+6; i++) {
                g1.connect(i, i + 1, 1);
            }
            assertEquals(-1, ga.shortestPathDist(ans[0], ans[0]+10), "node not exist");
            assertEquals(-1, ga.shortestPathDist(ans[0], ans[0]+7), "no path");
            assertEquals(5, ga.shortestPathDist(ans[0], ans[0]+5));
            g1.connect(ans[0]+5, ans[0]+9, 95);
            assertEquals(100, ga.shortestPathDist(ans[0], ans[0]+9));
            g1.connect(ans[0]+5, ans[0]+8, 5);
            assertEquals(10, ga.shortestPathDist(ans[0], ans[0]+8));

        g1.connect(ans[0],ans[0]+1,1);
        g1.connect(ans[0],ans[0]+2,2);
        g1.connect(ans[0],ans[0]+3,3);

        g1.connect(ans[0]+1,ans[0]+4,17);
        g1.connect(ans[0]+1,ans[0]+5,1);
        g1.connect(ans[0]+2,ans[0]+4,1);
        g1.connect(ans[0]+3, ans[0]+5,10);
        g1.connect(ans[0]+3,ans[0]+6,100);
        g1.connect(ans[0]+5,ans[0]+7,1.1);
        g1.connect(ans[0]+6,ans[0]+7,10);
        g1.connect(ans[0]+7,ans[0]+10,2);
        g1.connect(ans[0]+6,ans[0]+8,30);
        g1.connect(ans[0]+8,ans[0]+10,10);
        g1.connect(ans[0]+4,ans[0]+10,30);
        g1.connect(ans[0]+3,ans[0]+9,10);
        g1.connect(ans[0]+8,ans[0]+10,10);

        double d = ga.shortestPathDist(ans[0],ans[0]+10);
        assertEquals(d, 5.1);

    }

    /**
     * test for shortestPath
     * checks:
     * empty graph
     * path with node not exist
     * the list nodes have same refer with graph nodes
     * the last node.tag in list = path
     */
    @Test
    void shortestPath() {

        ga.init(g1);

        for (int i = 0; i < 11; i++) {
            g1.addNode(new nodeData());
        }
        Collection<node_data> G1 = g1.getV();
        node_data [] NG1 = new node_data[g1.nodeSize()];
        G1.toArray(NG1);
        int[] ans = new int[g1.nodeSize()];
        for(int i=0;i<g1.nodeSize();i++) {ans[i] = NG1[i].getKey();}
        Arrays.sort(ans);

        for (int i = ans[0]; i < ans[0]+6; i++) {
            g1.connect(i, i + 1, 1);
        }
        List<node_data> l = new LinkedList<>();
        l = ga.shortestPath(ans[0],ans[0]+5);
        int[] checkKey = {ans[0], ans[0]+1, ans[0]+2, ans[0]+3, ans[0]+4,ans[0]+5};
        int i = 0;
        for (node_data n : l){
            assertEquals(checkKey[i],n.getKey());
            i++;
        }
    }

    /**
     * test for save and load
     * checks:
     * save and load empty graph
     * the load not change when init the graph
     */
    @Test
    void saveAndLoad() {
        ga.init(g);
        ga.save("gTest");
        ga.init(emptyg);
        ga.load("gTest");
        g1= ga.getGraph();
        assertEquals(g,g1);

    }
}
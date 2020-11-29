package tests;


import api.DS_DWGraph;
import api.directed_weighted_graph;
import api.nodeData;
import api.node_data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  tester for DS_DWGraph
 *
 *  @author kfir ettinger
 */
class graphTester {

    private static int wUpdateCounter;

    private static Random numGenerator;


    /**
     *
     * test getNode and addNode
     * ex1.tests:
     *  - add new node to graph
     *
     * @result code will be persisted without any errors and will return null if key wasnt found
     */
    @Test
    void getAndAddTest() {

        directed_weighted_graph g = new DS_DWGraph();

        //check adding new node to graph
        node_data node = new nodeData();
        g.addNode(node);
        assertEquals(node, g.getNode(node.getKey()));

    }

    /**
     * public method thats creates a graph for ex1.tests
     * with a given number of node and number of edges and pre-difined range of keys and edge's weights
     *
     * @param nSize number of nodes of new graph
     * @param eSize number of edges in new graph
     * @param seed seed for pseudo-random numbers
     * @param wRange range of weights for edges (only for shortestPath)
     * @return a graph
     */
    public static DS_DWGraph createGraph(int nSize, int eSize, int seed, int wRange){
        wUpdateCounter=0;
        numGenerator = new Random(seed);
        int max = nSize*2;
        int min = 0;
        if(wRange!=-1) {
            min=wRange;
        }
        DS_DWGraph graph = new DS_DWGraph();
        List<Integer> keyList = new ArrayList<>();
        //generate vertexes
        while(graph.nodeSize()<nSize) {
            node_data node = new nodeData();
            graph.addNode(node);
            keyList.add(node.getKey());
        }

        //generate edges
        int key1 = 0, key2;
        Double w, Dmax = max+0.0, Dmin=min+0.0;

        while(graph.edgeSize() < eSize) {
            key1 = keyList.get(numGenerator.nextInt(keyList.size()));
            key2 = keyList.get(numGenerator.nextInt(keyList.size()));

            w = (numGenerator.nextInt(max - min) + min) + (numGenerator.nextDouble());

            graph.connect(key1, key2, w);
        }
        return graph;
    }
}
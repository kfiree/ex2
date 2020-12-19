## `Welcome to OOP_Ex2 `
##### **made by:** `Kfir Etinfer & Tehila Ben Kalifa`

**about:**
- *The first part of this project implements unidirectional weighted graph.*    
*for more reading- https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)*

- *The second part of this project is the **"pokemons game"**-*
*the game based on unidirectional weighted graph and the pokemons scatterd on the graph edges randomly.*
*the pokemons have different value. the main goal is to catch most pokemons whith high value during the allotted time.*
*The player can choose levels from 0-23 with rising difficulty.*


**methods:**
*The graph supports the below mentioned methods:*
* `getNode`- return the node_data by the node_id **_`O(1)`_**
* `hasEdge`- return true iff (if and only if) there is an edge between node1 and node2 **_`O(1)`_**
* `addNode`- add a new node to the graph with the given key **_`O(1)`_**
* `connect`- Connect an edge between node1 and node2, with an edge with weight >=0 **_`O(1)`_**
* `getV()`- return a pointer (shallow copy) for a  Collection representing all the nodes in the graph **_`O(1)`_**
* `getV(int node_id)`- return a Collection containing all the nodes connected to node_id **_`O(K), K = degree of node_id`_**
* `removeNode`- Delete the node (with the given ID) from the graph and removes all edges which starts or ends at this node. **_`O(n), |V|=n`_**
* `removeEdge`- Delete the edge from the graph **_`O(1)`_**
* `nodeSize`- return the number of vertices (nodes) in the graph **_`O(1)`_**
* `edgeSize`- return the number of edges (undirectional graph) **_`O(1)`_**
* `getMC`- return the Mode Count - for testing changes in the graph  **_`O(1)`_**

> The graph implementation relies on using Hash Map - which supports Delete, Get, Insert and Collection of the values in the Hash - all in O(1).






**algorithms methods:**
*Weighted Graph Theory algorithms including:*
* `init`- Init the graph on which this set of algorithms operates on
* `getGraph`-  Return the underlying graph of which this class works
* `copy`- Compute a deep copy of this weighted graph
* `isConnected`- Returns true if and only if there is a valid path from EVREY node to each other
* `shortestPathDist`- returns the length of the shortest path between src to dest 
* `shortestPath`- returns the the shortest path between src to dest - as an ordered List of nodes
* `save`- Saves the graph to the given file name
* `load`- load a graph by given file name

>###### The implementation relies on using BFS and Dijkstra's algorithms 
>> ###### `BFS algorithm`:
>> Breadth-first search (BFS) is an algorithm for traversing or searching tree or graph data structures. It starts at the tree root ,
>> and explores all of the neighbor nodes at the present depth prior to moving on to the nodes at the next depth level.
>>the implementation relies on using Queue.
>>
>> the time complexity is O(|V|+|E|) while V is the nodes and E is the edges in the graph
>>
>> for more reading- https://en.wikipedia.org/wiki/Breadth-first_search
>
>



>> ##### `Dijkstra's algorithm`:
>> Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph
>>
>> the implementation relies on using Priority Queue
>>
>> the time complexity is O((|E|+|V|)log |V|)
>>
>> for more reading- https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

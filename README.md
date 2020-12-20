
## `Welcome to OOP_Ex2 `
##### **made by:** `Kfir Etinger & Tehila Ben Kalifa`


### **About:**

- *The first part of this project implements directional weighted graph.*    
*for more reading- https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)*

- *The second part of this project implements the **"pokemons game"**-*
*the game based on unidirectional weighted graph and the pokemons scatterd on the graph edges randomly.*
*the pokemons have different value. the main goal is to catch most pokemons with high value during the allotted time.*
*The player can choose levels from 24 different game levels rising difficulty.*

  
### **Using:**

- *git clone this repository to a suitable folder*

    ```
    $ git clone https://github.com/kfiree/ex2.git
    ```

- *run the Ex2.jar*

    ```
    java -jar Ex2
    ```
- *enter your ID and number of level from 0-23 you want to play*

     ```
     java -jar Ex2.jar <YOUR ID> <LEVEL>
     ```
  
  #### `Or:` 
   *in the main window of the game- `Enter your ID and Select level`, after fill the details click the `Play`* 
  

### **Structure:**
### *[api packege](https://github.com/kfiree/ex2/wiki/Api-Structure)*
>
> - `nodeData`- represents the set of operations applicable on a node (vertex) in  directional weighted graph.
>
> - `geoLocation`- represents a geo location <x,y,z>, on graph
>
> - `edgeData`- represents the set of operations applicable on a directional edge(src,dest) in directional weighted graph.
>
> - `DS_DWGraph`- represents a directional weighted graph. 
>
 >The graph implementation relies on using Hash Map - which supports Delete, Get, Insert and Collection of the values in the Hash - all in O(1).
>
> - `Algo_DWGraph`-  represents a directed  Weighted Graph Theory Algorithms
>
 > The implementation relies on using BFS, Kosaraju and Dijkstra's algorithms

 

### *[gameClient packege](https://github.com/kfiree/ex2/wiki/gameClient-Structer)*
>
> - `CL_Pokemon`- represents the set of operations applicable on a pokemons in the game
>
> - `CL_Agent` - represents the set of operations applicable on a agent that grabs pokemons in the game
>
> - `Arena`- represents the Arena of the game
>
> - `gamePanel`-
>
> - `window`-
>
> - `Ex2`- the main class of running the game





### *Read more about the project in our [Wiki](https://github.com/kfiree/ex2/wiki)*. 




## `Welcome to OOP_Ex2 `
##### **made by:** `Kfir Etinger & Tehila Ben Kalifa`


### **About:**

- *The first part of this project implements directional weighted graph.*    
*for more reading- https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)*


![](https://github.com/kfiree/ex2/blob/main/src/wikiPhotos/readmeGraph.PNG)



- *The second part of this project implements the **"pokemons game"**-*
*the game is based on an undirected weighted graph.*
*In the game, the pokemons scatter on the graph edges randomly.*
*Each pokemon has a different value, and the main goal is to catch as many pokemons as possible, while taking their value into consideration, during the allotted time.* 
*The player can choose to play the game in 24 different levels of rising difficulty..*


![](https://github.com/kfiree/ex2/blob/main/src/wikiPhotos/pokemon.PNG)

  
### **Using:**

- *git clone this repository to a suitable folder*

    ```
    $ git clone https://github.com/kfiree/ex2.git
    ```

- *run the Ex2.jar, enter your ID and number of level from 0-23 you want to play*
- *the jar need to be at same folder as data folder*
     ```
    $ java -jar Ex2.jar <YOUR ID> <LEVEL>
     ```
  
  #### `Or:` 
  - *run the Ex2.jar*
  - *the jar need to be at same folder as data folder*
    ```
    $ java -jar Ex2.jar
    ```
   *in the main window of the game- `Enter your ID and Select level`, after fill the details click the `Login`* 
  

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
> - `gamePanel`- this panel contains all game's graphics
>
> - `welcomePanel`- this panel  contains the entry graphics
> 
> - `window`- a frame that contains gamePanel and welcomePanel
>
> - `Ex2`- the main class of running the game





### *Read more about the project in our [Wiki](https://github.com/kfiree/ex2/wiki)*. 



package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class represents the set of operations applicable on a agent that grabs pokemons in the game
 */
public class CL_Agent {
	//static vars
	public static final double EPS = 0.0001;
	private static int _count = 0;
	private static int _seed = 3331;

	//agents properties
	private int _id;
	private geo_location _pos;
	private double _speed;
	private edge_data _curr_edge;
	private node_data _curr_node;
	private directed_weighted_graph g;
	private CL_Pokemon _curr_fruit;
	private long _sg_dt;
	private double huntVal;
	List<node_data> path = new ArrayList<>();
	private double _val;
	PriorityQueue<PokemonEntry> pokemonQueue = new PriorityQueue<>();
	PokemonEntry nextTarget;
	edge_data targetEdge;

	public CL_Agent(directed_weighted_graph graph, int start_node) {
		g = graph;
		setMoney(0);
		this._curr_node = g.getNode(start_node);
		_pos = _curr_node.getLocation();
		_id = -1;
		setSpeed(0);
	}
	public void update(String json) {
		JSONObject line;
		try {
			// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
			line = new JSONObject(json);
			JSONObject ttt = line.getJSONObject("Agent");
			int id = ttt.getInt("id");
			if(id==this.getID() || this.getID() == -1) {
				if(this.getID() == -1) {_id = id;}
				double speed = ttt.getDouble("speed");
				String p = ttt.getString("pos");
				Point3D pp = new Point3D(p);
				int src = ttt.getInt("src");
				int dest = ttt.getInt("dest");
				double val = ttt.getDouble("value");
				this._pos = pp;
				this.setCurrNode(src);
				this.setSpeed(speed);
				this.setNextNode(dest);
				this.setMoney(val);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	//@Override
	public int getSrcNode() {return this._curr_node.getKey();}
	public String toJSON() {
		int d = this.getNextNode();
		String ans = "{\"Agent\":{"
				+ "\"id\":"+this._id+","
				+ "\"value\":"+this._val+","
				+ "\"src\":"+this._curr_node.getKey()+","
				+ "\"dest\":"+d+","
				+ "\"speed\":"+this.getSpeed()+","
				+ "\"pos\":\""+_pos.toString()+"\""
				+ "}"
				+ "}";
		return ans;
	}
	private void setMoney(double v) {_val = v;}

	public boolean setNextNode(int dest) {
		boolean ans = false;
		int src = this._curr_node.getKey();
		this._curr_edge = g.getEdge(src, dest);
		if(_curr_edge!=null) {
			ans=true;
		}
		else {_curr_edge = null;}
		return ans;
	}
	public void setCurrNode(int src) {
		this._curr_node = g.getNode(src);
	}
	public boolean isMoving() {
		return this._curr_edge!=null;
	}
	public String toString() {
		return toJSON();
	}
	public String toString1() {
		String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();
		return ans;
	}
	public int getID() {
		// TODO Auto-generated method stub
		return this._id;
	}

	public geo_location getLocation() {
		// TODO Auto-generated method stub
		return _pos;
	}


	public double getValue() {
		// TODO Auto-generated method stub
		return this._val;
	}

	//TODO might use synchronized

//	/**
//	 *
//	 * @param pokemons
//	 * @param ga
//	 * @return -1 if there is no pokemon available for agent
//	 */
//	public List<CL_Pokemon> calculateClosestPokemon(List<CL_Pokemon> pokemons, dw_graph_algorithms ga) {
////		int closestPokemon = -1;
////		double closestPokemonDist = -1, bestValue = -1, bestValueDist = -1;
////		for(CL_Pokemon pokemon: pokemons){
////
////			edge_data edge = pokemon.get_edge();
////			//TODO check if needed
////			//		maybe check dest to dest instead
////
////			double distToPokemon = ga.shortestPathDist(this._curr_node.getKey(), pokemon.get_edge().getDest());
////			double pokemonRealValue = pokemon.getValue()/distToPokemon;
////
////
////			if(pokemon.persecutedBy!=-1){
////				int currAgent = pokemon.persecutedBy;
////
////			}
////			|| pokemon.getTag()>pokemonRealValue
//		}
//
//
////			if(!pokemon.isPersecuted()){
////				if(pokemon.getValue()>bestValue){
////					bestValue = pokemon.getValue();
////				} else if(pokemon.getValue()==bestValue) {
////					if(bestValueDist>distToPokemon){
////						bestValue = pokemon.getValue();
////						bestValueDist = distToPokemon;
////					}
////				}else {
////					double distSoFar = ga.shortestPathDist(this._curr_node.getKey(), edge.getSrc())+edge.getWeight();
////					double nextStepsDist = distSoFar;
////					if(calculateNextStep(pokemons, ga, edge.getDest(), bestValueDist, bestValue, distSoFar, pokemon.getValue())){
////						//TODO make agent take this path
////					}
////
////
////				}
////
////				if( distToPokemon < closestPokemonDist || closestPokemonDist ==-1){
////					closestPokemon = edge.getSrc();
////					closestPokemonDist = distToPokemon;
////				}
////			}else{
////
////			}
////		}
//		return null;
////		return closestPokemon;
//	}

//	private boolean calculateNextStep(List<CL_Pokemon> pokemons, dw_graph_algorithms ga, int currNodeKey, double bestValueDist, double bestValue, double distSoFar, double valueSoFar){
//		while(bestValueDist>distSoFar){
//			for(CL_Pokemon pokemon: pokemons){
//				edge_data edge = pokemon.get_edge();
//				//TODO check if needed
//				double distFromNodeToP = pokemon.getLocation().distance(ga.getGraph().getNode(edge.getSrc()).getLocation());//how far is pokemon from closest node to it
//				double distToPokemon = ga.shortestPathDist(currNodeKey, edge.getSrc())+distFromNodeToP;
//
//				if(!pokemon.isPersecuted()){
//
//					if((pokemon.getValue()+valueSoFar>bestValue && bestValueDist>=distToPokemon+distSoFar)||
//							(pokemon.getValue()+valueSoFar==bestValue && bestValueDist>distToPokemon+distSoFar)){
//						return true;
//					} else if(pokemon.getValue()+valueSoFar<bestValue && bestValueDist>distToPokemon+distSoFar){
//						calculateNextStep(pokemons, ga, edge.getSrc(), bestValueDist, bestValue, distToPokemon+distSoFar ,pokemon.getValue()+valueSoFar);
//					}
////					} else if(pokemon.getValue()==bestValue) {
//					if(bestValueDist>distToPokemon){
//						bestValue = pokemon.getValue();
//						bestValueDist = distToPokemon;
//					}
//				}else {
//
//				}
//			}
//
//		}
//		return false;
//	}

//	private double distToPokemon(CL_Pokemon pokemon, node_data dest, geo_location pokemonLocation) {
//		double distFromNodeToP = pokemon.getLocation().distance(src.getLocation());//how far is pokemon from closest node to it
//		double distToPokemon = ga.shortestPathDist(this._curr_node.getKey(), edge.getSrc())+distFromNodeToP;
//	}


	public int getNextNode() {
		int ans = -2;
		if(this._curr_edge==null) {
			ans = -1;}
		else {
			ans = this._curr_edge.getDest();
		}
		return ans;
	}

	public double getSpeed() {
		return this._speed;
	}

	public void setSpeed(double v) {
		this._speed = v;
	}
	public CL_Pokemon get_curr_fruit() {
		return _curr_fruit;
	}
	public void set_curr_fruit(CL_Pokemon curr_fruit) {
		this._curr_fruit = curr_fruit;
	}
	public void set_SDT(long ddtt) {
		long ddt = ddtt;
		if(this._curr_edge!=null) {
			double w = _curr_edge.getWeight();
			geo_location destLocation = g.getNode(_curr_edge.getDest()).getLocation();
			geo_location srcLocation = g.getNode(_curr_edge.getSrc()).getLocation();
			double edgeLength = srcLocation.distance(destLocation);
			double agent2dest = _pos.distance(destLocation);	//dist to end of edge
			if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
				agent2dest = _curr_fruit.getLocation().distance(this._pos);
			}
			double norm = agent2dest/edgeLength;
			double dt = w*norm / this.getSpeed();
			ddt = (long)(1000.0*dt);
		}
		this.set_sg_dt(ddt);
	}


	public edge_data get_curr_edge() {
		return this._curr_edge;
	}
	public long get_sg_dt() {
		return _sg_dt;
	}
	public void set_sg_dt(long _sg_dt) {
		this._sg_dt = _sg_dt;
	}

	public double getHuntValue() {
		return huntVal;
	}

	public void setHuntValue(double huntValue) {
		this.huntVal = huntValue;
	}

	public void set_val(double _val) {
		this._val = _val;
	}

	public List<node_data> getPath() {
		return path;
	}

	public void resetPath() {
		path.clear();
		this.huntVal = 0;
	}

	public void setPath(List<node_data> path) {
		this.path = path;
	}

	public PriorityQueue<PokemonEntry> getPokemonQueue() {
		return pokemonQueue;
	}

	public void setPokemonQueue(PriorityQueue<PokemonEntry> pokemonQueue) {
		this.pokemonQueue = pokemonQueue;
	}

	public void addToQueue(CL_Pokemon p, double pValue) {
		pokemonQueue.add(new PokemonEntry(pValue, p));
	}

	public PokemonEntry pollTarget(){
		return pokemonQueue.poll();
	}


}

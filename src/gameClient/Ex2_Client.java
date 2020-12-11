package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Ex2_Client implements Runnable{
	private static window _win;
	private static Arena _ar;
	public static void main(String[] a) {
		Thread client = new Thread(new Ex2_Client());
		client.start();
	}

	@Override
	public void run() {
		int scenario_num = 11;
		game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games

//		String g = game.getGraph();
//		String pokemons = game.getPokemons();
		directed_weighted_graph g = game.getJava_Graph_Not_to_be_used();
		init(game);

		game.startGame();
		_win.setTitle("Kfir&Tehila's Arena");
//		ImageIcon icon = new ImageIcon("jangoIcon");
//		_win.setIconImage(icon.getImage());
		int ind=0;
		long dt=100;

		while(game.isRunning()) {
//			icon = new ImageIcon("jangoIcon");
//			_win.setIconImage(icon.getImage());
			moveAgents(game, g);
			try {
				if(ind%1==0) {_win.repaint();}
				Thread.sleep(dt);
				ind++;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		String res = game.toString();

		System.out.println(res);
		System.exit(0);
	}
	/**
	 * Moves each of the agents along the edge,
	 * in case the agent is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param g
	 * @param
	 */
	private static void moveAgents(game_service game, directed_weighted_graph g) {
		String lg = game.move();
		List<CL_Agent> agentList = Arena.getAgents(lg, g);
		_ar.setAgents(agentList);
		//ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
		String fs =  game.getPokemons();
		List<CL_Pokemon> pokemons = Arena.json2Pokemons(fs);
		_ar.setPokemons(pokemons);
		for(int i=0;i<agentList.size();i++) {
			CL_Agent agent = agentList.get(i);
			int id = agent.getID();
			int dest = agent.getNextNode();
			int src = agent.getSrcNode();
			double v = agent.getValue();
			if(dest==-1) {
				dest = nextNode(g, src);
//				dw_graph_algorithms ga = new Algo_DWGraph();
//				ga.init(_ar.getGraph());
//				dest = agent.calculateClosestPokemon(pokemons, ga);
				game.chooseNextEdge(agent.getID(), dest);
				System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
			}
		}
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(directed_weighted_graph g, int src) {
		int ans = -1;
		Collection<edge_data> ee = g.getE(src);
		Iterator<edge_data> itr = ee.iterator();
		int s = ee.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	private void init(game_service game) {
//		String g = game.getGraph();
		String fs = game.getPokemons();
		directed_weighted_graph g = game.getJava_Graph_Not_to_be_used();
		//gg.init(g);
		_ar = new Arena();
		_ar.setGraph(g);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new window("test Ex2");
		_win.setSize(1000, 700);
		_win.panel.update(_ar);
//		_win.gamePanel.update(_ar);


		_win.show();
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("agents");
			System.out.println(info);
			System.out.println(game.getPokemons());
			int src_node = 0;  // arbitrary node, you should start at one of the pokemon
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),g);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}
}

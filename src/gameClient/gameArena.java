package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class gameArena {
    public static final double EPS1 = 0.001, EPS2=EPS1*EPS1, EPS=EPS2;
    private directed_weighted_graph _graph;
    private List<CL_Agent> _agents;
    private List<CL_Pokemon> _pokemons;
    private List<String> _info;
    private static Point3D MIN = new Point3D(0, 100,0);
    private static Point3D MAX = new Point3D(0, 100,0);

    public gameArena() {;
        _info = new ArrayList<String>();
    }
    private gameArena(directed_weighted_graph g, List<CL_Agent> r, List<CL_Pokemon> p) {
        _graph = g;
        this.setAgents(r);
        this.setPokemons(p);
    }
    public void setPokemons(List<CL_Pokemon> f) {
        this._pokemons = f;
    }
    public void setAgents(List<CL_Agent> f) {
        this._agents = f;
    }
    public void setGraph(directed_weighted_graph g) {this._graph =g;}//init();}
    private void init( ) {
        MIN=null; MAX=null;
        double x0=0,x1=0,y0=0,y1=0;
        Iterator<node_data> iter = _graph.getV().iterator();
        while(iter.hasNext()) {
            geo_location c = iter.next().getLocation();
            if(MIN==null) {x0 = c.x(); y0=c.y(); x1=x0;y1=y0;MIN = new Point3D(x0,y0);}
            if(c.x() < x0) {x0=c.x();}
            if(c.y() < y0) {y0=c.y();}
            if(c.x() > x1) {x1=c.x();}
            if(c.y() > y1) {y1=c.y();}
        }
        double dx = x1-x0, dy = y1-y0;
        MIN = new Point3D(x0-dx/10,y0-dy/10);
        MAX = new Point3D(x1+dx/10,y1+dy/10);

    }
    public List<CL_Agent> getAgents() {return _agents;}
    public List<CL_Pokemon> getPokemons() {return _pokemons;}
    public directed_weighted_graph getGraph() {
        return _graph;
    }
    public List<String> get_info() {
        return _info;
    }
    public void set_info(List<String> _info) {
        this._info = _info;
    }

    ////////////////////////////////////////////////////
    public static List<CL_Agent> json2Agents(String json, directed_weighted_graph gg) {
        ArrayList<CL_Agent> ans = new ArrayList<CL_Agent>();
        try {
            JSONObject ttt = new JSONObject(json);
            JSONArray agentsObj = ttt.getJSONArray("Agents");
            for(int i=0;i<agentsObj.length();i++) {
                CL_Agent agent = new CL_Agent(gg,0);
                agent.update(agentsObj.get(i).toString());
                ans.add(agent);
            }
            //= getJSONArray("Agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ans;
    }
    public static ArrayList<CL_Pokemon> json2Pokemons(String fs) {
        ArrayList<CL_Pokemon> ans = new  ArrayList<CL_Pokemon>();
        try {
            JSONObject ttt = new JSONObject(fs);
            JSONArray pokemonsObj = ttt.getJSONArray("Pokemons");
            for(int i=0;i<pokemonsObj.length();i++) {
                JSONObject pp = pokemonsObj.getJSONObject(i);
                JSONObject pk = pp.getJSONObject("Pokemon");
                int type = pk.getInt("type");
                double val = pk.getDouble("value");
                //double s = 0;//pk.getDouble("speed");
                String pos = pk.getString("pos");
                CL_Pokemon pokemon = new CL_Pokemon(new Point3D(pos), type, val, 0, null);
                ans.add(pokemon);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
        return ans;
    }
    public static void updateEdge(CL_Pokemon pokemon, directed_weighted_graph g) {

       Collection<node_data> n = g.getV();
       List <node_data> nodes = new LinkedList<>();
       nodes.addAll(n);

        Comparator cmp = new geoLoComp();
        Collections.sort(nodes, cmp);

        int i=0;
        int j = nodes.size();
        double pDistFromNodes = (pokemon.getLocation().distance(nodes.get(i).getLocation())) + (pokemon.getLocation().distance(nodes.get(j).getLocation()));
        double nodesDist = nodes.get(i).getLocation().distance(nodes.get(j).getLocation());



        //	oop_edge_data ans = null;
        Iterator<node_data> itr = g.getV().iterator();
        while(itr.hasNext()) {
            node_data v = itr.next();
            Iterator<edge_data> iter = g.getE(v.getKey()).iterator();
            while(iter.hasNext()) {
                edge_data e = iter.next();
                boolean f = isOnEdge(pokemon.getLocation(), e,pokemon.getType(), g);
                if(f) {
                    pokemon.set_edge(e);
                }
            }
        }
    }

    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest ) {

        boolean ans = false;
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if(dist>d1-EPS2) {ans = true;}
        return ans;
    }
    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
        geo_location src = g.getNode(s).getLocation();
        geo_location dest = g.getNode(d).getLocation();
        return isOnEdge(p,src,dest);
    }
    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
        int src = g.getNode(e.getSrc()).getKey();
        int dest = g.getNode(e.getDest()).getKey();
        if(type<0 && dest>src) {return false;}
        if(type>0 && src>dest) {return false;}
        return isOnEdge(p,src, dest, g);
    }

    private static Range2D GraphRange(directed_weighted_graph g) {
        Iterator<node_data> itr = g.getV().iterator();
        double x0=0,x1=0,y0=0,y1=0;
        boolean first = true;
        while(itr.hasNext()) {
            geo_location p = itr.next().getLocation();
            if(first) {
                x0=p.x(); x1=x0;
                y0=p.y(); y1=y0;
                first = false;
            }
            else {
                if(p.x()<x0) {x0=p.x();}
                if(p.x()>x1) {x1=p.x();}
                if(p.y()<y0) {y0=p.y();}
                if(p.y()>y1) {y1=p.y();}
            }
        }
        Range xr = new Range(x0,x1);
        Range yr = new Range(y0,y1);
        return new Range2D(xr,yr);
    }
    public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
        Range2D world = GraphRange(g);
        Range2Range ans = new Range2Range(world, frame);
        return ans;
    }

}

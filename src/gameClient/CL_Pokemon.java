package gameClient;
import api.edge_data;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;


/**
 * This class represents the set of operations applicable on a pokemons in the game
 */
public class CL_Pokemon implements  Comparable<CL_Pokemon>{
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	int persecutedBy = -1;
	double tag;
	int src;
	//TODO add who persecuted for switching

	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
		//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
	//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}

	public int getPersecutedBy() {
		return persecutedBy;
	}

	public void setPersecutedBy(int persecutedBy) {
		this.persecutedBy = persecutedBy;
	}

	public double getTag() {
		return tag;
	}

	public int getSrc (){

		if (getType() < 0) { this.src = Math.max(get_edge().getSrc(),get_edge().getDest());}

		else {this.src = Math.min(get_edge().getSrc(),get_edge().getDest());}

		return this.src;

	}

	public void setTag(double tag) {
		this.tag = tag;
	}

	@Override
	public int compareTo(@NotNull CL_Pokemon o) {
		if (this._value < o.getValue()){
			return 1;
		}
		if (this._value > o.getValue()){
			return -1;
		}
		return 0;
	}

	//TODO check value?
	//TODO use EPS?
	public boolean sameAs(CL_Pokemon other){
		if(_value == other.getValue()) {
			return getLocation().equals(other.getLocation());
		}
		return false;
	}
}

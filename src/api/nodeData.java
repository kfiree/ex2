package api;


import com.google.gson.annotations.SerializedName;


/**
 * This class represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 */

public class nodeData implements node_data, Comparable {

    @SerializedName("id")
    private int key;
    private double weight;
    private String info;
    private int tag;
    @SerializedName("pos")
    private geo_location location = new geoLocation();
    private static int keyGenerator=0;

    /**
     * empty constructor
     */
    public nodeData() {
        this.key=keyGenerator;
        keyGenerator++;
    }

    /**
     * constructor
     * @param key the id of node
     */
    public nodeData(int key) {
        this.key = key;
    }

    /**
     * copy constructor
     * @param other other node
     */
    public nodeData(node_data other) {
        this.key = other.getKey();
        this.tag = other.getTag();
        this.info = other.getInfo();
        this.weight = other.getWeight();
        this.location = new geoLocation(other.getLocation());

    }

    /**
     * Returns the key (id) associated with this node.
     */
    @Override
    public int getKey() {
        return this.key;
    }
    /** Returns the location of this node, if
     * none return null.
     */
    @Override
    public geo_location getLocation() {
        return this.location;
    }
    /** Allows changing this node's location.
     * @param l - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location l) {
        this.location = l;
    }
    /**
     * Returns the weight associated with this node.
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        this.weight = w;
    }
    /**
     * Returns the remark (meta data) associated with this node.
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     */
    @Override
    public int getTag() {
        return this.tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * Compares this node with the specified object for order.
     * @param o other node
     */
    @Override
    public int compareTo(Object o) {

        node_data node = (node_data) o;

        if (getWeight() > node.getWeight()) {
            return 1;
        }
        if (getWeight() < node.getWeight()) {
            return -1;
        }
        return 0;

    }
    /**
     * Compares this node with the specified object for order.
     * @param o other node
     */
    public boolean equals(Object o) {
        if (!(o instanceof node_data)) {
            return false;
        }
        node_data node = (nodeData)o;
        return this.key == node.getKey();
    }
}

package api;
/**
 * This class represents the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 * @author boaz.benmoshe
 *
 */

public class edgeData implements edge_data {
    private int src, dest, tag;
    private double w;
    private String info;

    /**
     * empty constructor
     */
    public edgeData() {
        this.src = 0;
        this.dest = 0;
        this.tag = 0;
        this.w = 0;
        this.info = " ";
    }

    /**
     * copy constructor
     * @param other other edge
     */
    public edgeData(edge_data other) {
        this.src = other.getSrc();
        this.dest = other.getDest();
        this.w = other.getWeight();
        this.info = other.getInfo();
        this.tag = other.getTag();
    }

    /**
     * constructor
     * @param src src node id
     * @param dest dest node id
     * @param weight the w of new edge
     */
    public edgeData(int src, int dest, double weight) {

        if (weight >0) {
            //edge from node to itself
            if (src == dest){
                this.src = src;
                this.dest = dest;
                this.w = 0;
            }
            else {
                this.src = src;
                this.dest = dest;
                this.w = weight;
            }
        }
    }
    /**
     * Compares this edge with the specified object for order.
     * @param o other edge
     */
    @Override
    public boolean equals(Object o) {
        if (!( o instanceof edge_data)){
            return false;
        }
        edge_data e = (edgeData) o;
        return this.src== e.getSrc() && this.dest == e.getDest() && this.w == e.getWeight();
    }
    /**
     * The id of the source node of this edge.
     */
    @Override
    public int getSrc() {
        return this.src;
    }
    /**
     * The id of the destination node of this edge
     */
    @Override
    public int getDest() {
        return this.dest;
    }
    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return this.w;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     */
    @Override
    public String getInfo() {
        return this.info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
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
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}


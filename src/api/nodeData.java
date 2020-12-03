package api;


public class nodeData implements node_data, Comparable {
    //TODO check deafult of var below
    private int key;
    private double weight;
    private String info;
    private int tag;
    private geo_location location;
    static private int keyGenerator=0;

    //empty constructor
    public nodeData() {
        this.key=keyGenerator;
        keyGenerator++;
    }

    //copy constructor
    public nodeData(node_data other) {
        this.key = other.getKey();
        this.tag = other.getTag();
        this.info = other.getInfo();
        this.weight = other.getWeight();
        this.location = new geoLocation(other.getLocation());
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public geo_location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(geo_location l) {
        this.location = l;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

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
}

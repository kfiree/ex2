package api;

public class nodeData implements node_data {
    //TODO check deafult of var below
    private int key;
    private double weight;
    private String info;
    private int tag;
    private geo_location location;
    static private int keyGenerator=0;

    public nodeData() {
        this.key=keyGenerator;
        keyGenerator++;
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
}

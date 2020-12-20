package api;

public class edgeData implements edge_data {
    private int src, dest, tag;
    private double w;
    private String info;

    //empty constructor
    public edgeData() {
        this.src = 0;
        this.dest = 0;
        this.tag = 0;
        this.w = 0;
        this.info = " ";
    }

    //copy constructor
    public edgeData(edge_data other) {
        this.src = other.getSrc();
        this.dest = other.getDest();
        this.w = other.getWeight();
        this.info = other.getInfo();
        this.tag = other.getTag();
    }

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
    @Override
    public boolean equals(Object o) {
        if (!( o instanceof edge_data)){
            return false;
        }
        edge_data e = (edgeData) o;
        return this.src== e.getSrc() && this.dest == e.getDest() && this.w == e.getWeight();
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.w;
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



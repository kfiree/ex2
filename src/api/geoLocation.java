package api;

public class geoLocation implements geo_location {

    //TODO check if initilaize needed
    //TODO check if private or public
    public double x,y,z;

    //empty constructor
    public geoLocation() {

    }

    public geoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //copy constructor
    public geoLocation(geo_location other) {
        this.x = other.x();
        this.y = other.y();
        this.z = other.z();
    }



    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location otherG) {
        //3d distance = ((x2 - x1)2 + (y2 - y1)2 + (z2 - z1)2)1/2
        return Math.sqrt(Math.pow(this.x-otherG.x(), 2) + Math.pow(this.y-otherG.y(), 2) + Math.pow(this.z-otherG.z(), 2));
    }
}

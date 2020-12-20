package api;

import gameClient.PokemonEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
/**
 * This class represents a geo location (x,y,z), aka Point3D
 */
public class geoLocation implements geo_location,Comparable<geoLocation> {
    public double x,y,z;

    /**
     * empty constructor
     */
    public geoLocation() {

    }

    /**
     * constructor
     * @param x x Location
     * @param y y Location
     * @param z z Location
     */
    public geoLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * copy constructor
     * @param other geo_location other
     */
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

    /**
     * return the dist from geo_location other
     * @param otherG geo_location other
     */
    @Override
    public double distance(geo_location otherG) {
        //3d distance = ((x2 - x1)2 + (y2 - y1)2 + (z2 - z1)2)1/2
        return Math.sqrt(Math.pow(this.x-otherG.x(), 2) + Math.pow(this.y-otherG.y(), 2) + Math.pow(this.z-otherG.z(), 2));
    }
    /**
     * Compares this geoLocation with the specified object for order.
     * @param o other geoLocation
     */
    @Override
    public int compareTo(@NotNull geoLocation o) {
        geoLocation firsAxes = new geoLocation(0, 0, 0);

        if (distance(firsAxes) > o.distance(firsAxes)) {
            return 1;
        }
        if (distance(firsAxes) < o.distance(firsAxes)) {
            return -1;
        }
        return 0;
    }

}

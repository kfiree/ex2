package gameClient;

import api.geoLocation;
import api.geo_location;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;

/**
 * This class is CL_Pokemon Comparator by geoLocation
 */
public class geoLoCompPokemon implements Comparator<CL_Pokemon> {


    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {

        geoLocation g1 = o1.getLocation();
        geoLocation g2 = o2.getLocation();

        return g1.compareTo(g2);

    }

}

package gameClient;

import api.geoLocation;
import api.geo_location;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import org.jetbrains.annotations.NotNull;
import java.util.Comparator;


public class geoLoCompPokemon implements Comparator<CL_Pokemon> {


    @Override
    public int compare(CL_Pokemon o1, CL_Pokemon o2) {

        geo_location g_1 = o1.getLocation();
        geo_location g_2 = o2.getLocation();

        geoLocation g1 = (geoLocation)g_1;
        geoLocation g2 = (geoLocation)g_2;

        return g1.compareTo(g2);

    }

}

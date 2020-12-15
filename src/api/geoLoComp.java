package api;

import org.jetbrains.annotations.NotNull;
import java.util.Comparator;


public class geoLoComp extends geoLocation implements Comparator<geoLocation>  {

    @Override
    public int compare(geoLocation o1, geoLocation o2) {
        return o1.compareTo(o2);
    }

}

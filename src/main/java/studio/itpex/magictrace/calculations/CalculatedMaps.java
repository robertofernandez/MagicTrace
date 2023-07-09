package studio.itpex.magictrace.calculations;

import java.util.HashMap;

import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class CalculatedMaps {
    private HashMap<String, ColorMap> allMaps;

    public CalculatedMaps() {
        allMaps = new HashMap<>();
    }

    public HashMap<String, ColorMap> getAllMaps() {
        return allMaps;
    }

}

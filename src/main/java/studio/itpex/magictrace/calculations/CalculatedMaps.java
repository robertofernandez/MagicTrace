package studio.itpex.magictrace.calculations;

import java.util.HashMap;

import studio.itpex.images.mapping.ColorMap;

public class CalculatedMaps {
    private HashMap<String, ColorMap> allMaps;

    public CalculatedMaps() {
        allMaps = new HashMap<>();
    }

    public HashMap<String, ColorMap> getAllMaps() {
        return allMaps;
    }

}

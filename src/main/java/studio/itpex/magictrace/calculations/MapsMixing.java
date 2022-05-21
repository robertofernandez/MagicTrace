package studio.itpex.magictrace.calculations;

import studio.itpex.images.color.ColorsUtils;
import studio.itpex.images.mapping.ColorMap;

public class MapsMixing implements FrameCalculation {

    private String map1Name;
    private String map2Name;
    private String targetMapName;
    private CalculationsConfiguration configuration;

    public MapsMixing(String map1Name, String map2Name, String targetMapName, CalculationsConfiguration configuration) {
        this.map1Name = map1Name;
        this.map2Name = map2Name;
        this.targetMapName = targetMapName;
        this.configuration = configuration;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        ColorMap colorMap1 = maps.getAllMaps().get(map1Name);
        ColorMap colorMap2 = maps.getAllMaps().get(map2Name);
        ColorMap mixedMap = ColorsUtils.mixMaps(colorMap1, colorMap2, configuration.getNumber("transparency-proportion"));
        maps.getAllMaps().put(targetMapName, mixedMap);
    }

}

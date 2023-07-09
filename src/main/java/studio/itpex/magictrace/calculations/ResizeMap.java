package studio.itpex.magictrace.calculations;

import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.operations.GeometryUtils;

public class ResizeMap implements FrameCalculation {
    private String mapName;
    private String calculatedMapName;
    private CalculationsConfiguration configuration;

    public ResizeMap(String mapName, String calculatedMapName, CalculationsConfiguration configuration) {
        this.mapName = mapName;
        this.calculatedMapName = calculatedMapName;
        this.configuration = configuration;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        ColorMap imageMap = maps.getAllMaps().get(mapName);
        int width = (int) Math.round(imageMap.getWidth() * configuration.getNumber("resize-proportion-x"));
        int height = (int) Math.round(imageMap.getHeight() * configuration.getNumber("resize-proportion-y"));
        ColorMap enlargedMap = GeometryUtils.enlargeRegion(imageMap, width, height, 0, 0, imageMap.getWidth(),
                imageMap.getHeight());
        maps.getAllMaps().put(calculatedMapName, enlargedMap);
    }

}

package studio.itpex.magictrace.calculations;

import studio.itpex.images.mapping.ColorMap;
import studio.itpex.images.utils.GeometryUtils;

public class ResizeMap implements FrameCalculation {
    private String mapName;
    private double proportionX;
    private double proportionY;
    private String calculatedMapName;

    public ResizeMap(String mapName, String calculatedMapName, double proportionX, double proportionY) {
        this.mapName = mapName;
        this.calculatedMapName = calculatedMapName;
        this.proportionX = proportionX;
        this.proportionY = proportionY;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        ColorMap imageMap = maps.getAllMaps().get(mapName);
        int width = (int) Math.round(imageMap.getWidth() * proportionX);
        int height = (int) Math.round(imageMap.getHeight() * proportionY);
        ColorMap enlargedMap = GeometryUtils.enlargeRegion(imageMap, width, height, 0, 0, imageMap.getWidth(),
                imageMap.getHeight());
        maps.getAllMaps().put(calculatedMapName, enlargedMap);
    }

}

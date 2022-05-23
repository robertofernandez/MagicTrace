package studio.itpex.magictrace.calculations;

import studio.itpex.images.color.PaintUtils;
import studio.itpex.images.mapping.ColorMap;

public class PaintUsingMagnitudeAndColors implements FrameCalculation {

    private String colorMapName;
    private String magnitudeMapName;
    private String resultMapName;

    public PaintUsingMagnitudeAndColors(String colorMapName, String magnitudeMapName, String resultMapName) {
        this.colorMapName = colorMapName;
        this.magnitudeMapName = magnitudeMapName;
        this.resultMapName = resultMapName;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        if (!maps.getAllMaps().containsKey(colorMapName) || !maps.getAllMaps().containsKey(magnitudeMapName)) {
            throw new Exception("There are no needed maps to paint from magnitude");
        }
        ColorMap colorMap = maps.getAllMaps().get(colorMapName);
        ColorMap magnitudeMap = maps.getAllMaps().get(magnitudeMapName);
        ColorMap resultMap = PaintUtils.paintUsingMagnitudeAndColors(colorMap, magnitudeMap);
        maps.getAllMaps().put(resultMapName, resultMap);
    }

}

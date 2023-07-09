package studio.itpex.magictrace.calculations;

import ar.com.sodhium.commons.img.colors.PaintUtils;
import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class PaintUsingMagnitudeDifferencesAndColors implements FrameCalculation {

    private String colorMapName;
    private String magnitudeMapName;
    private String resultMapName;
    private String baseMapName;

    public PaintUsingMagnitudeDifferencesAndColors(String colorMapName, String magnitudeMapName, String baseMapName,
            String resultMapName) {
        this.colorMapName = colorMapName;
        this.magnitudeMapName = magnitudeMapName;
        this.baseMapName = baseMapName;
        this.resultMapName = resultMapName;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        if (!maps.getAllMaps().containsKey(colorMapName) || !maps.getAllMaps().containsKey(magnitudeMapName)
                || !maps.getAllMaps().containsKey(baseMapName)) {
            throw new Exception("There are no needed maps to paint from magnitude");
        }
        ColorMap colorMap = maps.getAllMaps().get(colorMapName);
        ColorMap magnitudeMap = maps.getAllMaps().get(magnitudeMapName);
        ColorMap magnitudeBaseMap = maps.getAllMaps().get(baseMapName);

        ColorMap resultMap = PaintUtils.paintUsingMagnitudeDifferencesAndColors(colorMap, magnitudeMap,
                magnitudeBaseMap);
        maps.getAllMaps().put(resultMapName, resultMap);
    }

}

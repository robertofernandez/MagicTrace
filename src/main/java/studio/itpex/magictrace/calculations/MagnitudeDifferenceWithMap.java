package studio.itpex.magictrace.calculations;

import ar.com.sodhium.commons.img.colors.MagnitudeUtils;
import ar.com.sodhium.commons.img.colors.RgbColor;
import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class MagnitudeDifferenceWithMap implements FrameCalculation {
    private String colorMapName;
    private String baseMapName;
    private String resultMapName;

    public MagnitudeDifferenceWithMap(String colorMapName, String baseMapName, String resultMapName) {
        this.colorMapName = colorMapName;
        this.baseMapName = baseMapName;
        this.resultMapName = resultMapName;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        if (!maps.getAllMaps().containsKey(colorMapName) || !maps.getAllMaps().containsKey(baseMapName)) {
            throw new Exception("There are no needed maps to calculate difference");
        }
        ColorMap finalMap = maps.getAllMaps().get(colorMapName);
        ColorMap baseMap = maps.getAllMaps().get(baseMapName);

        ColorMap outputMap = baseMap.cloneMap();

        for (int x = 0; x < baseMap.getWidth(); x++) {
            for (int y = 0; y < baseMap.getHeight(); y++) {
                if (x < finalMap.getWidth() && y < finalMap.getHeight()) {
                    RgbColor color = finalMap.getRgbColor(x, y);
                    RgbColor baseColor = baseMap.getRgbColor(x, y);
                    double magnitude = MagnitudeUtils.getMagnitude(color.getRed(), color.getGreen(), color.getBlue());
                    double baseMagnitude = MagnitudeUtils.getMagnitude(baseColor.getRed(), baseColor.getGreen(),
                            baseColor.getBlue());
                    double difference = baseMagnitude - magnitude;
                    double normalizedDifference = difference / MagnitudeUtils.getMagnitude(256, 256, 256);

                    // double differenceReference = 127 + 128 * normalizedDifference;
                    double differenceReference = 255;
                    if (normalizedDifference > 0) {
                        differenceReference = 255 - (normalizedDifference * 255);
                    }

                    int roundR = (int) Math.round(differenceReference);
                    int roundG = (int) Math.round(differenceReference);
                    int roundB = (int) Math.round(differenceReference);
                    outputMap.setRgbColor(x, y, new RgbColor(roundR, roundG, roundB));
                } else {
                    outputMap.setRgbColor(x, y, new RgbColor(255, 255, 255));
                }
            }
        }

        maps.getAllMaps().put(resultMapName, outputMap);
    }

}

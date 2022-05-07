package studio.itpex.images.color;

import java.awt.Color;

import studio.itpex.images.mapping.ColorMap;

public class PaintUtils {

    public static ColorMap paintUsingMagnitudeAndColors(ColorMap colorsMap, ColorMap magnitudeMap) throws Exception {
        ColorMap outputMap = magnitudeMap.cloneMap();
        for (int x = 0; x < outputMap.getWidth(); x++) {
            for (int y = 0; y < outputMap.getHeight(); y++) {
                Color magnitudeColor = magnitudeMap.getColor(x, y);
                if (x < colorsMap.getWidth() && y < colorsMap.getHeight()) {
                    double cubicMagnitude = MagnitudeUtils.getCubicMagnitude(magnitudeColor.getRed() + 1,
                            magnitudeColor.getGreen() + 1, magnitudeColor.getBlue() + 1);
                    RgbColor colorFromPalette = colorsMap.getRgbColor(x, y);
                    Double red0 = (double) (colorFromPalette.getRed() + 1);
                    Double green0 = (double) (colorFromPalette.getGreen() + 1);
                    Double blue0 = (double) (colorFromPalette.getBlue() + 1);

                    Double d = green0 / red0;
                    Double e = blue0 / red0;

                    Double finalRed = Math.cbrt(cubicMagnitude / (e * e + d * d + d));
                    Double finalGreen = d * finalRed;
                    Double finalBlue = e * finalRed;

                    int red = (int) Math.round(Math.max(0, Math.min(255, finalRed - 1)));
                    int green = (int) Math.round(Math.max(0, Math.min(255, finalGreen - 1)));
                    int blue = (int) Math.round(Math.max(0, Math.min(255, finalBlue - 1)));

                    outputMap.setColor(x, y, new Color(red, green, blue));
                } else {
                    outputMap.setColor(x, y, magnitudeColor);
                }
            }
        }
        return outputMap;
    }
}

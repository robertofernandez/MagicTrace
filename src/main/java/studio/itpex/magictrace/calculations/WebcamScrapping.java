package studio.itpex.magictrace.calculations;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

import ar.com.sodhium.commons.img.colors.map.ColorMap;
import studio.itpex.images.mapping.OperatedColorMap;
import studio.itpex.images.utils.ImageRepresentation;
import studio.itpex.java.utils.Rotate180;

public class WebcamScrapping implements FrameCalculation {
    private Webcam webcam;
    private boolean rotated;

    public WebcamScrapping(Webcam webcam) {
        this.webcam = webcam;
        rotated = false;
    }
    
    public void rotate() {
        rotated = !rotated;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        BufferedImage lastCameraImage = webcam.getImage();
        ImageRepresentation representation = new ImageRepresentation(lastCameraImage);
        
        ColorMap imageMap = null;
        int width = lastCameraImage.getWidth();
        int height = lastCameraImage.getHeight();
        if(!rotated) {
            imageMap = new ColorMap(width, height);
        } else {
            imageMap = new OperatedColorMap(width, height, new Rotate180(width, height));
        }
        imageMap.setRed(representation.getRed());
        imageMap.setGreen(representation.getGreen());
        imageMap.setBlue(representation.getBlue());
        maps.getAllMaps().put("webcam", imageMap);
    }

    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }
    
    public boolean isRotated() {
        return rotated;
    }

}

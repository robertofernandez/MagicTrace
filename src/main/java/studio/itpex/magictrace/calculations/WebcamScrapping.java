package studio.itpex.magictrace.calculations;

import java.awt.image.BufferedImage;

import com.github.sarxos.webcam.Webcam;

import studio.itpex.images.mapping.ColorMap;
import studio.itpex.images.utils.ImageRepresentation;

public class WebcamScrapping implements FrameCalculation {
    private Webcam webcam;

    public WebcamScrapping(Webcam webcam) {
        this.webcam = webcam;
    }

    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        BufferedImage lastCameraImage = webcam.getImage();
        ImageRepresentation representation = new ImageRepresentation(lastCameraImage);
        ColorMap imageMap = new ColorMap(lastCameraImage.getWidth(), lastCameraImage.getHeight());
        imageMap.setRed(representation.getRed());
        imageMap.setGreen(representation.getGreen());
        imageMap.setBlue(representation.getBlue());
        maps.getAllMaps().put("webcam", imageMap);
    }

}

package studio.itpex.magictrace.tests.ide;

import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import org.apache.log4j.PropertyConfigurator;

import com.github.sarxos.webcam.Webcam;

import studio.itpex.images.color.ColorsUtils;
import studio.itpex.images.mapping.ColorMap;
import studio.itpex.images.utils.GeometryUtils;
import studio.itpex.images.utils.ImageRepresentation;
import studio.itpex.magictrace.tests.ide.panels.ImagePanel;

public class MagicTraceTestIde extends JFrame {

    public static boolean SHOW_TEEMO = true;
    public static final int desktopWidth = 1200;
    public static final int desktopHeight = 680;
    private JDesktopPane mainDesktopPane;
    private static final long serialVersionUID = 6733330978640433656L;
    private ImagePanel cameraPanel;
    private ImagePanel tracePanel;
    private static double proportion = 0.8;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected static void createAndShowGUI() {
        CurrentMagicTraceTestIdeMaps currentMaps = new CurrentMagicTraceTestIdeMaps();
        MagicTraceTestIde ide = new MagicTraceTestIde();
        ide.pack();
        ide.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String log4jConfPath = "src/main/resources/config/log4j.properties";
                    PropertyConfigurator.configure(log4jConfPath);
                    System.out.println("About to start");
                    Webcam webcam = Webcam.getDefault();
                    System.out.println("opening camera");
                    webcam.open();
                    System.out.println("Camera open");

                    File teemoFile = new File("E:\\tmp\\teemo2.jpg");

                    BufferedImage teemoImage = null;
                    ColorMap teemoImageMap = null;
                    try {
                        teemoImage = ImageIO.read(teemoFile);

                        // MediaTracker object is used to block the task
                        // until image is loaded, or 10 seconds elapses
                        // since load starting moment
                        MediaTracker tracker = new MediaTracker(ide);
                        tracker.addImage(teemoImage, 1);
                        if (!tracker.waitForID(1, 30000)) {
                            System.out.println("Error loading image");
                            System.exit(1);
                        }
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                    if (teemoImage != null && SHOW_TEEMO) {
                        ImageRepresentation teemoRepresentation = new ImageRepresentation(teemoImage);
                        teemoImageMap = new ColorMap(teemoImage.getWidth(), teemoImage.getHeight());
                        teemoImageMap.setRed(teemoRepresentation.getRed());
                        teemoImageMap.setGreen(teemoRepresentation.getGreen());
                        teemoImageMap.setBlue(teemoRepresentation.getBlue());

                        // updateImageFromMap(teemoImageMap, ide);

                        updateImageFromCameraMixing(ide, webcam, teemoImageMap, proportion, currentMaps);
                    } else {
                        updateImageFromCamera(ide, webcam);
                    }

                    // for (int i = 0; i < 190; i++) {
                    for (int i = 0;; i++) {
                        //System.out.println("sleeping " + i);
                        Thread.sleep(100);
                        if (teemoImageMap != null && SHOW_TEEMO) {
                            updateImageFromCameraMixing(ide, webcam, teemoImageMap, proportion, currentMaps);
                            updateImageFromCurrentMap(ide.getTracePanel(), currentMaps.getCameraMap());
                        } else {
                            updateImageFromCamera(ide, webcam);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public MagicTraceTestIde() {
        this.setTitle("Magic Trace Test IDE");
        mainDesktopPane = new JDesktopPane();
        mainDesktopPane.setPreferredSize(new Dimension(desktopWidth, desktopHeight));

        cameraPanel = new ImagePanel("Combined Area");
        mainDesktopPane.add(cameraPanel);
        tracePanel = new ImagePanel("Trace Area");
        mainDesktopPane.add(tracePanel);

        this.getContentPane().add("North", mainDesktopPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public ImagePanel getCameraPanel() {
        return cameraPanel;
    }

    public ImagePanel getTracePanel() {
        return tracePanel;
    }

    private static void updateImageFromCamera(MagicTraceTestIde ide, Webcam webcam) throws Exception {
        BufferedImage image = webcam.getImage();
        System.out.println("Image retrieved");
        ImageRepresentation representation = new ImageRepresentation(image);
        ColorMap imageMap = new ColorMap(image.getWidth(), image.getHeight());
        imageMap.setRed(representation.getRed());
        imageMap.setGreen(representation.getGreen());
        imageMap.setBlue(representation.getBlue());

        ColorMap enlargedMap = GeometryUtils.enlargeRegion(imageMap, image.getWidth() * 2, image.getHeight() * 2, 0, 0,
                image.getWidth(), image.getHeight());

        ide.getCameraPanel().setMap(enlargedMap);
        // ide.getCameraPanel().updateImage();
        System.out.println("Image updated");
    }

    private static void updateImageFromCurrentMap(ImagePanel panel, ColorMap currentMap) throws Exception {
        panel.setMap(currentMap);
        // panel.updateImage();
    }

    private static void updateImageFromMap(ColorMap imageMap, MagicTraceTestIde ide) throws Exception {
        ide.getCameraPanel().setMap(imageMap);
        // ide.getCameraPanel().updateImage();
        System.out.println("Image updated");
    }

    private static void updateImageFromCameraMixing(MagicTraceTestIde ide, Webcam webcam, ColorMap mixingMap,
            double proportion, CurrentMagicTraceTestIdeMaps currentMaps) throws Exception {
        BufferedImage lastCameraImage = webcam.getImage();
        System.out.println("Image retrieved");
        ImageRepresentation representation = new ImageRepresentation(lastCameraImage);
        ColorMap imageMap = new ColorMap(lastCameraImage.getWidth(), lastCameraImage.getHeight());
        imageMap.setRed(representation.getRed());
        imageMap.setGreen(representation.getGreen());
        imageMap.setBlue(representation.getBlue());

        ColorMap enlargedMap = GeometryUtils.enlargeRegion(imageMap, lastCameraImage.getWidth() * 2,
                lastCameraImage.getHeight() * 2, 0, 0, lastCameraImage.getWidth(), lastCameraImage.getHeight());
        ColorMap mixedMap = ColorsUtils.mixMaps(enlargedMap, mixingMap, proportion);

        currentMaps.setCameraMap(enlargedMap);
        currentMaps.setMixedMap(mixedMap);

        ide.getCameraPanel().setMap(mixedMap);
        // ide.getCameraPanel().updateImage();
        System.out.println("Image updated");
    }

}

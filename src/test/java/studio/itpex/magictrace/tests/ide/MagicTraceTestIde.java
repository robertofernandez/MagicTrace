package studio.itpex.magictrace.tests.ide;

import static com.xuggle.xuggler.Global.DEFAULT_TIME_UNIT;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
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
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.IMediaViewer;
import com.xuggle.mediatool.IMediaWriter;

import studio.itpex.images.color.ColorsUtils;
import studio.itpex.images.mapping.ColorMap;
import studio.itpex.images.utils.GeometryUtils;
import studio.itpex.images.utils.ImageRepresentation;
import studio.itpex.magictrace.tests.ide.panels.ImagePanel;


public class MagicTraceTestIde extends JFrame {

    private static final int FRAME_TIME = 50;
    public static boolean SHOW_TEEMO = true;
    public static final int desktopWidth = 1200;
    public static final int desktopHeight = 680;
    private JDesktopPane mainDesktopPane;
    private static final long serialVersionUID = 6733330978640433656L;
    private ImagePanel cameraPanel;
    private ImagePanel tracePanel;
    private static double proportion = 0.8;
    
    static final long frameRate = DEFAULT_TIME_UNIT.convert(50, MILLISECONDS);
    private static IMediaWriter writer;
    private static int videoStreamIndex;
    private static long nextFrameTime;
    private static long currentFrame = 0;
    private static boolean written = false;

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
                    for (;;) {
                        //System.out.println("sleeping " + i);
                        Thread.sleep(FRAME_TIME);
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
        //System.out.println("Image updated");
        
        if(currentFrame < 100) {
            if(writer == null) {
                writer = ToolFactory.makeWriter("test.mov");
                writer.addListener(ToolFactory.makeViewer(IMediaViewer.Mode.VIDEO_ONLY, true,javax.swing.WindowConstants.EXIT_ON_CLOSE));
                writer.addVideoStream(0, 0, image.getWidth(), image.getHeight());
            }
            writer.encodeVideo(videoStreamIndex, image, nextFrameTime, DEFAULT_TIME_UNIT);
            nextFrameTime += frameRate;
            currentFrame++;
        }

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
        BufferedImage videoImage = new BufferedImage(320, 200, BufferedImage.TYPE_3BYTE_BGR);
        //videoImage.getGraphics().se
        lastCameraImage.copyData(videoImage.getRaster());
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
        //System.out.println("Image updated");
        if(currentFrame < 1000) {
            if(writer == null) {
                writer = ToolFactory.makeWriter("E:\\tmp\\img\\output\\testmt.mov");
                //writer.addListener(ToolFactory.makeViewer(IMediaViewer.Mode.VIDEO_ONLY, true,javax.swing.WindowConstants.EXIT_ON_CLOSE));
                //writer.addVideoStream(0, 0, lastCameraImage.getWidth(), lastCameraImage.getHeight());
                writer.addVideoStream(0, 0, 320, 200);
            }
            
			writer.encodeVideo(videoStreamIndex, videoImage, nextFrameTime, DEFAULT_TIME_UNIT);
            nextFrameTime += frameRate;
            currentFrame++;
        } else {
            if(!written ) {
                written = true;
                writer.close();
            }
        }
    }

}

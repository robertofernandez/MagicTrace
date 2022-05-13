package studio.itpex.magictrace.tests.ide;

import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.PropertyConfigurator;

import com.github.sarxos.webcam.Webcam;

import studio.itpex.images.color.ColorsUtils;
import studio.itpex.images.mapping.ColorMap;
import studio.itpex.images.utils.GeometryUtils;
import studio.itpex.images.utils.ImageRepresentation;
import studio.itpex.magictrace.tests.ide.panels.ImagePanel;

public class MagicTraceTestIde3 extends JFrame {
    private static final String CONTRAST_VIEW = "Contrast";
    private static final String MAGIC_PAINT_TRACE_VIEW = "Magic paint trace";
    private static final String CONTRAST_TRACE_VIEW = "Contrast trace";
    private static final String TRANSPARENCY_TRACE_VIEW = "Transparency trace";
    private static final String CAMERA_VIEW = "Camera";
    public static final String VERSION = "0.3";
    private static final int FRAME_TIME = 50;
    public static boolean SHOW_TEEMO = true;
    public static final int desktopWidth = 1200;
    public static final int desktopHeight = 680;
    private JDesktopPane mainDesktopPane;
    private static final long serialVersionUID = 6733330978640433656L;
    private ImagePanel cameraPanel;
    private ImagePanel tracePanel;
    private static double proportion = 0.8;

    private ImagePanel[] allViews = new ImagePanel[2];
    private int currentTargetedView = 0;

    private JMenuBar menuBar;
    private JMenu operationsMenu;
    private JMenu viewsMenu;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected static void createAndShowGUI() {
        CurrentMagicTraceTestIdeMaps currentMaps = new CurrentMagicTraceTestIdeMaps();
        MagicTraceTestIde3 ide = new MagicTraceTestIde3();
        
        ide.pack();
        ide.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                try {
                    
                    ide.initialize();
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

    protected void initialize() {
        menuBar = new JMenuBar();
        operationsMenu = createOperationsMenu();
        viewsMenu = createViewsMenu();
        menuBar.add(operationsMenu);
        menuBar.add(viewsMenu);
        setJMenuBar(menuBar);
    }

    private JMenu createOperationsMenu() {
        JMenu menu = new JMenu("Operations");
        JMenuItem connect = new JMenuItem("Set base sheet");
        menu.add(connect);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBaseSheet();
            }
        });
        return menu;
    }
    
    private JMenu createViewsMenu() {
        JMenu menu = new JMenu("Views");
        addViewMenuItem(menu, CAMERA_VIEW);
        addViewMenuItem(menu, TRANSPARENCY_TRACE_VIEW);
        addViewMenuItem(menu, CONTRAST_TRACE_VIEW);
        addViewMenuItem(menu, MAGIC_PAINT_TRACE_VIEW);
        addViewMenuItem(menu, CONTRAST_VIEW);
        return menu;
    }

    private void addViewMenuItem(JMenu menu, String title) {
        JMenuItem cameraView = new JMenuItem(title);
        menu.add(cameraView);
        cameraView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setView(title);
            }
        });
    }

    private void setView(String title) {
        // FIXME implement
        trace("info", "View set to " + title);
    }

    public MagicTraceTestIde3() {
        this.setTitle("Magic Trace Test IDE " + VERSION);
        mainDesktopPane = new JDesktopPane();
        mainDesktopPane.setPreferredSize(new Dimension(desktopWidth, desktopHeight));

        cameraPanel = new ImagePanel("Combined Area");
        mainDesktopPane.add(cameraPanel);
        cameraPanel.setLocation(0, 0);
        tracePanel = new ImagePanel("Trace Area");
        mainDesktopPane.add(tracePanel);
        tracePanel.setLocation(650, 0);
        
        allViews[0] = cameraPanel;
        allViews[1] = tracePanel;
        
        tracePanel.setTitle("Trace Area");
        
        tracePanel.setFocusable(false);
        cameraPanel.setFocusable(false);
        //cameraPanel.setDecoration(5);

        this.getContentPane().add("North", mainDesktopPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //System.out.println("typed " + e.getKeyChar());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("released " + e.getKeyChar());

            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                //System.out.println("pressed " + e.getKeyChar());
            }
        };
        
        addKeyListener(keyAdapter);
    }

    public ImagePanel getCameraPanel() {
        return cameraPanel;
    }

    public ImagePanel getTracePanel() {
        return tracePanel;
    }

    private static void updateImageFromCamera(MagicTraceTestIde3 ide, Webcam webcam) throws Exception {
        BufferedImage image = webcam.getImage();
        //System.out.println("Image retrieved");
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
    }

    private static void updateImageFromCurrentMap(ImagePanel panel, ColorMap currentMap) throws Exception {
        panel.setMap(currentMap);
        // panel.updateImage();
    }

    private static void updateImageFromMap(ColorMap imageMap, MagicTraceTestIde3 ide) throws Exception {
        ide.getCameraPanel().setMap(imageMap);
        // ide.getCameraPanel().updateImage();
        System.out.println("Image updated");
    }

    private static void updateImageFromCameraMixing(MagicTraceTestIde3 ide, Webcam webcam, ColorMap mixingMap,
            double proportion, CurrentMagicTraceTestIdeMaps currentMaps) throws Exception {
        BufferedImage lastCameraImage = webcam.getImage();
        //System.out.println("Image retrieved");
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
    }
    
    private void setBaseSheet() {
        // FIXME implement set base sheet using current camera view
        trace("info", "Base sheet stored");
    }

    private void trace(String level, String message) {
        System.out.println("[" + level + "] " + message);
        
    }


}

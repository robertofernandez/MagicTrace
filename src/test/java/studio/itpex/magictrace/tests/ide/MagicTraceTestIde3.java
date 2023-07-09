package studio.itpex.magictrace.tests.ide;

import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.PropertyConfigurator;

import com.github.sarxos.webcam.Webcam;

import ar.com.sodhium.commons.img.colors.map.ColorMap;
import studio.itpex.images.utils.ImageRepresentation;
import studio.itpex.magictrace.calculations.CalculatedMaps;
import studio.itpex.magictrace.calculations.CalculationsConfiguration;
import studio.itpex.magictrace.calculations.CalculationsManager;
import studio.itpex.magictrace.calculations.MagnitudeDifferenceWithMap;
import studio.itpex.magictrace.calculations.MapsMixing;
import studio.itpex.magictrace.calculations.PaintUsingMagnitudeAndColors;
import studio.itpex.magictrace.calculations.PaintUsingMagnitudeDifferencesAndColors;
import studio.itpex.magictrace.calculations.PrioritizedCalculationsSet;
import studio.itpex.magictrace.calculations.ResizeMap;
import studio.itpex.magictrace.calculations.SetBaseMap;
import studio.itpex.magictrace.calculations.WebcamScrapping;
import studio.itpex.magictrace.tests.ide.panels.ImagePanel;

public class MagicTraceTestIde3 extends JFrame {
    private static final String CONTRAST_VIEW = "Contrast";
    private static final String MAGIC_PAINT_TRACE_VIEW = "Magic paint trace";
    private static final String CONTRAST_TRACE_VIEW = "Contrast trace";
    private static final String TRANSPARENCY_TRACE_VIEW = "Transparency trace";
    private static final String CAMERA_VIEW = "Camera";
    public static final String VERSION = "0.4";
    private static final int FRAME_TIME = 50;
    public static final int desktopWidth = 1200;
    public static final int desktopHeight = 680;
    private JDesktopPane mainDesktopPane;
    private static final long serialVersionUID = 6733330978640433656L;
    private ImagePanel panel0;
    private ImagePanel panel1;

    private ImagePanel[] allPanelViews = new ImagePanel[2];
    private String[] currentViews = new String[2];
    private int currentTargetedView = 1;

    private JMenuBar menuBar;

    private JMenu fileMenu;

    private JMenu operationsMenu;
    private JMenu viewsMenu;

    // private HashMap<String, FrameCalculation> calculationsByName;
    // private PrioritizedCalculationsSet prioritizedCalculations;
    private CalculatedMaps calculatedMaps;
    private CalculationsManager calculationsManager;
    private CalculationsConfiguration calculationsConfiguration;

    private PrioritizedCalculationsSet defaultSet;
    private PrioritizedCalculationsSet currentSet;

    private FrameView cameraView;
    private FrameView transparencyTraceView;
    private FrameView contrastView;
    private FrameView contrastTraceView;
    private FrameView magicPaintTraceView;

    private HashMap<String, FrameView> frameViewByName;
    private Webcam webcam;

    private SetBaseMap setBaseMapCalculation;
    private WebcamScrapping webScrappingCalculation;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected static void createAndShowGUI() {
        // CurrentMagicTraceTestIdeMaps currentMaps = new
        // CurrentMagicTraceTestIdeMaps();
        MagicTraceTestIde3 ide = new MagicTraceTestIde3();
        ide.pack();
        ide.setVisible(true);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ide.initialize();
                    for (;;) {
                        // System.out.println("sleeping " + i);
                        Thread.sleep(FRAME_TIME);
                        ide.executeFrame();
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();

    }

    protected void executeFrame() {
        calculationsManager.executeCalculations(calculatedMaps);
        FrameView frameView0 = frameViewByName.get(currentViews[0]);
        FrameView frameView1 = frameViewByName.get(currentViews[1]);
        allPanelViews[0].setMap(calculatedMaps.getAllMaps().get(frameView0.getMapNameToShow()));
        allPanelViews[1].setMap(calculatedMaps.getAllMaps().get(frameView1.getMapNameToShow()));
    }

    protected void initialize() throws Exception {
        menuBar = new JMenuBar();
        operationsMenu = createOperationsMenu();
        fileMenu = createFilesMenu(this);
        viewsMenu = createViewsMenu();
        menuBar.add(fileMenu);
        menuBar.add(operationsMenu);
        menuBar.add(operationsMenu);
        menuBar.add(viewsMenu);
        menuBar.add(createProportionsMenu());
        setJMenuBar(menuBar);

        ImageIcon logo = createImageIconFromAbsolutePath("img/icons/mt_icon.png", "Application Logo");
        setIconImage(logo.getImage());

        String log4jConfPath = "src/main/resources/config/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
        System.out.println("About to start");

        File baseFile = new File("src/main/resources/img/base.png");

        BufferedImage referenceImage = null;
        try {
            referenceImage = ImageIO.read(baseFile);

            // MediaTracker object is used to block the task
            // until image is loaded, or 10 seconds elapses
            // since load starting moment
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(referenceImage, 1);
            if (!tracker.waitForID(1, 30000)) {
                System.out.println("Error loading image");
                System.exit(1);
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }

        if (referenceImage != null) {
            ImageRepresentation baseImageRepresentation = new ImageRepresentation(referenceImage);
            ColorMap baseReferenceImageMap = new ColorMap(referenceImage.getWidth(), referenceImage.getHeight());
            baseReferenceImageMap.setRed(baseImageRepresentation.getRed());
            baseReferenceImageMap.setGreen(baseImageRepresentation.getGreen());
            baseReferenceImageMap.setBlue(baseImageRepresentation.getBlue());
            calculatedMaps.getAllMaps().put("reference-image", baseReferenceImageMap);
        }

        this.setExtendedState(MAXIMIZED_BOTH);
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
        JMenuItem rotateCamera = new JMenuItem("Rotate camera");
        menu.add(rotateCamera);
        rotateCamera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                webScrappingCalculation.rotate();
            }
        });
        
        return menu;
    }

    private JMenu createFilesMenu(MagicTraceTestIde3 ide) {
        JMenu menu = new JMenu("Files");
        JMenuItem openFile = new JMenuItem("Open...");
        menu.add(openFile);
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileSelector = new JFileChooser();
                int returnVal = fileSelector.showOpenDialog(ide);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileSelector.getSelectedFile();

                    BufferedImage referenceImage = null;
                    try {
                        referenceImage = ImageIO.read(selectedFile);

                        // MediaTracker object is used to block the task
                        // until image is loaded, or 10 seconds elapses
                        // since load starting moment
                        MediaTracker tracker = new MediaTracker(ide);
                        tracker.addImage(referenceImage, 1);
                        if (!tracker.waitForID(1, 30000)) {
                            System.out.println("Error loading image");
                            System.exit(1);
                        }

                        if (referenceImage != null) {
                            ImageRepresentation imageReferenceRepresentation = new ImageRepresentation(referenceImage);
                            ColorMap referenceImageMap = new ColorMap(referenceImage.getWidth(),
                                    referenceImage.getHeight());
                            referenceImageMap.setRed(imageReferenceRepresentation.getRed());
                            referenceImageMap.setGreen(imageReferenceRepresentation.getGreen());
                            referenceImageMap.setBlue(imageReferenceRepresentation.getBlue());
                            calculatedMaps.getAllMaps().put("reference-image", referenceImageMap);
                        }
                    } catch (InterruptedException ex) {
                        trace("error", ex.getMessage(), ex);
                    } catch (IOException ex) {
                        trace("error", ex.getMessage(), ex);
                    } catch (Exception e1) {
                        trace("error", e1.getMessage(), e1);
                    }
                }
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

    private JMenu createProportionsMenu() {
        JMenu menu = new JMenu("Trace contrast");
        for (int i = 10; i < 110; i += 10) {
            addProportionMenuItem(menu, i);
        }
        return menu;
    }

    private void addProportionMenuItem(JMenu menu, int proportionPercentage) {
        JMenuItem cameraView = new JMenuItem("" + proportionPercentage + "%");
        menu.add(cameraView);
        cameraView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculationsConfiguration.setNumber("transparency-proportion", ((double) proportionPercentage) / 100);
            }
        });
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
        allPanelViews[currentTargetedView].setTitle(title);
        currentViews[currentTargetedView] = title;
        FrameView frameView0 = frameViewByName.get(currentViews[0]);
        FrameView frameView1 = frameViewByName.get(currentViews[1]);
        currentSet = defaultSet.merge(frameView0.getCalculationsSet()).merge(frameView1.getCalculationsSet());
        calculationsManager.setCalculationsSet(currentSet);
        trace("info", "View set to " + title);
    }

    public MagicTraceTestIde3() {
        this.setTitle("Magic Trace Test IDE " + VERSION);

        webcam = Webcam.getDefault();
        System.out.println("Opening camera...");
        webcam.open();
        System.out.println("Camera open.");

        calculationsConfiguration = new CalculationsConfiguration();
        calculationsConfiguration.setNumber("resize-proportion-x", 2D);
        calculationsConfiguration.setNumber("resize-proportion-y", 2D);
        calculationsConfiguration.setNumber("transparency-proportion", 0.8D);

        calculatedMaps = new CalculatedMaps();
        calculationsManager = new CalculationsManager();
        webScrappingCalculation = new WebcamScrapping(webcam);
        calculationsManager.addCalculation("webcam-capture", webScrappingCalculation);
        calculationsManager.addCalculation("webcam-resize",
                new ResizeMap("webcam", "resized-webcam", calculationsConfiguration));
        calculationsManager.addCalculation("mix-maps", new MapsMixing("resized-webcam", "reference-image",
                "transparency-trace-image", calculationsConfiguration));
        calculationsManager.addCalculation("paint-using-magnitude-and-colors",
                new PaintUsingMagnitudeAndColors("reference-image", "resized-webcam", "painted-image-M&C1"));
        calculationsManager.addCalculation("magnitude-difference-with-base",
                new MagnitudeDifferenceWithMap("resized-webcam", "base-map", "magnitude-difference-base"));
        calculationsManager.addCalculation("paint-using-magnitude-and-colors-contrasted",
                new PaintUsingMagnitudeDifferencesAndColors("reference-image", "resized-webcam", "base-map", "painted-image-M&C2"));

        defaultSet = new PrioritizedCalculationsSet();
        defaultSet.addCalculation(0, "webcam-capture");
        defaultSet.addCalculation(1, "webcam-resize");

        cameraView = new FrameView(CAMERA_VIEW, "resized-webcam");
        // no further calculations as already in default set

        transparencyTraceView = new FrameView(TRANSPARENCY_TRACE_VIEW, "transparency-trace-image");
        transparencyTraceView.getCalculationsSet().addCalculation(3, "mix-maps");

        setBaseMapCalculation = new SetBaseMap("resized-webcam");

        frameViewByName = new HashMap<>();
        frameViewByName.put(CAMERA_VIEW, cameraView);
        frameViewByName.put(TRANSPARENCY_TRACE_VIEW, transparencyTraceView);

        currentSet = defaultSet.merge(cameraView.getCalculationsSet())
                .merge(transparencyTraceView.getCalculationsSet());

        calculationsManager.setCalculationsSet(currentSet);

        magicPaintTraceView = new FrameView(MAGIC_PAINT_TRACE_VIEW, "painted-image-M&C1");
        magicPaintTraceView.getCalculationsSet().addCalculation(4, "paint-using-magnitude-and-colors");

        frameViewByName.put(MAGIC_PAINT_TRACE_VIEW, magicPaintTraceView);

        contrastView = new FrameView(CONTRAST_VIEW, "magnitude-difference-base");
        contrastView.getCalculationsSet().addCalculation(3, "magnitude-difference-with-base");

        frameViewByName.put(CONTRAST_VIEW, contrastView);

        contrastTraceView = new FrameView(CONTRAST_TRACE_VIEW, "painted-image-M&C2");
        contrastTraceView.getCalculationsSet().addCalculation(3, "paint-using-magnitude-and-colors-contrasted");

        frameViewByName.put(CONTRAST_TRACE_VIEW, contrastTraceView);

        mainDesktopPane = new JDesktopPane();
        mainDesktopPane.setPreferredSize(new Dimension(desktopWidth, desktopHeight));

        panel0 = new ImagePanel("Panel 0");
        mainDesktopPane.add(panel0);
        panel0.setLocation(0, 0);
        panel1 = new ImagePanel("Panel 1");
        mainDesktopPane.add(panel1);
        panel1.setLocation(650, 0);

        allPanelViews[0] = panel0;
        allPanelViews[1] = panel1;

        currentViews[0] = TRANSPARENCY_TRACE_VIEW;
        currentViews[1] = CAMERA_VIEW;

        panel0.setTitle(TRANSPARENCY_TRACE_VIEW);
        panel1.setTitle(CAMERA_VIEW);

        panel1.setFocusable(true);
        panel0.setFocusable(true);

        panel0.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
            }

            @Override
            public void focusGained(FocusEvent e) {
                currentTargetedView = 0;
                System.out.println("view 0");
            }
        });

        panel1.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {

            }

            @Override
            public void focusGained(FocusEvent e) {
                currentTargetedView = 1;
                System.out.println("view 1");
            }
        });
        // cameraPanel.setDecoration(5);

        this.getContentPane().add("North", mainDesktopPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                // System.out.println("typed " + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // System.out.println("released " + e.getKeyChar());

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // System.out.println("pressed " + e.getKeyChar());
            }
        };

        addKeyListener(keyAdapter);
    }

    public ImagePanel getCameraPanel() {
        return panel0;
    }

    public ImagePanel getTracePanel() {
        return panel1;
    }

    private void setBaseSheet() {
        try {
            setBaseMapCalculation.calculate(calculatedMaps);
            trace("info", "Base sheet stored");
        } catch (Exception e) {
            trace("error", "Error storing base sheet stored", e);
        }
    }

    private void trace(String level, String message) {
        System.out.println("[" + level + "] " + message);
    }

    private void trace(String level, String message, Exception ex) {
        System.out.println("[" + level + "] " + message + " -> " + ex.getStackTrace());
    }

    public Webcam getWebcam() {
        return webcam;
    }

    protected ImageIcon createImageIconFromAbsolutePath(String path, String description) {
        return new ImageIcon(path, description);
    }

}

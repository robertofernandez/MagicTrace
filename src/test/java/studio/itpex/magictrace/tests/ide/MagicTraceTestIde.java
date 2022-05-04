package studio.itpex.magictrace.tests.ide;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;

import org.apache.log4j.PropertyConfigurator;

import com.github.sarxos.webcam.Webcam;

import studio.itpex.magictrace.tests.ide.panels.ImagePanel;

public class MagicTraceTestIde extends JFrame {
    static final int desktopWidth = 1200;
    static final int desktopHeight = 680;
    private JDesktopPane mainDesktopPane;
    private static final long serialVersionUID = 6733330978640433656L;
    private ImagePanel cameraPanel;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected static void createAndShowGUI() {
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
                    BufferedImage image = webcam.getImage();
                    System.out.println("Image retrieved");
                    ide.getCameraPanel().setImage(image);
                    ide.getCameraPanel().updateImage();
                    System.out.println("Image updated");

                    for (int i = 0; i < 190; i++) {
                        System.out.println("sleeping " + i);
                        Thread.sleep(200);
                        System.out.println("Retrieving image");
                        image = webcam.getImage();
                        System.out.println("Image retrieved");
                        ide.getCameraPanel().setImage(image);
                        ide.getCameraPanel().updateImage();
                        System.out.println("Image updated");
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

        cameraPanel = new ImagePanel("Trace Area");
        mainDesktopPane.add(cameraPanel);

        this.getContentPane().add("North", mainDesktopPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public ImagePanel getCameraPanel() {
        return cameraPanel;
    }
}

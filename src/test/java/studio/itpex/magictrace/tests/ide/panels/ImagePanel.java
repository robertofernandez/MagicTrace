/**
 * @author Roberto G. Fernandez
 * @version 0.02
 *
 * Changes history:
 *
 * Author                       Date              Description
 * -----------------------      ----------        ---------------------------------------
 * Roberto G. Fernandez         24/08/2007        Creation
 * Roberto G. Fernandez         18/07/2019        Renamed class and fields
 *
 */

package studio.itpex.magictrace.tests.ide.panels;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.img.colors.map.GrayscaleMap;

/**
 * 
 * @author Roberto G. Fernandez
 */
public class ImagePanel extends JInternalFrame {
    private static final long serialVersionUID = -7565056578582724726L;
    private BufferedImage image;
    private int imageHeight;
    private int imageWidth;
    private int[] map;
    private int[] red;
    private int[] green;
    private int[] blue;
    private int[] alpha;
    private int id;
    private int decoration = 1;

    /**
     * 
     * @param input
     *            Image file.
     * @param parentIde
     *            Corresponding window.
     */
    public ImagePanel(String name) {
        super(name, true, true, true, true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setSize(600, 600);
        this.setVisible(true);
    }

    public ImagePanel(ColorMap colorMap, String title) {
        super(title, true, true, true, true);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setMap(colorMap);
    }

    public void setMap(ColorMap colorMap) {
        if(colorMap == null) {
            return;
        }
        imageWidth = colorMap.getWidth();
        imageHeight = colorMap.getHeight();
        setRed(colorMap.getRed());
        setGreen(colorMap.getGreen());
        setBlue(colorMap.getBlue());
        fillAlpha();
        recompose();
        updateImage();
        this.init(image);
    }

    public ImagePanel(GrayscaleMap map, String title) {
        super(title, true, true, true, true);
        imageWidth = map.getWidth();
        imageHeight = map.getHeight();
        setRed(map.getValues());
        setGreen(map.getValues());
        setBlue(map.getValues());
        fillAlpha();
        recompose();
        updateImage();
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.init(image);
    }

    private void fillAlpha() {
        alpha = new int[imageWidth * imageHeight];
        for (int i = 0; i < alpha.length; i++) {
            alpha[i] = 255;
        }
    }

    public void setComponents(int[] red, int[] green, int[] blue, int[] alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    private void init(BufferedImage inputImage) {
        image = inputImage;
        imageWidth = image.getWidth(this);
        imageHeight = image.getHeight();
        this.setVisible(true);
        int insetTop = this.getInsets().top;
        int insetLeft = this.getInsets().left;
        this.setSize(2 * insetLeft + imageWidth, 2 * insetTop + imageHeight + 25);
        map = new int[imageWidth * imageHeight];
        // The source image is transformed into a numerical representation
        // corresponding to its pixels, so they can be manipulated.
        // This must be placed in a try-catch block, because we have to be
        // prepared to pick up the exceptions of type "InterrruptedException"
        // that the grabPixels () method can throw
        try {
            // A PixelGrabber object is instantiated, passing as a parameter the
            // array of pixels where we want to save the numerical
            // representation of the image that we are going to manipulate
            PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, imageWidth, imageHeight, map, 0, imageWidth);
            //FIXME check why this happens
//            if (!(pixelGrabber.grabPixels() && ((pixelGrabber.getStatus() & ImageObserver.ALLBITS) != 0))) {
//                throw new Exception("Not all bits loaded");
//            }
        } catch (Exception e) {
            System.out.println(e);
        }
        getRootPane().setWindowDecorationStyle(decoration);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, this.getInsets().left, this.getInsets().top + 25, this);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method to update image based on an update of its map.
     */
    public void updateImage() {
        // Create a buffered image from the source image with a format that's
        // compatible with the screen
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
        // If the source image has no alpha info use Transparency.OPAQUE instead
        Image updatedImage = this.createImage(new MemoryImageSource(imageWidth, imageHeight, map, 0, imageWidth));
        image = graphicsConfiguration.createCompatibleImage(imageWidth, imageHeight, Transparency.BITMASK);
        // Copy image to buffered image
        Graphics graphics = image.createGraphics();
        // Paint the image onto the buffered image
        graphics.drawImage(updatedImage, 0, 0, null);
        graphics.dispose();

        this.setSize(2 * this.getInsets().left + imageWidth, 2 * this.getInsets().top + imageHeight + 25);
    }

    public int[] getAlpha() {
        if (alpha == null)
            decompose();
        return alpha;
    }

    public void setAlpha(int[] alpha) {
        this.alpha = alpha;
    }

    public int[] getBlue() {
        if (blue == null)
            decompose();
        return blue;
    }

    public void setBlue(int[] blue) {
        this.blue = blue;
    }

    public int[] getGreen() {
        if (green == null)
            decompose();
        return green;
    }

    public void setGreen(int[] green) {
        this.green = green;
    }

    public int[] getRed() {
        if (red == null)
            decompose();
        return red;
    }

    public void setRed(int[] red) {
        this.red = red;
    }

    /**
     * Decomposes the image into RGB + alpha components.
     * 
     */
    public void decompose() {
        if (this.map == null)
            return;
        alpha = new int[imageWidth * imageHeight];
        red = new int[imageWidth * imageHeight];
        green = new int[imageWidth * imageHeight];
        blue = new int[imageWidth * imageHeight];

        for (int i = 0; i < imageWidth * imageHeight; i++) {
            blue[i] = map[i] & 0xFF;
            // red[i] = (map[i] >> 8) & 0xFF;
            // green[i] = (map[i] >> 16) & 0xFF;
            green[i] = (map[i] >> 8) & 0xFF;
            red[i] = (map[i] >> 16) & 0xFF;
            alpha[i] = (map[i] >> 24) & 0xFF;
        }
    }

    public void recompose() {
        if (alpha == null)
            return;
        if (map == null) {
            map = new int[imageWidth * imageHeight];
        }
        for (int i = 0; i < imageWidth * imageHeight; i++) {
            // map[i] = blue[i] | (red[i] << 8) | (green[i] << 16) | (alpha[i]
            // << 24);
            map[i] = blue[i] | (green[i] << 8) | (red[i] << 16) | (alpha[i] << 24);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public ColorMap getColorMap() throws Exception {
        decompose();
        ColorMap output = new ColorMap(image.getWidth(), image.getHeight());
        output.setRed(red);
        output.setBlue(blue);
        output.setGreen(green);
        return output;
    }

    public void setImage(BufferedImage inputImage) {
        init(inputImage);
    }
    
    public void setDecoration(int decoration) {
        this.decoration = decoration;
    }
}

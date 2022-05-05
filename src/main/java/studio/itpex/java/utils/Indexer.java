package studio.itpex.java.utils;

/**
 * Utility to index in two dimensions a matrix represented by an array.
 * 
 * @author Roberto G. Fernandez
 * 
 */
public class Indexer {
    private int[] indexedArray;
    private int width;
    private int height;

    public Indexer(int[] input, int width, int height) throws Exception {
        super();
        this.indexedArray = input;
        this.height = height;
        this.width = width;
        if (indexedArray.length < width * height) {
            throw new Exception("" + indexedArray.length + " < " + width + " * " + height);
        }
    }

    public int get(int x, int y) {
        int index = y * width + x;
        if (index > indexedArray.length - 1 || index < 0) {
            String xAndY = "" + x + ", " + y;
            String widthAndHeight = "" + width + ", " + height;
            System.out.println("w, h -> (" + widthAndHeight + "; x,y -> (" + xAndY + "); index = " + index
                    + "; limit = " + (indexedArray.length - 1));
            return 0;
        }
        return (indexedArray[index]);
    }

    public int getMaxSurounding(int x, int y) {
        int max = get(x, y);
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i > 0 && i < width && j > 0 && j < height) {
                    int value = get(i, j);
                    if (value > max) {
                        max = value;
                    }
                }
            }
        }
        return max;
    }

    public void set(int value, int x, int y) {
        int index = y * width + x;
        if (index > indexedArray.length - 1 || index < 0) {
            System.out.println((new Integer(x)).toString() + ", " + (new Integer(y)).toString());
            System.out.println((new Integer(width)).toString() + ", " + (new Integer(height)).toString());
            return;
        }
        indexedArray[index] = value;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

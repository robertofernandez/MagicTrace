package studio.itpex.images.zones;

/**
 * It consists of a descriptor of an area inside an external image. The image is
 * not referenced from the class, and any operation related to it should be
 * performed by external classes.
 * 
 * @author Roberto G. Fernandez
 *
 */
public class RectangularImageArea implements IntegerRectangularZone {
    protected final int x;
    protected final int y;
    protected final int width;
    protected final int height;

    public RectangularImageArea(int x, int y, int width, int height) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public RectangularImageArea(RectangularImageZone zone) {
        this(zone.getX(), zone.getY(), zone.getWidth(), zone.getHeight());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getId() {
        return "RIA:" + x + "_" + y + width + "_" + height;
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + "><" + getMaxX() + ", " + getMaxY() + ">";
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMaxY() {
        return y + height;
    }

    public int distance(RectangularImageArea area) {
        int horizontalDistance = 0;
        int verticalDistance = 0;
        if (x < area.x) {
            horizontalDistance = area.x - (x + width);
        } else {
            horizontalDistance = x - (area.x + area.width);
        }
        if (y < area.y) {
            verticalDistance = area.y - (y + height);
        } else {
            verticalDistance = y - (area.y + area.height);
        }
        return Math.max(horizontalDistance, verticalDistance);
    }

}

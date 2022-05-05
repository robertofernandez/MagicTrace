package studio.itpex.images.zones;

import java.util.ArrayList;
import java.util.HashMap;

import studio.itpex.images.utils.ImageMathUtils;


public class RectangularImageZone implements IntegerRectangularZone {
    private int x;
    private int y;
    private int width;
    private int height;
    private RectangularImageZone parent;
    private ArrayList<RectangularImageZone> children;
    private HashMap<String, RectangularZonesBlock> childrenBlocks;

    public RectangularImageZone(int x, int y, int width, int height) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        children = new ArrayList<>();
        childrenBlocks = new HashMap<>();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setParent(RectangularImageZone parent) {
        this.parent = parent;
    }

    public RectangularImageZone getParent() {
        return parent;
    }

    public boolean contains(RectangularImageZone anotherRectangle) {
        return ImageMathUtils.contains(this, anotherRectangle);
    }

    public ArrayList<RectangularImageZone> getChildren() {
        return children;
    }

    public void addChild(RectangularImageZone child) {
        children.add(child);
    }

    public int getMaxX() {
        return x + width;
    }

    public int getMaxY() {
        return y + height;
    }

    // TODO reuse from static function
    public boolean overlapsX(IntegerRectangularZone rectangle) {
        return (x + width >= rectangle.getX() && x + width <= rectangle.getX() + rectangle.getWidth())
                || (rectangle.getX() + rectangle.getWidth() >= x
                        && rectangle.getX() + rectangle.getWidth() <= x + width);
    }

    // TODO reuse from static function
    public boolean overlapsY(IntegerRectangularZone rectangle) {
        return (y + height >= rectangle.getY() && y + height <= rectangle.getY() + rectangle.getHeight())
                || (rectangle.getY() + rectangle.getHeight() >= y
                        && rectangle.getY() + rectangle.getHeight() <= y + height);
    }

    public static boolean overlapsX(IntegerRectangularZone rectangle1, IntegerRectangularZone rectangle2) {
        return (rectangle1.getX() + rectangle1.getWidth() >= rectangle2.getX()
                && rectangle1.getX() + rectangle1.getWidth() <= rectangle2.getX() + rectangle2.getWidth())
                || (rectangle2.getX() + rectangle2.getWidth() >= rectangle1.getX()
                        && rectangle2.getX() + rectangle2.getWidth() <= rectangle1.getX() + rectangle1.getWidth());
    }

    public static boolean overlapsY(IntegerRectangularZone rectangle1, IntegerRectangularZone rectangle2) {
        return (rectangle1.getY() + rectangle1.getHeight() >= rectangle2.getY()
                && rectangle1.getY() + rectangle1.getHeight() <= rectangle2.getY() + rectangle2.getHeight())
                || (rectangle2.getY() + rectangle2.getHeight() >= rectangle1.getY()
                        && rectangle2.getY() + rectangle2.getHeight() <= rectangle1.getY() + rectangle1.getHeight());
    }

    @Override
    public String toString() {
        return "<" + x + ", " + y + "><" + getMaxX() + ", " + getMaxY() + ">";
    }

    public String getId() {
        return "RIZ:" + x + "_" + y + width + "_" + height;
    }

    public HashMap<String, RectangularZonesBlock> getChildrenBlocks() {
        return childrenBlocks;
    }

    public int distance(RectangularImageZone area) {
        return Math.max(horizontalDistance(area), verticalDistance(area));
    }

    public int horizontalDistance(RectangularImageZone area) {
        int horizontalDistance = 0;
        if (x < area.x) {
            horizontalDistance = area.x - (x + width);
        } else {
            horizontalDistance = x - (area.x + area.width);
        }
        return horizontalDistance;
    }

    public int verticalDistance(RectangularImageZone area) {
        int verticalDistance = 0;
        if (y < area.y) {
            verticalDistance = area.y - (y + height);
        } else {
            verticalDistance = y - (area.y + area.height);
        }
        return verticalDistance;
    }

    public double getSurface() {
        return getWidth() * getHeight();
    }

    public void removeAllChildren() {
        children = new ArrayList<>();
        childrenBlocks = new HashMap<>();
    }

    public RectangularImageArea asArea() {
        return new RectangularImageArea(this);
    }

}

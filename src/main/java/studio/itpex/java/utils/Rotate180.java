package studio.itpex.java.utils;

public class Rotate180 implements CoordinatesOperator {
    
    private int width;
    private int height;

    public Rotate180(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Coordinates operate(Coordinates input) {
        int x1 = width - input.getX();
        int y1 = height - input.getY();
        return new Coordinates(x1, y1);
    }

}

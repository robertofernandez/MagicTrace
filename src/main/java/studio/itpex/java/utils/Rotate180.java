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
        int x1 = width - 1 - input.getX();
        int y1 = height - 1 - input.getY();
        return new Coordinates(x1, y1);
    }

}

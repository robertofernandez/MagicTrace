package studio.itpex.java.utils;

import ar.com.sodhium.commons.indexes.Indexer;

public class OperatedIndexer extends Indexer {
    private CoordinatesOperator operator;

    public OperatedIndexer(Indexer baseIndexer, CoordinatesOperator operator) throws Exception {
        super(baseIndexer);
        this.operator = operator;
    }
    
    public OperatedIndexer(int[] input, int width, int height, CoordinatesOperator operator) throws Exception {
        super(input, width, height);
        this.operator = operator;
    }

    public OperatedIndexer(int[] input, int width, int height) throws Exception {
        super(input, width, height);
    }

    @Override
    public int get(int x, int y) {
        Coordinates input = new Coordinates(x, y);
        Coordinates coordinates = operator.operate(input);
        return super.get(coordinates.getX(), coordinates.getY());
    }

    @Override
    public void set(int value, int x, int y) {
        Coordinates input = new Coordinates(x, y);
        Coordinates coordinates = operator.operate(input);
        super.set(value, coordinates.getX(), coordinates.getY());
    }

}

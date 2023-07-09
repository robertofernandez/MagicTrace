package studio.itpex.images.mapping;

import ar.com.sodhium.commons.img.colors.map.ColorMap;
import ar.com.sodhium.commons.indexes.Indexer;
import studio.itpex.java.utils.CoordinatesOperator;
import studio.itpex.java.utils.OperatedIndexer;

public class OperatedColorMap extends ColorMap {
    private CoordinatesOperator coordinatesOperator;

    public OperatedColorMap(int width, int height, CoordinatesOperator coordinatesOperator) {
        super(width, height);
        this.coordinatesOperator = coordinatesOperator;
    }

    @Override
    public Indexer getRedIndex() {
        try {
            return new OperatedIndexer(super.getRedIndex(), coordinatesOperator);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getRedIndex();
        }
    }

    @Override
    public Indexer getBlueIndex() {
        try {
            return new OperatedIndexer(super.getBlueIndex(), coordinatesOperator);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getBlueIndex();
        }
    }

    @Override
    public Indexer getGreenIndex() {
        try {
            return new OperatedIndexer(super.getGreenIndex(), coordinatesOperator);
        } catch (Exception e) {
            e.printStackTrace();
            return super.getGreenIndex();
        }
    }

}

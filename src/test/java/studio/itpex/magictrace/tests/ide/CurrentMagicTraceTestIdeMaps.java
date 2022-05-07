package studio.itpex.magictrace.tests.ide;

import studio.itpex.images.mapping.ColorMap;

public class CurrentMagicTraceTestIdeMaps {

    private ColorMap cameraMap;
    private ColorMap mixedMap;
    private ColorMap resultMap;

    public ColorMap getCameraMap() {
        return cameraMap;
    }

    public ColorMap getMixedMap() {
        return mixedMap;
    }

    public void setCameraMap(ColorMap cameraMap) {
        this.cameraMap = cameraMap;
    }

    public void setMixedMap(ColorMap mixedMap) {
        this.mixedMap = mixedMap;
    }

    public ColorMap getResultMap() {
        return resultMap;
    }

    public void setResultMap(ColorMap resultMap) {
        this.resultMap = resultMap;
    }
}

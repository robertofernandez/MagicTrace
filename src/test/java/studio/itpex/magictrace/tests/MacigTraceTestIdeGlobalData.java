package studio.itpex.magictrace.tests;

import studio.itpex.images.mapping.ColorMap;

public class MacigTraceTestIdeGlobalData {
    private static MacigTraceTestIdeGlobalData instance;
    static {
        try {
            instance = new MacigTraceTestIdeGlobalData();
        } catch (Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }
    }

    private ColorMap currentMap;

    public static MacigTraceTestIdeGlobalData getInstance() {
        return instance;
    }

    public ColorMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(ColorMap currentMap) {
        this.currentMap = currentMap;
    }
}

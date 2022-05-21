package studio.itpex.magictrace.tests.ide;

import studio.itpex.magictrace.calculations.PrioritizedCalculationsSet;

public class FrameView {
    private PrioritizedCalculationsSet calculationsSet;
    private String title;
    private String mapNameToShow;

    public FrameView(String title, String mapNameToShow) {
        this.title = title;
        this.mapNameToShow = mapNameToShow;
        calculationsSet = new PrioritizedCalculationsSet();
    }

    public PrioritizedCalculationsSet getCalculationsSet() {
        return calculationsSet;
    }

    public void setCalculationsSet(PrioritizedCalculationsSet calculationsSet) {
        this.calculationsSet = calculationsSet;
    }

    public String getTitle() {
        return title;
    }

    public String getMapNameToShow() {
        return mapNameToShow;
    }

    @Override
    public String toString() {
        return "FrameView [title=" + title + ", mapNameToShow=" + mapNameToShow + "]";
    }
}

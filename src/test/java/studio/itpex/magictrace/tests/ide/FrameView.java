package studio.itpex.magictrace.tests.ide;

public class FrameView {
    private PrioritizedCalculationsSet calculationsSet;
    private String title;

    public FrameView(String title) {
        this.title = title;
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
}

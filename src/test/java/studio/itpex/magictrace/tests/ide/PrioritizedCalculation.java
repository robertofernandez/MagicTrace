package studio.itpex.magictrace.tests.ide;

public class PrioritizedCalculation implements Comparable<PrioritizedCalculation> {

    private Integer priority;

    public PrioritizedCalculation(Integer priority) {
        this.priority = priority;
    };

    @Override
    public int compareTo(PrioritizedCalculation o) {
        return priority.compareTo(o.priority);
    }

}

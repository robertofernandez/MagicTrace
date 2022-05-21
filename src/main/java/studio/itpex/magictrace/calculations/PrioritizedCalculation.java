package studio.itpex.magictrace.calculations;

public class PrioritizedCalculation implements Comparable<PrioritizedCalculation> {
    private Integer priority;
    private String name;

    public PrioritizedCalculation(Integer priority, String name) {
        this.priority = priority;
        this.name = name;
    };

    @Override
    public int compareTo(PrioritizedCalculation o) {
        return priority.compareTo(o.priority);
    }

    public String getName() {
        return name;
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "[priority=" + priority + ", name=" + name + "]";
    }

}

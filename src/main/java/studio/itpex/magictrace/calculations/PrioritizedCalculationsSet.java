package studio.itpex.magictrace.calculations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PrioritizedCalculationsSet {

    private ArrayList<PrioritizedCalculation> calculations;

    public PrioritizedCalculationsSet() {
        calculations = new ArrayList<>();
    }

    public ArrayList<PrioritizedCalculation> getCalculations() {
        return calculations;
    }

    public ArrayList<PrioritizedCalculation> getSortedCalculations() {
        Collections.sort(calculations);
        return calculations;
    }

    public void addCalculation(Integer priority, String name) {
        calculations.add(new PrioritizedCalculation(priority, name));
    }

    public PrioritizedCalculationsSet merge(PrioritizedCalculationsSet anotherSet) {
        HashMap<String, PrioritizedCalculation> mergedCalculations = new HashMap<>();

        for (PrioritizedCalculation prioritizedCalculation : calculations) {
            mergedCalculations.put(prioritizedCalculation.getName(), prioritizedCalculation);
        }
        for (PrioritizedCalculation prioritizedCalculation2 : anotherSet.calculations) {
            if (mergedCalculations.containsKey(prioritizedCalculation2.getName())) {
                if (mergedCalculations.get(prioritizedCalculation2.getName()).getPriority() > prioritizedCalculation2
                        .getPriority()) {
                    mergedCalculations.put(prioritizedCalculation2.getName(), prioritizedCalculation2);
                }
            } else {
                mergedCalculations.put(prioritizedCalculation2.getName(), prioritizedCalculation2);
            }
        }
        PrioritizedCalculationsSet output = new PrioritizedCalculationsSet();
        output.getCalculations().addAll(mergedCalculations.values());
        return output;
    }

    @Override
    public String toString() {
        return calculations.toString();
    }

}

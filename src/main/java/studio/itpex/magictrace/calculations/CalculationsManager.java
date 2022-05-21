package studio.itpex.magictrace.calculations;

import java.util.ArrayList;
import java.util.HashMap;

public class CalculationsManager {

    private HashMap<String, FrameCalculation> calculationsByName;
    private PrioritizedCalculationsSet calculationsSet;

    public CalculationsManager() {
        calculationsByName = new HashMap<>();
    }
    
    public void addCalculation(String name, FrameCalculation calculation) {
        calculationsByName.put(name, calculation);
    }

    public HashMap<String, FrameCalculation> getCalculationsByName() {
        return calculationsByName;
    }

    public void setCalculationsByName(HashMap<String, FrameCalculation> calculationsByName) {
        this.calculationsByName = calculationsByName;
    }

    public PrioritizedCalculationsSet getCalculationsSet() {
        return calculationsSet;
    }

    public void setCalculationsSet(PrioritizedCalculationsSet calculationsSet) {
        this.calculationsSet = calculationsSet;
    }

    public void executeCalculations(CalculatedMaps maps) {
        if(calculationsSet == null) {
            return;
        }
        ArrayList<PrioritizedCalculation> sortedCalculations = calculationsSet.getSortedCalculations();
        for (PrioritizedCalculation prioritizedCalculation : sortedCalculations) {
            try {
                calculationsByName.get(prioritizedCalculation.getName()).calculate(maps);
            } catch (Exception e) {
                // FIXME Auto-generated catch block
                System.out.println("ERROR calculating " + prioritizedCalculation.getName());
                e.printStackTrace();
            }
        }
    }
}

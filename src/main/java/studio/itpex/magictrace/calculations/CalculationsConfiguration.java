package studio.itpex.magictrace.calculations;

import java.util.HashMap;

public class CalculationsConfiguration {
    private HashMap<String, Double> numericConfigurations;
    private HashMap<String, String> stringConfigurations;

    public CalculationsConfiguration() {
        numericConfigurations = new HashMap<>();
        stringConfigurations = new HashMap<>();
    }

    public Double getNumber(String name) {
        return numericConfigurations.get(name);
    }

    public String getText(String name) {
        return stringConfigurations.get(name);
    }

    public void setNumber(String name, Double value) {
        numericConfigurations.put(name, value);
    }

    public void setText(String name, String value) {
        stringConfigurations.put(name, value);
    }

}

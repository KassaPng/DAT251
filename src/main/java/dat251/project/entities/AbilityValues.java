package dat251.project.entities;


import java.util.HashMap;
import java.util.Map;

public class AbilityValues {
    private Map<String, Double> abilities;


    public AbilityValues() {
        this.abilities = new HashMap<>();
    }

    public void setAbilities(String ab, int val) throws IllegalArgumentException {
        if(val < 0 || val > 10) {
            throw new IllegalArgumentException("Val must be between 0 and 10");
        }
        if(!abilities.containsKey(ab)) {
            throw new IllegalArgumentException("ability-name '" + ab + "' does not exist");
        }
        abilities.put(ab, (double) val);
    }

    public Map<String, Double> getAbilityValues() {
        return abilities;
    }
}

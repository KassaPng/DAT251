package dat251.project.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Abilities {
    private ArrayList<String> abilities;
    private Map<String, Double> abilityMap;

    /**
     *  Creates the list of abilities that should be used for a course.
     * @param general True if general abilities should be used
     * @param programming True if programming abilities should be used
     */
    public Abilities(boolean general, boolean programming) {
        abilities = new ArrayList<>();
        if(general) {
            abilities.addAll(Arrays.asList(getGeneralAbilities()));
        }
        if(programming) {
            abilities.addAll(Arrays.asList(getProgrammingAbilities()));
        }
        for(int i = 0; i < abilities.size(); i++) {
            abilityMap.put(abilities.get(i), 0.0);
        }

    }


    //abilities for programming courses
    public String[] getProgrammingAbilities() {
        String[] abList = new String[]{
                "Algorithms",
                "Databases",
                "Back-end",
                "Front-end",
                "Testing",
        };
        return abList;
    }

    //general abilities
    public String[] getGeneralAbilities() {
        String[] abList = new String[]{
                "Ambition",
                "Work-rate",
                "Knowledge",
        };
        return abList;
    }

    public void setAbility(String ability, int val) throws NumberFormatException {
        if(val < 0 || val > 10) {
            throw new NumberFormatException("val must be between 0 and 10");
        }
        abilityMap.put(ability, (double) val);
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public Map<String, Double> getAbilityMap() {
        return abilityMap;
    }

}

package dat251.project.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Abilities {
    private ArrayList<String> abilities;

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

    public ArrayList<String> getAbilities() {
        return abilities;
    }

}

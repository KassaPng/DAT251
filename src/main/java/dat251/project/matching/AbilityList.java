package dat251.project.matching;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to generate the abilities that should be available within a group
 */
public class AbilityList {

    private AbilityList() {}

    /**
     *  Creates the list of abilities that should be used for a course.
     * @param general True if general abilities should be used
     * @param programming True if programming abilities should be used
     */
    public static List<String> createAbilityList(boolean general, boolean programming) {
        List<String> abilities = new ArrayList<>();
        if(general) {
            abilities.addAll(Arrays.asList(getGeneralAbilities()));
        }
        if(programming) {
            abilities.addAll(Arrays.asList(getProgrammingAbilities()));
        }
        return abilities;
    }

    //abilities for programming courses
    public static String[] getProgrammingAbilities() {
        return new String[]{
                "Algorithms",
                "Databases",
                "Back-end",
                "Front-end",
                "Testing",
        };
    }

    //general abilities
    public static String[] getGeneralAbilities() {
        return new String[]{
                "Ambition",
                "Work-rate",
                "Knowledge",
        };
    }














 //   private ArrayList<String> abilities;

    /**
     *  Creates the list of abilities that should be used for a course.
     * @param general True if general abilities should be used
     * @param programming True if programming abilities should be used
     */
 /*   public AbilityList(boolean general, boolean programming) {
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
        return new String[]{
                "Algorithms",
                "Databases",
                "Back-end",
                "Front-end",
                "Testing",
        };
    }

    //general abilities
    public String[] getGeneralAbilities() {
        return new String[]{
                "Ambition",
                "Work-rate",
                "Knowledge",
        };
    }

    public List<String> getListOfAbilities() {
        return abilities;
    }
*/
}

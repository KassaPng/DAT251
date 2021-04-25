package dat251.project.matching;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to generate the abilities that should be available within a group
 */
@Entity
public class AbilityList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> abilities;

    /**
     *  Creates the list of abilities that should be used for a course.
     * @param general True if general abilities should be used
     * @param programming True if programming abilities should be used
     */
    public AbilityList(boolean general, boolean programming) {
        abilities = new ArrayList<>();
        if(general) {
            abilities.addAll(Arrays.asList(getGeneralAbilities()));
        }
        if(programming) {
            abilities.addAll(Arrays.asList(getProgrammingAbilities()));
        }

    }

    public AbilityList() {

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

    public List<String> getListOfAbilities() {
        return abilities;
    }

}

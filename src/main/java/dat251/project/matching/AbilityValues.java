package dat251.project.matching;


import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "abilities")
public class AbilityValues {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ElementCollection
    @CollectionTable(name = "ability_value_mapping",
            joinColumns = {@JoinColumn(name = "ability_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "ability_name")
    @Column(name = "value")
    private Map<String, Double> abilities;


    public AbilityValues() {
        this.abilities = new HashMap<>();
    }

    public void setAbilities(String ab, int val) throws IllegalArgumentException {
        if(val < 0 || val > 10) {
            throw new IllegalArgumentException("Val must be between 0 and 10");
        }
        abilities.put(ab, (double) val);
    }

    public Map<String, Double> getAbilityValues() {
        return abilities;
    }
}

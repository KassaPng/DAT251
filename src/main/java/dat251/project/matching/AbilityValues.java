package dat251.project.matching;


import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, Double> getAbilities() {
        return abilities;
    }

    public void setAbilities(Map<String, Double> abilities) {
        this.abilities = abilities;
    }

    private static final double ZERO = 0.0;

    public void initializeAbilities(List<String> abilityNames) {
        for (String name : abilityNames) {
            this.abilities.put(name, ZERO);
        }
    }

    public void setAbilities(String abilityName, int value) throws IllegalArgumentException {
        if(value < 0 || value > 10) {
            throw new IllegalArgumentException("Value must be between 0 and 10");
        }
        abilities.put(abilityName, (double) value);
    }
}

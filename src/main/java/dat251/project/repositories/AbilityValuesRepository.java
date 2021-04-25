package dat251.project.repositories;

import dat251.project.matching.AbilityValues;
import org.springframework.data.repository.CrudRepository;

public interface AbilityValuesRepository extends CrudRepository<AbilityValues, String> {

    AbilityValues findAbilityValuesById(long id);

}

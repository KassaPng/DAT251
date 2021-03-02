package repositories;

import entities.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUserName(String userName);

    User findById(long id);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);
}

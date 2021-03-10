package dat251.project.repositories;

import dat251.project.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {

    User findByUserName(String userName);

    User findById(long id);

  //  List<User> findByTag(Tag tag);

 //   List<User> findByFirstName(String firstName);

 //   List<User> findByLastName(String lastName);
}

package repositories;

import entities.User;
import entities.Group;
import entities.Tag;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GroupRepository extends CrudRepository<Group, String> {

    List<Group> findByUserName(String userName); // All groups that a user is participating in

    List<Group> findByGroupName(String groupName); // return a list if the group name is not unique --> change to a single Group if it is

    List<Group> findByTag(Tag tag); // All groups containing specified tag




}

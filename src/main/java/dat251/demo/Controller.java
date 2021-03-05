package dat251.demo;

import entities.Group;
import entities.User;
import org.apache.tomcat.jni.Poll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repositories.GroupRepository;
import repositories.UserRepository;

import java.util.Map;
import java.util.Optional;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    final UserRepository userRepository;
    final GroupRepository groupRepository;

    @Autowired
    public Controller(UserRepository userRepository,
                      GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }


    @RequestMapping("/hello")
    public String index() {
        return "Hello World!";
    }


/*
    USER REQUESTS
*/

    @PostMapping("/users")
    public String createUser(@RequestBody Map<String, String> json) {
        log.info("Attempting to create a new user");
        // Concatenating request parameters with an empty
        // string to prevent null values.
        String userName = "" + json.get("userName");
        String name = "" + json.get("name");
        String password = "" + json.get("password");
        String repeatPassword = "" + json.get("repeatPassword");
        if (password.equals(repeatPassword)) {
            User user = new User(name, userName, password);
            userRepository.save(user);
            return "Successfully created user: " + user.toString();
        } else {
            return "Failed to create user.\nPassword did not match repeat password";
        }
    }

    @GetMapping("/users")
    public @ResponseBody
    Iterable<User> getAllUsers() {
        log.info("Getting all users");
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping("/users/{userName}")
    public @ResponseBody
    User getUser(@PathVariable String userName) {
        log.info("Trying to find user with username: " + userName);
        // This returns a JSON or XML with the users
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            log.info("Failed to retrieve user with userName: " + userName);
            return null;
        } else {
            log.info("Successfully retrieved user " + user.getUserName()
                    + " with ID: " + user.getId());
            return user;
        }
    }

    @PutMapping("/users/{userName}")
    public @ResponseBody User updateUser(@PathVariable String userName,
                                         @RequestBody Map<String, String> json) {
        log.info("Attempting to alter existing user with username: " + userName);
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            log.info("User with username: " + userName + " did not exist in the database");
            return null;
        }
        String newName = "" + json.get("newName");
        String newPassword = "" + json.get("newPassword");
        String repeatedNewPassword = "" + json.get("repeatedNewPassword");
        updateName(user, newName);
        updatePassword(user, newPassword, repeatedNewPassword);
        log.info("Successfully updated the user with ID: " + user.getId());
        return user;
    }

    private void updateName(User user, String newName) {
        if (!newName.isEmpty() && !newName.equals("null")) {
            user.setName(newName);
            userRepository.save(user);
            log.info("Updated user's name");
        }
    }

    private void updatePassword(User user, String newPassword, String repeatedNewPassword) {
        if (!newPassword.isEmpty() && !newPassword.equals("null")) {
            if (newPassword.equals(repeatedNewPassword)) {
                user.setPasswordAsHash(newPassword);
                userRepository.save(user);
                log.info("Updated user's password");
            } else {
                log.info("New password and repeated password did not match");
            }
        }
    }

    @DeleteMapping("/users/{userName}")
    public String deleteUser(@PathVariable String userName) {
        log.info("Attempting to delete a user with username: " + userName);
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            return "Failed to delete user\n" +
                    "The user did not exist in the database";
        } else {
            long userId = user.getId();
            userRepository.delete(user);
            log.info("Successfully deleted the user with ID: " + userId);
            return "User deleted";
        }
    }

/*
    GROUP REQUESTS
*/

    @PostMapping("/groups")
    public String createANewGroup(@RequestBody Map<String, String> json) {
        log.info("Attempting to create a new Group");
        // Concatenating request parameters with an empty
        // string to prevent null values.
        String groupName = "" + json.get("groupName");
        if (groupName.length() >= 3) {
            Group group = new Group(groupName);
            groupRepository.save(group);
            return "Successfully created group: " + group.toString();
        } else {
            return "Failed to create new group." +
                    "\nGroup name must contain at least 3 characters";
        }
    }

    @GetMapping("/groups")
    public @ResponseBody Iterable<Group> getAllGroups() {
        log.info("Getting all registered groups");
        return groupRepository.findAll();
    }

    @GetMapping("/groups/{groupID}")
    public @ResponseBody Group getGroup(@PathVariable long groupID) {
        log.info("Trying to find a specific group with id: " + groupID);
        Group group = groupRepository.findById(groupID);
        if (group == null) {
            log.info("Failed to retrieve group with ID: " + groupID);
            return null;
        } else {
            log.info("Successfully retrieved group " + group.getGroupName()
                    + " with ID: " + group.getId());
            return group;
        }
    }

    @PutMapping("/groups/{groupID}")
    public @ResponseBody Group updateGroup(@PathVariable long groupID,
                                           @RequestBody Map<String, String> json) {
        log.info("Attempting to alter group with ID: " + groupID);
        Group group = groupRepository.findById(groupID);
        if (group == null) {
            log.info("Group with ID: " + groupID + " did not exist in the database");
            return null;
        }
        String newGroupName = "" + json.get("groupName");
        updateGroupName(group, newGroupName);
        log.info("Successfully updated the group information");
        return group;
    }

    private void updateGroupName(Group group, String newGroupName) {
        if (newGroupName.length() > 3 && !newGroupName.equals("null")) {
            String oldGroupName = group.getGroupName();
            group.setGroupName(newGroupName);
            groupRepository.save(group);
            log.info("Updated group name from: " + oldGroupName + " to: " + newGroupName);
        }
    }


    @DeleteMapping("/groups/{groupID}")
    public String deleteGroup(@PathVariable long groupID) {
        log.info("Attempting to delete group with ID: " + groupID);
        Group group = groupRepository.findById(groupID);
        if (group == null) {
            return "Failed to delete group\n" +
                    "The group did not exist in the database";
        } else {
            String groupName = group.getGroupName();
            groupRepository.delete(group);
            log.info("Successfully deleted group with name: " + groupName
                    + " and ID: " + groupID);
            return "Group deleted";
        }
    }

}

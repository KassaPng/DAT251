package dat251.project;

import dat251.project.entities.Group;
import dat251.project.entities.User;
import dat251.project.repositories.GroupRepository;
import dat251.project.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        if (!(userRepository.findByUserName(userName) == null)) {
            log.info("The username was already taken");
            return "Failed to create user.\nUsername was already taken";
        }
        if (password.equals(repeatPassword)) {
            User user = new User(name, userName, password);
            userRepository.save(user);
            log.info("Successfully created user: " + user.getUserName() + " with ID: " + user.getId());
            return "Successfully created user: " + user.toString();
        } else {
            log.info("Failed to create the new user");
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
        if (notExistsInDatabase(user, "User")) {
            return null;
        } else {
            log.info("Successfully retrieved user: " + user.getUserName()
                    + " with ID: " + user.getId());
            return user;
        }
    }

    @PutMapping("/users/{userName}")
    public @ResponseBody User updateUser(@PathVariable String userName,
                                         @RequestBody Map<String, String> json) {
        log.info("Attempting to alter existing user with username: " + userName);
        User user = userRepository.findByUserName(userName);
        if (notExistsInDatabase(user, "User")) {
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
        if (notExistsInDatabase(user, "User")) {
            return "Failed to delete user\n" +
                    "The user did not exist in the database";
        } else {
            removeForeignKeyToGroups(user);
            long userId = user.getId();
            userRepository.delete(user);
            log.info("Successfully deleted the user with ID: " + userId);
            return "User deleted";
        }
    }

    private void removeForeignKeyToGroups(User user) {
        for (Group group : user.getGroups()) {
            group.removeUserFromGroup(user);
            groupRepository.save(group);
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
            log.info("Successfully created group: " + group.getGroupName() + " with ID: " + group.getId());
            return "Successfully created group: " + group.toString();
        } else {
            log.info("Failed to create the new group");
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
        if (notExistsInDatabase(group, "Group")) {
            return null;
        } else {
            log.info("Successfully retrieved group: " + group.getGroupName()
                    + " with ID: " + group.getId());
            return group;
        }
    }

    @PutMapping("/groups/{groupID}")
    public @ResponseBody Group updateGroup(@PathVariable long groupID,
                                           @RequestBody Map<String, String> json) {
        log.info("Attempting to alter group with ID: " + groupID);
        Group group = groupRepository.findById(groupID);
        if (notExistsInDatabase(group, "Group")) {
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
        if (notExistsInDatabase(group, "Group")) {
            return "Failed to delete group\n" +
                    "The group did not exist in the database";
        } else {
            removeForeignKeyToUsers(group);
            String groupName = group.getGroupName();
            groupRepository.delete(group);
            log.info("Successfully deleted group with name: " + groupName
                    + " and ID: " + groupID);
            return "Group deleted";
        }
    }

    private void removeForeignKeyToUsers(Group group) {
        for (User user : group.getMembers()) {
            user.removeGroupFromListOfGroups(group);
            userRepository.save(user);
        }
    }

    @PutMapping("/groups/{groupID}/members")
    public @ResponseBody Group addUserToGroup(@PathVariable long groupID,
                                              @RequestBody Map<String, String> json) {
        log.info("Attempting to add a user to group with ID: " + groupID);
        Group group = groupRepository.findById(groupID);
        String nameOfUser = "" + json.get("userName");
        User user = userRepository.findByUserName(nameOfUser);
        if (notExistsInDatabase(group, "Group")
                || notExistsInDatabase(user, "User")) {
            return null;
        }
        if (group.addUserToGroup(user)) {
            log.info("Successfully made user: " + user.getUserName()
                    + " a member of group with ID: " + group.getId());
            groupRepository.save(group);
            if(!user.addGroupToUsersListOfGroups(group)) {
                log.info("Something went wrong when trying to add group to users list of groups");
            } else {
                userRepository.save(user);
            }
        } else {
            log.info("Failed to add user: " + user.getUserName()
                    + " as a member to group with ID: " + group.getId());
        }
        return group;
    }


    @DeleteMapping("/groups/{groupID}/members")
    public @ResponseBody Group removeUserFromGroup(@PathVariable long groupID,
                                              @RequestBody Map<String, String> json) {
        log.info("Attempting to remove a user from group with ID: " + groupID);
        Group group = groupRepository.findById(groupID);
        String nameOfUser = "" + json.get("userName");
        User user = userRepository.findByUserName(nameOfUser);
        if (notExistsInDatabase(group, "Group")
                || notExistsInDatabase(user, "User")) {
            return null;
        }
        if (group.removeUserFromGroup(user)) {
            log.info("Successfully removed user: " + user.getUserName()
                    + " from list of members of group with ID: " + group.getId());
            groupRepository.save(group);
            if(!user.removeGroupFromListOfGroups(group)) {
                log.info("Something went wrong when trying to remove group from users list of groups");
            } else {
                userRepository.save(user);
            }
        } else {
            log.info("Failed to remove user: " + user.getUserName()
                    + " from the list of members of group with ID: " + group.getId());
        }
        return group;
    }



    private boolean notExistsInDatabase(Object object, String objectName) {
        if (object == null) {
            log.info(objectName + " did not exist in the database");
            return true;
        } else {
            return false;
        }
    }


}

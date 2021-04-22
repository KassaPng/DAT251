package dat251.project.controllers;

import dat251.project.entities.Course;
import dat251.project.entities.Group;
import dat251.project.entities.User;
import dat251.project.repositories.CourseRepository;
import dat251.project.repositories.GroupRepository;
import dat251.project.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    final UserRepository userRepository;
    final GroupRepository groupRepository;
    final CourseRepository courseRepository;

    @Autowired
    public Controller(UserRepository userRepository,
                      GroupRepository groupRepository,
                      CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
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

    private static final String standardGroupDescription = "Nothing here yet." +
            "\nLet others know what this group is all about!";

    @PostMapping("/groups")
    public String createANewGroup(@RequestBody Map<String, String> json) {
        log.info("Attempting to create a new Group");
        // Concatenating request parameters with an empty
        // string to prevent null values.
        String groupName = "" + json.get("groupName");
        String creatorName = "" +json.get("creatorName"); //adds the group creator to the group by default, reduces the amount of HTTP requests needed
        User creator = userRepository.findByUserName(creatorName);
        if (groupName.length() >= 3) {
            Group group = new Group(groupName, standardGroupDescription);
            group.addUserToGroup(creator);
            creator.addGroupToUsersListOfGroups(group);
            groupRepository.save(group);
            userRepository.save(creator);
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
        String newDescription = "" + json.get("description");
        updateGroupName(group, newGroupName);
        updateGroupDescription(group, newDescription);
        log.info("Successfully updated the group information");
        return group;
    }

    private void updateGroupDescription(Group group, String newDescription) {
        if (newDescription.length() <= Group.MAX_GROUP_DESCRIPTION_LENGTH && !newDescription.equals("null")) {
            group.setDescription(newDescription);
            groupRepository.save(group);
            log.info("Updated group description to: " + newDescription);
        }
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


    @GetMapping("/users/{userID}/groups")
    public @ResponseBody List<Group> getUsersGroups(@PathVariable long userID) {
        log.info("Getting all groups the user with ID: {} is a member off", userID);
        User user = userRepository.findById(userID);
        if (notExistsInDatabase(user, "User")) {
            return null;
        }
        return user.getGroups();
    }

/*
    COURSE REQUESTS
*/

    @GetMapping("/courses")
    public @ResponseBody Iterable<Course> getCourses() {
        log.info("Getting all registered courses");
        return courseRepository.findAll();
    }

    @GetMapping("/courses/{courseID}")
    public @ResponseBody Course getCourse(@PathVariable long courseID) {
        log.info("Trying to find a specific course with id: {}", courseID);
        Course course = courseRepository.findById(courseID);
        if (notExistsInDatabase(course, "Course")) {
            return null;
        } else {
            log.info("Successfully retrieved course: {} with ID: {}", course.getName(), course.getId());
            return course;
        }
    }





















    private boolean notExistsInDatabase(Object object, String objectName) {
        if (object == null) {
            log.info("{} did not exist in the database", objectName);
            return true;
        } else {
            return false;
        }
    }


}

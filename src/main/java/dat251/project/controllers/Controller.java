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
        if (userRepository.findByUserName(userName) != null) {
            log.info("The username was already taken");
            return "Failed to create user.\nUsername was already taken";
        }
        if (password.equals(repeatPassword)) {
            User user = new User(name, userName, password);
            userRepository.save(user);
            log.info("Successfully created user: {} with ID: {}", user.getUserName(), user.getId());
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
        log.info("Trying to find user with username: {}", userName);
        // This returns a JSON or XML with the users
        User user = userRepository.findByUserName(userName);
        if (notExistsInDatabase(user, "User")) {
            return null;
        } else {
            log.info("Successfully retrieved user: {} with ID: {}", user.getUserName(), user.getId());
            return user;
        }
    }

    @PutMapping("/users/{userName}")
    public @ResponseBody User updateUser(@PathVariable String userName,
                                         @RequestBody Map<String, String> json) {
        log.info("Attempting to alter existing user with username: {}", userName);
        User user = userRepository.findByUserName(userName);
        if (notExistsInDatabase(user, USER)) {
            return null;
        }
        String newName = "" + json.get("newName");
        String newPassword = "" + json.get("newPassword");
        String repeatedNewPassword = "" + json.get("repeatedNewPassword");
        updateName(user, newName);
        updatePassword(user, newPassword, repeatedNewPassword);
        log.info("Successfully updated the user with ID: {}", user.getId());
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
        log.info("Attempting to delete a user with username: {}", userName);
        User user = userRepository.findByUserName(userName);
        if (notExistsInDatabase(user, USER)) {
            return "Failed to delete user\n" +
                    "The user did not exist in the database";
        } else {
            removeForeignKeys(user);
            long userId = user.getId();
            userRepository.delete(user);
            log.info("Successfully deleted the user with ID: {}", userId);
            return "User deleted";
        }
    }

    private void removeForeignKeys(User user) {
        for (Group group : user.getGroups()) {
            group.removeUserFromGroup(user);
            groupRepository.save(group);
        }
        for (Course course : user.getCourses()) {
            course.removeUser(user);
            courseRepository.save(course);
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
            log.info("Successfully created group: {} with ID: {}", group.getGroupName(), group.getId());
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
        log.info("Trying to find a specific group with id: {}", groupID);
        Group group = groupRepository.findById(groupID);
        if (notExistsInDatabase(group, GROUP)) {
            return null;
        } else {
            log.info("Successfully retrieved group: {} with ID: {}", group.getGroupName(), group.getId());
            return group;
        }
    }

    @PutMapping("/groups/{groupID}")
    public @ResponseBody Group updateGroup(@PathVariable long groupID,
                                           @RequestBody Map<String, String> json) {
        log.info("Attempting to alter group with ID: {}", groupID);
        Group group = groupRepository.findById(groupID);
        if (notExistsInDatabase(group, GROUP)) {
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
            log.info("Updated group description to: {}", newDescription);
        }
    }

    private void updateGroupName(Group group, String newGroupName) {
        if (newGroupName.length() > 3 && !newGroupName.equals("null")) {
            String oldGroupName = group.getGroupName();
            group.setGroupName(newGroupName);
            groupRepository.save(group);
            log.info("Updated group name from: {} to: {}", oldGroupName, newGroupName);
        }
    }

    @DeleteMapping("/groups/{groupID}")
    public String deleteGroup(@PathVariable long groupID) {
        log.info("Attempting to delete group with ID: {}", groupID);
        Group group = groupRepository.findById(groupID);
        if (notExistsInDatabase(group, GROUP)) {
            return "Failed to delete group\n" +
                    "The group did not exist in the database";
        } else {
            removeForeignKeys(group);
            String groupName = group.getGroupName();
            groupRepository.delete(group);
            log.info("Successfully deleted group with name: {} and ID: {}", groupName, groupID);
            return "Group deleted";
        }
    }

    private void removeForeignKeys(Group group) {
        for (User user : group.getMembers()) {
            user.removeGroupFromListOfGroups(group);
            userRepository.save(user);
        }
        for (Course course : group.getCourses()) {
            course.removeGroup(group);
            courseRepository.save(course);
        }
    }

    @PutMapping("/groups/{groupID}/members")
    public @ResponseBody Group addUserToGroup(@PathVariable long groupID,
                                              @RequestBody Map<String, String> json) {
        log.info("Attempting to add a user to group with ID: {}", groupID);
        Group group = groupRepository.findById(groupID);
        String nameOfUser = "" + json.get("userName");
        User user = userRepository.findByUserName(nameOfUser);
        if (notExistsInDatabase(group, GROUP)
                || notExistsInDatabase(user, USER)) {
            return null;
        }
        if (group.addUserToGroup(user)) {
            log.info("Successfully made user: {} a member of group with ID: {}", user.getUserName(), group.getId());
            groupRepository.save(group);
            if(!user.addGroupToUsersListOfGroups(group)) {
                log.info("Something went wrong when trying to add group to users list of groups");
            } else {
                userRepository.save(user);
            }
        } else {
            log.info("Failed to add user: {} as a member to group with ID: {}", user.getUserName(), group.getId());
        }
        return group;
    }


    @DeleteMapping("/groups/{groupID}/members")
    public @ResponseBody Group removeUserFromGroup(@PathVariable long groupID,
                                              @RequestBody Map<String, String> json) {
        log.info("Attempting to remove a user from group with ID: {}", groupID);
        Group group = groupRepository.findById(groupID);
        String nameOfUser = "" + json.get("userName");
        User user = userRepository.findByUserName(nameOfUser);
        if (notExistsInDatabase(group, GROUP)
                || notExistsInDatabase(user, USER)) {
            return null;
        }
        if (group.removeUserFromGroup(user)) {
            log.info("Successfully removed user: {} from list of members of group with ID: {}",
                    user.getUserName(), group.getId());
            groupRepository.save(group);
            if(!user.removeGroupFromListOfGroups(group)) {
                log.info("Something went wrong when trying to remove group from users list of groups");
            } else {
                userRepository.save(user);
            }
        } else {
            log.info("Failed to remove user: {} from the list of members of group with ID: {}",
                    user.getUserName(), group.getId());
        }
        return group;
    }


    @GetMapping("/users/{userID}/groups")
    public @ResponseBody List<Group> getUsersGroups(@PathVariable long userID) {
        log.info("Getting all groups the user with ID: {} is a member off", userID);
        User user = userRepository.findById(userID);
        if (notExistsInDatabase(user, USER)) {
            return null;
        }
        return user.getGroups();
    }

/*
    COURSE REQUESTS
*/
    @PostMapping("/courses")
    public String createCourse(@RequestBody Map<String, String> json) {
        log.info("Attempting to create a new course");
        // Concatenating request parameters with an empty
        // string to prevent null values.
        String courseName = "" + json.get("courseName");
        String educationalInstitution = "" + json.get("institution");
        String description = "" + json.get("description");
        if (courseRepository.findByName(courseName) != null) {
            log.info("The course name was already taken");
            return "Failed to create course.\nThe name was already taken";
        }
        try {
            Course course = new Course(courseName, educationalInstitution, description);
            courseRepository.save(course);
            log.info("Successfully created course: {} with ID: {}", course.getName(), course.getId());
            return "Successfully created course: {}" + course;
        } catch (IllegalArgumentException e) {
            log.info("Course name was too short");
            return "Failed to create course.\nThe name must be at least three characters long";
        }
    }

    @PutMapping("/courses/{courseID}")
    public @ResponseBody Course updateCourse(@PathVariable long courseID,
                                             @RequestBody Map<String, String> json) {
        log.info("Attempting to alter course with ID: {}", courseID);
        Course course = courseRepository.findById(courseID);
        if (notExistsInDatabase(course, COURSE)) {
            return null;
        }
        String newCourseName = "" + json.get("newCourseName");
        String newEducationalInstitution = "" + json.get("newInstitution");
        String newDescription = "" + json.get("newDescription");
        updateCourseName(course, newCourseName);
        updateCourseInstitution(course, newEducationalInstitution);
        updateCourseDescription(course, newDescription);
        log.info("Successfully updated the course information");
        return course;
    }

    private void updateCourseDescription(Course course, String newDescription) {
        if (!newDescription.isEmpty() && !newDescription.equals("null")) {
            course.setDescription(newDescription);
            courseRepository.save(course);
            log.info("Updated course description to: {}", newDescription);
        }
    }

    private void updateCourseInstitution(Course course, String newEducationalInstitution) {
        if (!newEducationalInstitution.isEmpty() && !newEducationalInstitution.equals("null")) {
            course.setInstitutionName(newEducationalInstitution);
            courseRepository.save(course);
            log.info("Updated course educational institution to: {}", newEducationalInstitution);
        }
    }

    private void updateCourseName(Course course, String newCourseName) {
        if (newCourseName.length() >= Course.MINIMUM_COURSE_NAME_LENGTH && !newCourseName.equals("null")
                && courseRepository.findByName(newCourseName) == null) {
            course.setName(newCourseName);
            courseRepository.save(course);
            log.info("Updated course name to: {}", newCourseName);
        }
    }

    @GetMapping("/courses")
    public @ResponseBody Iterable<Course> getAllCourses() {
        log.info("Getting all registered courses");
        return courseRepository.findAll();
    }

    @GetMapping("/courses/{courseID}")
    public @ResponseBody Course getCourse(@PathVariable long courseID) {
        log.info("Trying to find a specific course with id: {}", courseID);
        Course course = courseRepository.findById(courseID);
        if (notExistsInDatabase(course, COURSE)) {
            return null;
        } else {
            log.info("Successfully retrieved course: {} with ID: {}", course.getName(), course.getId());
            return course;
        }
    }

    @DeleteMapping("/courses/{courseID}")
    public String deleteCourse(@PathVariable long courseID) {
        log.info("Attempting to delete course with ID: {}", courseID);
        Course course = courseRepository.findById(courseID);
        if (notExistsInDatabase(course, COURSE)) {
            return "Failed to delete course\n" +
                    "The course did not exist in the database";
        } else {
            removeForeignKeys(course);
            String courseName = course.getName();
            courseRepository.delete(course);
            log.info("Successfully deleted course with name: {} and ID: {}", courseName, courseID);
            return "Course deleted";
        }
    }

    private void removeForeignKeys(Course course) {
        for (User user : course.getRelatedUsers()) {
            user.removeReferenceToCourse(course);
            userRepository.save(user);
        }
        for (Group group : course.getRelatedGroups()) {
            group.removeReferenceToCourse(course);
            groupRepository.save(group);
        }
    }

    @PutMapping("/courses/{courseID}/groups/{groupID}")
    public @ResponseBody Course registerGroupWithCourse(@PathVariable long courseID,
                                          @PathVariable long groupID) {
        log.info("Attempting to register group with ID: {} with course with ID: {}", groupID, courseID);
        Course course = courseRepository.findById(courseID);
        Group group = groupRepository.findById(groupID);
        if (notExistsInDatabase(course, COURSE) || notExistsInDatabase(group, GROUP)) {
            return null;
        }
        if (course.addGroup(group)) {
            log.info("Successfully registered the group: {} with the course: {}",
                    group.getGroupName(), course.getName());
            courseRepository.save(course);
            if (!group.addReferenceToCourse(course)) {
                log.info("Something went wrong when trying to add a reference to the course in the group");
            } else {
                groupRepository.save(group);
            }
        } else {
            log.info("Failed to register group: {} with course: {}", group.getGroupName(), course.getName());
        }
        return course;
    }

    @DeleteMapping("/courses/{courseID}/groups/{groupID}")
    public @ResponseBody Course removeGroupFromCourse(@PathVariable long courseID,
                                        @PathVariable long groupID) {
        log.info("Attempting to deregister group with ID: {} from course with ID: {}", groupID, courseID);
        Course course = courseRepository.findById(courseID);
        Group group = groupRepository.findById(groupID);
        if (notExistsInDatabase(course, COURSE) || notExistsInDatabase(group, GROUP)) {
            return null;
        }
        if (course.removeGroup(group)) {
            log.info("Successfully removed group: {} from course: {}", group.getGroupName(), course.getName());
            courseRepository.save(course);
            if (!group.removeReferenceToCourse(course)) {
                log.info("Something went wrong when trying to remove the reference to course from the group");
            } else {
                groupRepository.save(group);
            }
        } else {
            log.info("Failed to remove group: {} from course: {}", group.getGroupName(), course.getName());
        }
        return course;
    }

    @PutMapping("/courses/{courseID}/users/{userID}")
    public @ResponseBody Course registerUserWithCourse(@PathVariable long courseID,
                                         @PathVariable long userID) {
        log.info("Attempting to register user with ID: {} with course with ID: {}", userID, courseID);
        Course course = courseRepository.findById(courseID);
        User user = userRepository.findById(userID);
        if (notExistsInDatabase(course, COURSE) || notExistsInDatabase(user, USER)) {
            return null;
        }
        if (course.addUser(user)) {
            log.info("Successfully registered user: {} with the course: {}", user.getUserName(), course.getName());
            courseRepository.save(course);
            if (!user.addCourseToUsersListOfCourses(course)) {
                log.info("Something went wrong when trying to add a reference to the course for the user");
            } else {
                userRepository.save(user);
            }
        } else {
            log.info("Failed to register user: {} with the course: {}", user.getUserName(), course.getName());
        }
        return course;
    }

    @DeleteMapping("/courses/{courseID}/users/{userID}")
    public @ResponseBody Course removeUserFromCourse(@PathVariable long courseID,
                                       @PathVariable long userID) {
        log.info("Attempting to deregister user with ID: {} from course with ID: {}", userID, courseID);
        Course course = courseRepository.findById(courseID);
        User user = userRepository.findById(userID);
        if (notExistsInDatabase(course, COURSE) || notExistsInDatabase(user, USER)) {
            return null;
        }
        if (course.removeUser(user)) {
            log.info("Successfully removed user: {} from course: {}", user.getUserName(), course.getName());
            courseRepository.save(course);
            if (!user.removeReferenceToCourse(course)) {
                log.info("Something went wrong when trying to remove the reference to course from the user");
            } else {
                userRepository.save(user);
            }
        } else {
            log.info("Failed remove user: {} from course: {}", user.getUserName(), course.getName());
        }
        return course;
    }



    private static final String GROUP = "Group";
    private static final String USER = "User";
    private static final String COURSE = "Course";

    private boolean notExistsInDatabase(Object object, String objectName) {
        if (object == null) {
            log.info("{} did not exist in the database", objectName);
            return true;
        } else {
            return false;
        }
    }


}

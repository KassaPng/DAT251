package dat251.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat251.project.entities.utilities.EntityUtilities;
import dat251.project.matching.AbilityValues;
import dat251.project.security.Credentials;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String userName;
    private String name;

    @JsonIgnore // Enable to prevent password from being used in output.
    private String passwordAsHash;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    private List<Group> groups;

    @JsonIgnore
    @ManyToMany
    private List<Course> courses;

    @OneToMany
    private Map<Long, AbilityValues> abilities; //The users abilities for each group.

    // Construct the object to be included in the JSON response instead of groups
    @JsonProperty("groups")
    public List<String> getGroupsAsJsonString() {
        List<String> groupNames = new ArrayList<>();
        for (Group group : groups) {
            groupNames.add(group.getGroupName());
        }
        return groupNames;
    }

    // Construct the object to be included in the JSON response instead of courses
    @JsonProperty("courses")
    public List<String> getCoursesAsJsonString() {
        List<String> courseNames = new ArrayList<>();
        for (Course course : courses) {
            courseNames.add(course.getName());
        }
        return courseNames;
    }

    public User() {

    }

    public User(String name, String userName, String password) {
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name was too short");
        }
        this.name = name;
        this.userName = userName;
        setPasswordAsHash(password);
        this.groups = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.abilities = new HashMap<>();
    }

    public boolean addCourseToUsersListOfCourses(Course course) {
        if(course != null && !courses.contains(course)) {
            courses.add(course);
            //create mapping for the course in abilities
            var abilityValues = new AbilityValues();
            abilityValues.initializeAbilities(course.getAbilities());
            abilities.put(course.getId(), abilityValues);
            return true;
        }
        return false;
    }

    public boolean removeReferenceToCourse(Course course) {
        if (course != null && courses.contains(course)) {
            courses.remove(course);
            abilities.remove(course.getId());
            return true;
        }
        return false;
    }

    public boolean verifyPassword(String keyPhrase) {
        return Credentials.passwordsMatch(keyPhrase, this.passwordAsHash);
    }

    public boolean addGroupToUsersListOfGroups(Group group) {
        if (group != null && !groups.contains(group)) {
            groups.add(group);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeGroupFromListOfGroups(Group group) {
        if (group != null && groups.contains(group)) {
            groups.remove(group);
         //   abilities.remove(group);
            return true;
        } else {
            return false;
        }
    }
    public Map<String, Double> getAbilities(Course course) throws IllegalArgumentException {
        if(!abilities.containsKey(course.getId())) {
            throw new IllegalArgumentException("Course does not exist");
        }
        return abilities.get(course.getId()).getAbilities();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordAsHash() {
        return passwordAsHash;
    }

    public void setPasswordAsHash(String passwordAsHash) {
      //  long start = System.currentTimeMillis();
        this.passwordAsHash = Credentials.encodePassword(passwordAsHash);
      //  long stop = System.currentTimeMillis();
       // System.out.println("Encryption took: " + (stop - start) + " microseconds");
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void setAbilities(Course course, String ability, int value) {
        AbilityValues abilityValues = abilities.get(course.getId());
        abilityValues.setAbilities(ability, value);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Map<Long, AbilityValues> getAbilities() {
        return abilities;
    }

    public void setAbilities(Map<Long, AbilityValues> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        return String.format(
                "User[Id='%d', " +
                        "name='%s', " +
                        "userName='%s', " +
                        "groups='%s', " +
                        "courses='%s']",
                id, name, userName, EntityUtilities.printListContents(groups),
                EntityUtilities.printListContents(courses)
        );
    }
}

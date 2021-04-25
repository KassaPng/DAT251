package dat251.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat251.project.entities.utilities.EntityUtilities;
import dat251.project.matching.AbilityList;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "groups")
public class Group {

    public static final int MAX_GROUP_DESCRIPTION_LENGTH = 500;

    @OneToOne
    private AbilityList listOfAbilities;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String groupName;
    @Column(length = MAX_GROUP_DESCRIPTION_LENGTH)
    private String description;

    @JsonIgnore
    @ManyToMany
    private List<User> members;

    @JsonIgnore
    @ManyToMany
    private List<Course> courses;

    // Construct the object to be included in the JSON response instead of members
    @JsonProperty("members")
    public List<String> getMembersAsJsonString() {
        List<String> memberNames = new ArrayList<>();
        for (User member : members) {
            memberNames.add(member.getName());
        }
        return memberNames;
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

    public Group() {

    }

    public Group(String groupName, String description) {
        if (groupName.length() < 3) {
            throw new IllegalArgumentException("Group name must be at least three characters long");
        }
        this.groupName = groupName;
        this.description = description;
        this.members = new ArrayList<>();
        this.courses = new ArrayList<>();
        //this.listOfAbilities = new AbilityList(true,false); //TODO: should be set by group admin
    }

    public boolean addUserToGroup(User user) {
        if (user != null && !members.contains(user)) {
            members.add(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeUserFromGroup(User user) {
        if (user != null && members.contains(user)) {
            members.remove(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean addReferenceToCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeReferenceToCourse(Course course) {
        if (course != null && courses.contains(course)) {
            courses.remove(course);
            return true;
        } else {
            return false;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public AbilityList getAbilities() {
        return listOfAbilities;
    }

    public void setListOfAbilities(AbilityList listOfAbilities) {
        this.listOfAbilities = listOfAbilities;
    }

    public AbilityList getListOfAbilities() {
        return listOfAbilities;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return String.format(
                "Group[Id='%d', " +
                        "groupName='%s', " +
                        "description='%s', " +
                        "members='%s', " +
                        "courses='%s']",
                id, groupName, description, EntityUtilities.printListContents(members),
                EntityUtilities.printListContents(courses)
        );
    }
}

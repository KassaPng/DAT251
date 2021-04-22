package dat251.project.entities;

import dat251.project.matching.AbilityList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {

    private static final int MINIMUM_COURSE_NAME_LENGTH = 3;
    static final String DEFAULT_NAME_OF_EDUCATIONAL_INSTITUTION = "None";
    static final String DEFAULT_COURSE_DESCRIPTION = "This course has not yet defined a description.";

    @Transient
    private AbilityList listOfAbilities;

    @Id
    private long id;
    private String name;
    private String institutionName; // Name of educational institution where the course is taught.
    private String description;
    
    @ManyToMany
    private List<Group> relatedGroups;

    @ManyToMany
    private List<User> relatedUsers;

    public Course() {}

    public Course(String courseName, String nameOfInstitutionTeachingThisCourse, String description) {
        if (courseName.length() < MINIMUM_COURSE_NAME_LENGTH) {
            throw new IllegalArgumentException("Course name was too short");
        }
        this.name = courseName;
        this.institutionName = (nameOfInstitutionTeachingThisCourse == null
                || nameOfInstitutionTeachingThisCourse.isEmpty()) ?
                DEFAULT_NAME_OF_EDUCATIONAL_INSTITUTION : nameOfInstitutionTeachingThisCourse;
        this.description = (description == null || description.isEmpty()) ? DEFAULT_COURSE_DESCRIPTION : description;
        this.relatedGroups = new ArrayList<>();
        this.relatedUsers = new ArrayList<>();
        this.listOfAbilities = new AbilityList(true,false); //TODO: should be set by group admin
    }

    public boolean addGroup(Group group) {
        if (group != null && !relatedGroups.contains(group)) {
            relatedGroups.add(group);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeGroup(Group group) {
        if (group != null && relatedGroups.contains(group)) {
            relatedGroups.remove(group);
            return true;
        } else {
            return false;
        }
    }

    public boolean addUser(User user) {
        if (user != null && !relatedUsers.contains(user)) {
            relatedUsers.add(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeUser(User user) {
        if (user != null && relatedUsers.contains(user)) {
            relatedUsers.remove(user);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Group> getRelatedGroups() {
        return relatedGroups;
    }

    public void setRelatedGroups(List<Group> relatedGroups) {
        this.relatedGroups = relatedGroups;
    }

    public List<User> getRelatedUsers() {
        return relatedUsers;
    }

    public void setRelatedUsers(List<User> relatedUsers) {
        this.relatedUsers = relatedUsers;
    }

    public List<String> getAbilities() {
        return listOfAbilities.getListOfAbilities();
    }

    public void setAbilities(AbilityList listOfAbilities) {
        this.listOfAbilities = listOfAbilities;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", description='" + description + '\'' +
                ", relatedGroups=" + printListContents(relatedGroups) +
                ", relatedUsers=" + printListContents(relatedUsers) +
                '}';
    }

    private String printListContents(List<?> array) {
        if (array.isEmpty()) {
            return "[]";
        }
        StringBuilder out = new StringBuilder("[ ");
        for (Object obj : array) {
            if (obj instanceof User) {
                out.append(((User) obj).getUserName());
            } else if (obj instanceof Group) {
                out.append(((Group) obj).getGroupName());
            } else {
                throw new IllegalArgumentException("Array content not recognized");
            }
            out.append(", ");
        }
        out.deleteCharAt(out.length() - 2);
        out.append("]");
        return out.toString();
    }
}

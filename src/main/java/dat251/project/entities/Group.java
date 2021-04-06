package dat251.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat251.project.matching.AbilityList;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "groups")
public class Group {

    public static final int MAX_GROUP_DESCRIPTION_LENGTH = 500;
    @Transient
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

    // Construct the object to be included in the JSON response instead of members
    @JsonProperty("members")
    public List<String> getMembersAsJsonString() {
        List<String> memberNames = new ArrayList<>();
        for (User member : members) {
            memberNames.add(member.getName());
        }
        return memberNames;
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
        this.listOfAbilities = new AbilityList(true,false); //TODO: should be set by group admin
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

    @Override
    public String toString() {
        return String.format(
                "Group[Id='%d', " +
                        "groupName='%s', " +
                        "description='%s', " +
                        "members='%s']",
                id, groupName, description, printMembers()
        );
    }

    private String printMembers() {
        if (members.isEmpty()) {
            return "[]";
        }
        StringBuilder out = new StringBuilder("[ ");
        for (User user : members) {
            out.append(user.getUserName()).append(", ");
        }
        out.deleteCharAt(out.length() - 2);
        out.append("]");
        return out.toString();
    }
}

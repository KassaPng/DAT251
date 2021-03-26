package dat251.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat251.project.matching.AbilityValues;

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
    @ManyToMany
    private List<Group> groups;


    // Construct the object to be included in the JSON response instead of groups
    @JsonProperty("groups")
    public List<String> getGroupsAsJsonString() {
        List<String> groupNames = new ArrayList<>();
        for (Group group : groups) {
            groupNames.add(group.getGroupName());
        }
        return groupNames;
    }
    @Transient
    private Map<Group, AbilityValues> abilities; //The users abilities for each group.

    public User() {

    }

    public User(String name, String userName, String password) {
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name was too short");
        }
        this.name = name;
        this.userName = userName;
        this.passwordAsHash = password;
        this.groups = new ArrayList<>();
        this.abilities = new HashMap<>();

    }

    public boolean addGroupToUsersListOfGroups(Group group) {
        if (!(group == null) && !groups.contains(group)) {
            groups.add(group);
            //create mapping for the group in abilities
            AbilityValues ab = new AbilityValues();
            for(int i = 0; i < group.getAbilities().size(); i++) {
                ab.setAbilities(group.getAbilities().get(i), 0);
            }
            abilities.put(group, ab);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeGroupFromListOfGroups(Group group) {
        if (!(group == null) && groups.contains(group)) {
            groups.remove(group);
            abilities.remove(group);
            return true;
        } else {
            return false;
        }
    }
    public Map<String, Double> getAbilities(Group group) throws IllegalArgumentException {
        if(!abilities.containsKey(group)) {
            throw new IllegalArgumentException("group does not exist");
        }
        return abilities.get(group).getAbilityValues();
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
        this.passwordAsHash = passwordAsHash;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Map<Group, AbilityValues> getAbilities() {
        return abilities;
    }

    public void setAbilities(Map<Group, AbilityValues> abilities) {
        this.abilities = abilities;
    }

    @Override
    public String toString() {
        return String.format(
                "User[Id='%d', " +
                        "name='%s', " +
                        "userName='%s', " +
                        "groups='%s']",
                id, name, userName, printGroups()
        );
    }

    private String printGroups() {
        if (groups.isEmpty()) {
            return "[]";
        }
        StringBuilder out = new StringBuilder("[ ");
        for (Group group : groups) {
            out.append(group.getGroupName()).append(", ");
        }
        out.deleteCharAt(out.length() - 2);
        out.append("]");
        return out.toString();
    }

}

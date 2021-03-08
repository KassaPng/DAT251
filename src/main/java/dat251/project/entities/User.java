package dat251.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    private List<Group> groups;


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
    }

    public boolean addGroupToUsersListOfGroups(Group group) {
        if (!(group == null) && !groups.contains(group)) {
            groups.add(group);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeGroupFromListOfGroups(Group group) {
        if (!(group == null) && groups.contains(group)) {
            groups.remove(group);
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
        StringBuilder out = new StringBuilder("[ ");
        for (Group group : groups) {
            out.append(group.getGroupName()).append(", ");
        }
        out.deleteCharAt(out.length() - 2);
        out.append("]");
        return out.toString();
    }

}

package dat251.project.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String groupName;

    @ManyToMany
    private List<User> members;


    public Group() {

    }

    public Group(String groupName) {
        if (groupName.length() < 3) {
            throw new IllegalArgumentException("Group name must be at least three characters long");
        }
        this.groupName = groupName;
        this.members = new ArrayList<>();
    }

    public boolean addUserToGroup(User user) {
        if (!(user == null) && !members.contains(user)) {
            members.add(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeUserFromGroup(User user) {
        if (!(user == null) && members.contains(user)) {
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

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return String.format(
                "Group[Id='%d', " +
                        "groupName='%s', " +
                        "members='%s']",
                id, groupName, printMembers()
        );
    }

    private String printMembers() {
        StringBuilder out = new StringBuilder("[ ");
        for (User user : members) {
            out.append(user.getUserName()).append(", ");
        }
        out.deleteCharAt(out.length() - 2);
        out.append("]");
        return out.toString();
    }
}

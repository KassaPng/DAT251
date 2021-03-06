package dat251.project.entities;

import javax.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String groupName;


    public Group() {

    }

    public Group(String groupName) {
        if (groupName.length() < 3) {
            throw new IllegalArgumentException("Group name must be at least three characters long");
        }
        this.groupName = groupName;
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

    @Override
    public String toString() {
        return String.format(
                "User[Id='%d', " +
                        "groupName='%s']",
                id, groupName
        );
    }
}

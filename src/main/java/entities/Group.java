package entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "group")
public class Group {
    @Id
    private long id;
    private String groupName;

    public Group(String groupName) {
        if (groupName.length() < 3) {
            throw new IllegalArgumentException("Group name must be at least three characters long");
        }
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

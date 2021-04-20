package dat251.project.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {

    private static final int MINIMUM_COURSE_NAME_LENGTH = 3;

    @Id
    private long id;
    private String name;





    public Course() {}

    public Course(String name) {
        if (name.length() < MINIMUM_COURSE_NAME_LENGTH) {
            throw new IllegalArgumentException("Course name was too short");
        }
        this.name = name;
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
}

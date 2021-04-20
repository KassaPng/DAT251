package dat251.project.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {

    private static final int MINIMUM_COURSE_NAME_LENGTH = 3;
    static final String DEFAULT_NAME_OF_EDUCATIONAL_INSTITUTION = "None";
    static final String DEFAULT_COURSE_DESCRIPTION = "This course has not yet defined a description.";

    @Id
    private long id;
    private String name;
    private String institutionName; // Name of educational institution where the course is taught.
    private String description;




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
}

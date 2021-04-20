package dat251.project.entities;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        String name = "DAT251";
        String nameOfInstitution = "UiB";
        String courseDescription = "Description";
        course = new Course(name, nameOfInstitution, courseDescription);
    }

    @Test
    void coursesShouldHaveANameConsistingOfAtLeastThreeCharacters() {
        String tooShortName = "AB";
        assertThrows(IllegalArgumentException.class, () -> new Course(tooShortName, "", ""));
        String longEnoughName = "ABC";
        assertDoesNotThrow(() -> new Course(longEnoughName, "", ""));
    }

    @Test
    void coursesShouldBeTaughtAtAnEducationalInstitution() {
        Course course1 = new Course("ABC", "", "");
        assertFalse(course1.getInstitutionName().isEmpty());
        assertEquals(Course.DEFAULT_NAME_OF_EDUCATIONAL_INSTITUTION, course1.getInstitutionName());

        String institutionName = "UiB";
        Course course2 = new Course("ABC", institutionName, "");
        assertEquals(institutionName, course2.getInstitutionName());
    }

    @Test
    void coursesShouldHaveACourseDescription() {
        Course course1 = new Course("ABC", "", "");
        assertFalse(course1.getDescription().isEmpty());
        assertEquals(Course.DEFAULT_COURSE_DESCRIPTION, course1.getDescription());

        String description = "Course for teaching software development methods and best practices";
        Course course2 = new Course("DAT251", "UiB", description);
        assertEquals(description, course2.getDescription());
    }



    @Test
    void coursesShouldHaveReferencesToAllGroupsRelatedToThatCourse() {


      //  course.getRelatedGroups();


    }









/*

 @Test
    void a() {

    }
    @Test
    void a() {

    }
    @Test
    void a() {

    }
    @Test
    void a() {

    }
    @Test
    void a() {

    }
//*/
}
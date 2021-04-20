package dat251.project.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        String name = "DAT251";
        course = new Course(name);
    }

    @Test
    void courseShouldHaveANameOfAtLeastThreeCharacters() {
        String tooShortName = "AB";
        assertThrows(IllegalArgumentException.class, () -> new Course(tooShortName));
        String longEnoughName = "ABC";
        assertDoesNotThrow(() -> new Course(longEnoughName));
    }




}
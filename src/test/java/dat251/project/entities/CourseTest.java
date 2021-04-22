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
        assertTrue(course.getRelatedGroups().isEmpty());
        String group1Name = "Group1";
        Group group1 = new Group(group1Name, "");
        assertTrue(course.addGroup(group1));
        assertEquals(group1, course.getRelatedGroups().get(0));
        String group2Name = "Group2";
        Group group2 = new Group(group2Name, "");
        assertTrue(course.addGroup(group2));
        assertEquals(2, course.getRelatedGroups().size());
        assertEquals(group2Name, course.getRelatedGroups().get(1).getGroupName());
    }

    @Test
    void addingAGroupReferenceThatAlreadyExistsForThatCourseShouldFail() {
        Group group = new Group("Group", "");
        assertTrue(course.addGroup(group));
        assertFalse(course.addGroup(group));
    }

    @Test
    void addingAGroupReferenceThatIsNullShouldFail() {
        assertFalse(course.addGroup(null));
    }

    @Test
    void removingARelatedGroupShouldRemoveTheReferenceToThatGroup() {
        Group group1 = new Group("Group1", "");
        assertTrue(course.addGroup(group1));
        Group group2 = new Group("Group2", "");
        assertTrue(course.addGroup(group2));
        assertEquals(2, course.getRelatedGroups().size());
        assertTrue(course.removeGroup(group1));
        assertEquals(1, course.getRelatedGroups().size());
        assertEquals(group2, course.getRelatedGroups().get(0));
        assertTrue(course.removeGroup(group2));
        assertTrue(course.getRelatedGroups().isEmpty());
    }

    @Test
    void removingAGroupThatTheCourseIsNotReferencingShouldFail() {
        Group group = new Group("Group", "");
        assertFalse(course.getRelatedGroups().contains(group));
        assertFalse(course.removeGroup(group));
    }

    @Test
    void coursesShouldHaveReferencesToAllUsersRelatedToThatCourse() {
        User user1 = new User("Name1", "test1", "Password");
        assertTrue(course.addUser(user1));
        assertEquals(user1, course.getRelatedUsers().get(0));
        User user2 = new User("Name2", "test2", "Password");
        assertTrue(course.addUser(user2));
        assertEquals(2, course.getRelatedUsers().size());
        assertEquals(user1, course.getRelatedUsers().get(0));
        assertEquals(user2, course.getRelatedUsers().get(1));
    }

    @Test
    void addingAUserReferenceThatAlreadyExistsForThatCourseShouldFail() {
        User user = new User("Name", "test", "Password");
        assertTrue(course.addUser(user));
        assertFalse(course.addUser(user));
    }

    @Test
    void addingAUserReferenceThatIsNullShouldFail() {
        assertFalse(course.addUser(null));
    }

    @Test
    void removingARelatedUserShouldRemoveTheReferenceToThatUser() {
        User user1 = new User("Name1", "test1", "Password");
        assertTrue(course.addUser(user1));
        User user2 = new User("Name2", "test2", "Password");
        assertTrue(course.addUser(user2));
        assertEquals(2, course.getRelatedUsers().size());
        assertTrue(course.removeUser(user1));
        assertEquals(1, course.getRelatedUsers().size());
        assertEquals(user2, course.getRelatedUsers().get(0));
        assertTrue(course.removeUser(user2));
        assertTrue(course.getRelatedUsers().isEmpty());
    }

    @Test
    void removingAUserThatTheCourseIsNotReferencingShouldFail() {
        User user = new User("Name", "test", "Password");
        assertFalse(course.getRelatedUsers().contains(user));
        assertFalse(course.removeUser(user));
    }

    @Test
    void aCourseShouldBeCorrectlyRepresentedAsAString() {
        String emptyArray = "[]";
        String courseAsAString = "Course{" +
                "id=" + course.getId() +
                ", name='" + course.getName() + '\'' +
                ", institutionName='" + course.getInstitutionName() + '\'' +
                ", description='" + course.getDescription() + '\'';
        String courseWithNoRelatedGroupsAndUsers = courseAsAString +
                ", relatedGroups=" + emptyArray +
                ", relatedUsers=" + emptyArray + "}";
        assertEquals(courseWithNoRelatedGroupsAndUsers, course.toString());
        Group group1 = new Group("Name", "description");
        User user1 = new User("Name", "UserName", "Password");
        assertTrue(course.addGroup(group1));
        assertTrue(course.addUser(user1));
        String courseWithOneRelatedGroupAndOneRelatedUser = courseAsAString +
                ", relatedGroups=" + "[ " + group1.getGroupName() + " ]" +
                ", relatedUsers=" + "[ " + user1.getUserName() + " ]" + "}";
        assertEquals(courseWithOneRelatedGroupAndOneRelatedUser, course.toString());
    }


/*

    @Test
    void a() {

    }
    @Test
    void a() {

    }
//*/
}
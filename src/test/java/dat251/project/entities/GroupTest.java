package dat251.project.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private Group group;
    private User user;
    private User user2;
    private Course course;

    @BeforeEach
    void setUp() {
        String groupName = "Test Group1";
        String groupDescription = "Description";
        group = new Group(groupName, groupDescription);
        String name = "Test";
        String userName = "test@something.com";
        String password = "Password99";
        user = new User(name, userName, password);
        name = "User2";
        userName = "user2@something.com";
        password = "Password346352345";
        user2 = new User(name, userName, password);
        course = new Course("Name", "UiB", "Description");
    }

    @Test
    void onlyAListOfUserNamesShouldBeIncludedInAJsonResponse() {
        List<String> memberNames = group.getMembersAsJsonString();
        assertTrue(memberNames.isEmpty());

        group.addUserToGroup(user);
        assertEquals(user, group.getMembers().get(0));
        memberNames = group.getMembersAsJsonString();
        assertEquals(user.getName(), memberNames.get(0));

        group.addUserToGroup(user2);
        memberNames = group.getMembersAsJsonString();
        assertEquals(user.getName(), memberNames.get(0));
        assertEquals(user2.getName(), memberNames.get(1));
    }

    @Test
    void groupNamesMustBeAtLeastThreeCharactersLong() {
        String tooShortGroupName = "HF";
        String description = "Description";
        assertThrows(IllegalArgumentException.class, () -> new Group(tooShortGroupName, description));
        String acceptableGroupName = "HFA";
        assertDoesNotThrow(() -> { new Group(acceptableGroupName, description); });
    }

    @Test
    void addingAUserToAGroupShouldRegisterThatUserAsAMemberOfThatGroup() {
        assertTrue(group.addUserToGroup(user));
        assertEquals(user, group.getMembers().get(0));
    }

    @Test
    void addingAUserToAGroupThatIsAlreadyAMemberOfThatGroupShouldDoNothing() {
        assertTrue(group.addUserToGroup(user));
        assertFalse(group.addUserToGroup(user));
        assertEquals(1, group.getMembers().size());
        assertEquals(user, group.getMembers().get(0));
    }

    @Test
    void removingAUserFromAGroupShouldUnregisterThatUserAsAMemberOfThatGroup() {
        assertTrue(group.addUserToGroup(user));
        assertEquals(user, group.getMembers().get(0));
        assertTrue(group.removeUserFromGroup(user));
        assertTrue(group.getMembers().isEmpty());
    }

    @Test
    void removingAUserFromAGroupThatIsNotAMemberOfThatGroupShouldDoNothing() {
        assertTrue(group.addUserToGroup(user));
        assertEquals(user, group.getMembers().get(0));
        assertFalse(group.removeUserFromGroup(user2));
        assertEquals(1, group.getMembers().size());
        assertEquals(user, group.getMembers().get(0));
    }

    @Test
    void aGroupShouldBeCorrectlyRepresentedAsAString() {
        group.getMembers().clear();
        String emptyArray = "[]";
        String groupAsString = "Group[Id='" + group.getId() + "', "
                + "groupName='" + group.getGroupName() + "', "
                + "description='" + group.getDescription() + "', ";
        String groupWithNoMembersAndCourses = groupAsString +
                "members='" + emptyArray + "', " +
                "courses='" + emptyArray + "']";
        assertEquals(groupWithNoMembersAndCourses, group.toString());

        assertTrue(group.addUserToGroup(user));
        String oneMember = "[ " + group.getMembers().get(0).getUserName() + " ]";
        Course course1 = new Course("Name1", "UiB", "description");
        assertTrue(group.addReferenceToCourse(course1));
        String oneCourse = "[ " + group.getCourses().get(0).getName() + " ]";
        String groupWithOneMemberAndOneCourse = groupAsString +
                "members='" + oneMember + "', " +
                "courses='" + oneCourse + "']";
        assertEquals(groupWithOneMemberAndOneCourse, group.toString());

        assertTrue(group.addUserToGroup(user2));
        String twoMembers = "[ "
                + group.getMembers().get(0).getUserName() + ", "
                + group.getMembers().get(1).getUserName() + " ]";
        Course course2 = new Course("Name2", "HVL", "description");
        assertTrue(group.addReferenceToCourse(course2));
        String twoCourses = "[ " +
                group.getCourses().get(0).getName() + ", " +
                group.getCourses().get(1).getName() + " ]";
        String groupWithTwoMembersAndTwoCourses = groupAsString +
                "members='" + twoMembers + "', " +
                "courses='" + twoCourses + "']";
        assertEquals(groupWithTwoMembersAndTwoCourses, group.toString());
    }

    @Test
    void addingAReferenceToACourseShouldRegisterThatReference() {
        assertTrue(group.addReferenceToCourse(course));
        assertEquals(course, group.getCourses().get(0));
    }

    @Test
    void addingAReferenceToACourseAlreadyPresentShouldFail() {
        assertTrue(group.addReferenceToCourse(course));
        assertFalse(group.addReferenceToCourse(course));
        assertEquals(1, group.getCourses().size());
        assertEquals(course, group.getCourses().get(0));
    }

    @Test
    void addingANullReferenceToACourseShouldFail() {
        assertFalse(group.addReferenceToCourse(null));
    }

    @Test
    void removingAReferenceToACourseShouldDeRegisterThatCourse() {
        assertTrue(group.addReferenceToCourse(course));
        assertEquals(1, group.getCourses().size());
        assertEquals(course, group.getCourses().get(0));
        assertTrue(group.removeReferenceToCourse(course));
        assertTrue(group.getCourses().isEmpty());
    }

    @Test
    void removingAReferenceToACourseThatIsNotRegisteredForTheGroupShouldDoNothing() {
        assertTrue(group.addReferenceToCourse(course));
        assertEquals(1, group.getCourses().size());
        Course course2 = new Course("Name2", "HVL", "Description");
        assertFalse(group.removeReferenceToCourse(course2));
        assertEquals(course, group.getCourses().get(0));
    }
}
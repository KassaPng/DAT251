package dat251.project.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    private Group group;
    private User user;
    private User user2;

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
    void aGroupShouldBeCorrectlyRepresentedAsAString() {
        group.getMembers().clear();
        String noMembers = "[]";
        String groupAsString = "Group[Id='" + group.getId() + "', "
                + "groupName='" + group.getGroupName() + "', "
                + "description='" + group.getDescription() + "', "
                + "members='" + noMembers + "']";
        assertEquals(groupAsString, group.toString());
        group.addUserToGroup(user);
        String oneMember = "[ " + group.getMembers().get(0).getUserName() + " ]";
        groupAsString = "Group[Id='" + group.getId() + "', "
                + "groupName='" + group.getGroupName() + "', "
                + "description='" + group.getDescription() + "', "
                + "members='" + oneMember + "']";
        assertEquals(groupAsString, group.toString());
        group.addUserToGroup(user2);
        String twoMembers = "[ "
                + group.getMembers().get(0).getUserName() + ", "
                + group.getMembers().get(1).getUserName() + " ]";
        groupAsString = "Group[Id='" + group.getId() + "', "
                + "groupName='" + group.getGroupName() + "', "
                + "description='" + group.getDescription() + "', "
                + "members='" + twoMembers + "']";
        assertEquals(groupAsString, group.toString());
    }

}
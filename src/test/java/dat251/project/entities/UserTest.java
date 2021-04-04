package dat251.project.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Group group;

    @BeforeEach
    void beforeEach() {
        user = new User("test", "username", "Password");
        group = new Group("Test Group", "Description");
    }

    @Test
    void userShouldHaveANameOfAtLeastThreeCharacters() {
        User user = new User("abc", "username", "password");
        assertTrue(user.getName().length() >= 3);

        assertThrows(IllegalArgumentException.class, () -> {User user2 = new User("hh", "username","password");});
    }

/*
    PASSWORD TESTS
*/

    @Test
    void passwordShouldBeTheSameAsWhatItIsSetToBe() {
        String newPassword = "newPassword";
        user.setPasswordAsHash(newPassword);
        assertTrue(user.verifyPassword(newPassword));
    }

    /*
     hash examples:
     $argon2i$v=19$m=16,t=2,p=1$ZnlqdGRka2NrNzY4$xwFk6TKNxORmHwrCDsaSNA
     $argon2id$v=19$m=4096,t=3,p=1$vnOEfUC3oZ3sVBj/yKG/4g$LdVFmw9N5D49tuJiYT0LGZ8YOqYetqz5UzDyku+7PRs
     $argon2id$v=19$m=4096,t=3,p=1$iurr6y6xk2X7X/YVOEQXBg$ti9/be9VgbXtJWpm1hoYyLm8V0wBGr+dxu9X+PFbpZI
     $argon2i$v=19$m=65536,t=22,p=1$LqszYnhGhlM6AW3ehXhXmA$hgFiUxZUbgdodrIOUHhUzPdiWecYYFmHdFPQEf6beBc
     $argon2i$v=19$m=65536,t=22,p=1$kySDkzqRkEr748trey63Dg$OsKcqvoK/Y5pywATXw0P8RmKeMAzurNsgbGlmnw8Svs
    */
    @Test
    void passwordShouldBeHashed() {
        String regularExpressionOfArgon2Hash = "\\$[a-z0-9]+"
                + "\\$v=[0-9]+"
                + "\\$m=[0-9]+,t=[0-9]+,p=[0-9]+"
                + "\\$[a-zA-Z0-9/+]+"
                + "\\$[a-zA-Z0-9/+]+";
        assertTrue(Pattern.matches(regularExpressionOfArgon2Hash, user.getPasswordAsHash()));
    }

    @Test
    void AddingAGroupShouldRegisterThatGroupForTheUser() {
        assertTrue(user.addGroupToUsersListOfGroups(group));
        assertEquals(group, user.getGroups().get(0));
    }

    @Test
    void addingAGroupTheUserIsAlreadyAMemberOfShouldDoNothing() {
        assertTrue(user.addGroupToUsersListOfGroups(group));
        assertFalse(user.addGroupToUsersListOfGroups(group));
        assertEquals(1, user.getGroups().size());
    }

    @Test
    void removingAGroupShouldUnregisterThatGroupForTheUser() {
        assertTrue(user.addGroupToUsersListOfGroups(group));
        assertTrue(user.removeGroupFromListOfGroups(group));
        assertTrue(user.getGroups().isEmpty());
    }

    @Test
    void removingAGroupTheUserIsNotAMemberOfShouldDoNothing() {
        user.addGroupToUsersListOfGroups(group);
        Group group2 = new Group("New Group", "Description");
        assertFalse(user.removeGroupFromListOfGroups(group2));
        assertEquals(1, user.getGroups().size());
        assertEquals(group, user.getGroups().get(0));
    }

    @Test
    void onlyAListOfGroupNamesShouldBeIncludedInAJsonResponse() {
        user.addGroupToUsersListOfGroups(group);
        assertEquals(group, user.getGroups().get(0));
        List<String> groupNames = user.getGroupsAsJsonString();
        assertEquals(group.getGroupName(), groupNames.get(0));
    }

    @Test
    void aUserShouldBeRepresentedWithTheCorrectFormatAsAString() {
        String emptyGroup = "[]";
        String userAsString = "User[Id='" + user.getId()
                + "', name='" + user.getName()
                + "', userName='" + user.getUserName()
                + "', groups='" + emptyGroup
                + "']";
        assertEquals(userAsString, user.toString());
        user.addGroupToUsersListOfGroups(group);
        String oneGroup = "[ " + user.getGroups().get(0).getGroupName() + " ]";
        userAsString = "User[Id='" + user.getId()
                + "', name='" + user.getName()
                + "', userName='" + user.getUserName()
                + "', groups='" + oneGroup
                + "']";
        assertEquals(userAsString, user.toString());
        Group group2 = new Group("Group 2", "description");
        user.addGroupToUsersListOfGroups(group2);
        assertEquals(2, user.getGroups().size());
        String twoGroups = "[ "
                + user.getGroups().get(0).getGroupName() + ", "
                + user.getGroups().get(1).getGroupName() + " ]";
        userAsString = "User[Id='" + user.getId()
                + "', name='" + user.getName()
                + "', userName='" + user.getUserName()
                + "', groups='" + twoGroups
                + "']";
        assertEquals(userAsString, user.toString());
    }

}
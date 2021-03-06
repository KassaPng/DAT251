package dat251.project.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    void userShouldHaveNameOfAtLeastThreeCharacters() {
        User user = new User("abc", "username", "password");
        assertTrue(user.getName().length() >= 3);

        assertThrows(IllegalArgumentException.class, () -> {User user2 = new User("hh", "username","password");});
    }



}
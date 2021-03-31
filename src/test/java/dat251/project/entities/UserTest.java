package dat251.project.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static User user;

    @BeforeAll
    static void beforeAll() {
        user = new User("test", "username", "Password");
    }

    @Test
    void userShouldHaveNameOfAtLeastThreeCharacters() {
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
        System.out.println("user = " + user.getPasswordAsHash());
        String regularExpressionOfArgon2Hash = "\\$[a-z0-9]+"
                + "\\$v=[0-9]+"
                + "\\$m=[0-9]+,t=[0-9]+,p=[0-9]+"
                + "\\$[a-zA-Z0-9/]+"
                + "\\$[a-zA-Z0-9/+]+";
        assertTrue(Pattern.matches(regularExpressionOfArgon2Hash, user.getPasswordAsHash()));
    }
}
package dat251.project.security;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Credentials {

    private Credentials() {
    }

    static final int SALT_LENGTH = 16;
    static final int HASH_LENGTH = 32;
    static final int ITERATIONS = 2;
    static final int ONE_MEGABYTE_IN_KIBIBYTES = 1024;
    static final int MEMORY_REQUIRED = 64 * ONE_MEGABYTE_IN_KIBIBYTES;
    static final int PARALLELISM = 1;

    public static String encodePassword(String password) {
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder(SALT_LENGTH,
                HASH_LENGTH,
                PARALLELISM,
                MEMORY_REQUIRED,
                ITERATIONS);
        return passwordEncoder.encode(password);
    }

    public static boolean passwordsMatch(String keyPhrase, String hashedKeyPhrase) {
        PasswordEncoder encoder = new Argon2PasswordEncoder();
        return encoder.matches(keyPhrase, hashedKeyPhrase);
    }
}

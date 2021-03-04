package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String userName;
    private String name;


    //    @JsonIgnore // Enable to prevent password from being used in output.
 //   @Setter(AccessLevel.NONE)
    private String passwordAsHash;



    public User(String name, String userName, String password) {
        if (name.length() < 3) {
            throw new IllegalArgumentException("Name was too short");
        }
        this.name = name;
        this.userName = userName;
        this.passwordAsHash = password;
    }

    @Override
    public String toString() {
        return String.format(
                "User[Id='%d', " +
                        "name= '%s', " +
                        "userName='%s']",
                id, name, userName
        );
    }
}

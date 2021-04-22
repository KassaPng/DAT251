package dat251.project;

import dat251.project.entities.Course;
import dat251.project.entities.Group;
import dat251.project.entities.User;
import dat251.project.matching.Kmeans;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KmeansTest {
    private static Course course;
    private static ArrayList<User> users;


    @BeforeAll
    public static void init() {
        course = new Course("INF251", "UIB", "");
        users = new ArrayList<>();
        User trond = new User("trond", "trond", "a");
        User espen = new User("espen", "espen", "a");
        User atle = new User("atle", "atle", "a");
        User per = new User("per", "per", "a");
        trond.addCourseToUsersListOfCourses(course);
        espen.addCourseToUsersListOfCourses(course);
        atle.addCourseToUsersListOfCourses(course);
        per.addCourseToUsersListOfCourses(course);
        trond.setAbilities(course, "Ambition", 10);
        trond.setAbilities(course, "Work-rate", 10);
        trond.setAbilities(course, "Knowledge", 10);
        espen.setAbilities(course, "Ambition", 5);
        espen.setAbilities(course, "Work-rate", 5);
        espen.setAbilities(course, "Knowledge", 5);
        users.add(trond);
        users.add(espen);
        users.add(atle);
        users.add(per);
    }

    @Test
    public void checkThatKClustersAreCreated() {
        Kmeans km = new Kmeans(course);
        Map<Kmeans.Centroid, ArrayList<User>> centroids = km.runKmeans(users, 2);

        for (Map.Entry<Kmeans.Centroid, ArrayList<User>> entry : centroids.entrySet()) {
            System.out.println("Key = " + entry.getKey().coords +
                    ", Value = " + entry.getValue());
        }
        assertEquals(2, centroids.size());
    }



}
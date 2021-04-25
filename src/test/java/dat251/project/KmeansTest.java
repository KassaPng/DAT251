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
        User paal = new User("paal", "paal", "a");
        User kine = new User("kine", "kine", "a");
        User sofie = new User("sofie", "sofie", "a");
        User daniel = new User("daniel", "daniel", "a");
        User andy = new User("andy", "andy", "a");
        trond.addCourseToUsersListOfCourses(course);
        espen.addCourseToUsersListOfCourses(course);
        atle.addCourseToUsersListOfCourses(course);
        per.addCourseToUsersListOfCourses(course);
        paal.addCourseToUsersListOfCourses(course);
        kine.addCourseToUsersListOfCourses(course);
        sofie.addCourseToUsersListOfCourses(course);
        daniel.addCourseToUsersListOfCourses(course);
        andy.addCourseToUsersListOfCourses(course);
        //c1
        atle.setAbilities(course, "Ambition", 1);
        atle.setAbilities(course, "Work-rate", 1);
        atle.setAbilities(course, "Knowledge", 1);
        trond.setAbilities(course, "Ambition", 3);
        trond.setAbilities(course, "Work-rate", 2);
        trond.setAbilities(course, "Knowledge", 1);
        espen.setAbilities(course, "Ambition", 2);
        espen.setAbilities(course, "Work-rate", 2);
        espen.setAbilities(course, "Knowledge", 2);
        //c2
        paal.setAbilities(course, "Ambition", 5);
        paal.setAbilities(course, "Work-rate", 6);
        paal.setAbilities(course, "Knowledge", 2);
        kine.setAbilities(course, "Ambition", 6);
        kine.setAbilities(course, "Work-rate", 3);
        kine.setAbilities(course, "Knowledge", 4);
        sofie.setAbilities(course, "Ambition", 4);
        sofie.setAbilities(course, "Work-rate", 4);
        sofie.setAbilities(course, "Knowledge", 4);
        //c3
        daniel.setAbilities(course, "Ambition", 10);
        daniel.setAbilities(course, "Work-rate", 9);
        daniel.setAbilities(course, "Knowledge", 8);
        andy.setAbilities(course, "Ambition", 9);
        andy.setAbilities(course, "Work-rate", 9);
        andy.setAbilities(course, "Knowledge", 10);
        per.setAbilities(course, "Ambition", 8);
        per.setAbilities(course, "Work-rate", 8);
        per.setAbilities(course, "Knowledge", 9);
        users.add(trond);
        users.add(espen);
        users.add(atle);
        users.add(per);
        users.add(paal);
        users.add(kine);
        users.add(sofie);
        users.add(daniel);
        users.add(andy);
    }

    @Test
    public void checkThatKClustersAreCreated() {
        Kmeans km = new Kmeans(course);
        Map<Kmeans.Centroid, ArrayList<User>> centroids = km.runKmeans(users, 3);
        /*
        for (Map.Entry<Kmeans.Centroid, ArrayList<User>> entry : centroids.entrySet()) {
            System.out.println("Key = " + entry.getKey().coords +
                    ", Value = " + entry.getValue());
        }

         */
        assertEquals(3, centroids.size());
    }

    @Test
    public void findGroupTest() {
        Kmeans km = new Kmeans(course);
        Map<Kmeans.Centroid, ArrayList<User>> centroids = km.runKmeans(users, 3);
        int count = 1;
        for (Map.Entry<Kmeans.Centroid, ArrayList<User>> entry : centroids.entrySet()) {
            System.out.println("Key = " + entry.getKey().coords +
                    ", Value = " + entry.getValue());
        }
        for (Map.Entry<Kmeans.Centroid, ArrayList<User>> entry : centroids.entrySet()) {
            Group g = new Group("Group"+count,"");
            for(User u : entry.getValue()) {
                g.addUserToGroup(u);
            }
            course.addGroup(g);
            count++;
        }
        User test = new User("TEST", "TEST", "a");
        test.addCourseToUsersListOfCourses(course);
        test.setAbilities(course, "Ambition", 1);
        test.setAbilities(course, "Work-rate", 2);
        test.setAbilities(course, "Knowledge", 3);
        Group ans = km.findClosestGroup(test);
        System.out.println("found group: " +ans);
    }



}
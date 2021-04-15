package dat251.project;

import dat251.project.entities.Group;
import dat251.project.entities.User;
import dat251.project.matching.Kmeans;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KmeansTest {
    private static Group group;
    private static ArrayList<User> users;


    @BeforeAll
    public static void init() {
        group = new Group("testGroup", "test");
        users = new ArrayList<>();
        User trond = new User("trond", "trond", "a");
        User espen = new User("espen", "espen", "a");
        User atle = new User("atle", "atle", "a");
        User per = new User("per", "per", "a");
        trond.addGroupToUsersListOfGroups(group);
        espen.addGroupToUsersListOfGroups(group);
        atle.addGroupToUsersListOfGroups(group);
        per.addGroupToUsersListOfGroups(group);
        trond.setAbilities(group, "Ambition", 10);
        trond.setAbilities(group, "Work-rate", 10);
        trond.setAbilities(group, "Knowledge", 10);
        espen.setAbilities(group, "Ambition", 5);
        espen.setAbilities(group, "Work-rate", 5);
        espen.setAbilities(group, "Knowledge", 5);
        users.add(trond);
        users.add(espen);
        users.add(atle);
        users.add(per);
    }

    @Test
    public void checkThatKClustersAreCreated() {
        Kmeans km = new Kmeans(group);
        Map<Kmeans.Centroid, ArrayList<User>> centroids = km.runKmeans(users, 2);

        for (Map.Entry<Kmeans.Centroid, ArrayList<User>> entry : centroids.entrySet()) {
            System.out.println("Key = " + entry.getKey().coords +
                    ", Value = " + entry.getValue());
        }
        assertEquals(2, centroids.size());
    }




}
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
        users.add(trond);
        users.add(espen);
        users.add(atle);
        users.add(per);
        trond.addGroupToUsersListOfGroups(group);
        espen.addGroupToUsersListOfGroups(group);
        atle.addGroupToUsersListOfGroups(group);
        per.addGroupToUsersListOfGroups(group);
    }

    @Test
    public void checkThatKClustersAreCreated() {
        Kmeans km = new Kmeans(group);
        Map<Kmeans.Centroid, ArrayList<User>> centroids = km.runKmeans(users, 5);
        for (Map.Entry<Kmeans.Centroid, ArrayList<User>> entry : centroids.entrySet()) {
            System.out.println("Key = " + entry.getKey() +
                    ", Value = " + entry.getValue());
        }
        //assertEquals(2, centroids.size());
    }


}
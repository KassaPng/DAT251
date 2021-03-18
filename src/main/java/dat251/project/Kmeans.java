package dat251.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Kmeans {

    public static class Centroid { //Todo: placeholder
        public Map<String, Double> coords;

        public Centroid(Map<String, Double> coords) {
            this.coords = coords;
        }
    }

    public class User {  //TODO: Placeholder
        public String name;
        public Map<String, Double> attributes;
    }



    private static Random rand = new Random(5);//TODO: remove seed after testing
    private static int maxIterations = 1000;
    private static ArrayList<String> abilities; // TODO: Placeholder (List of all ability-names)


    public static Map<Centroid, ArrayList<User>> runKmeans(ArrayList<User> users, int k) {
        Map<Centroid, ArrayList<User>> clusters = new HashMap<>();
        Map<Centroid, ArrayList<User>> lastClusters = new HashMap<>();
        ArrayList<Centroid> centroids = new ArrayList<>();

        //create k random centroids
        for(int i = 0; i < k; i++) {
            Map<String, Double> randomMap = new HashMap<>();
            for (String ability : abilities) {
                randomMap.put(ability, (double) (rand.nextInt(10)));
            }
            centroids.add(new Centroid(randomMap));
        }



        for(int i = 0; i < maxIterations; i++) {


            for(User u : users) {
                //Assign each user to a cluster
                Centroid closest = closestCentroid(centroids, u);
                setToCluster(clusters, u, closest);
            }
            boolean done = clusters.equals(lastClusters);
            lastClusters = clusters;
            if(i == maxIterations-1 || done) {
                break;
            }

            //update centroids
            ArrayList<Centroid> newCentroids = new ArrayList<>();
            for (Centroid c : centroids) {
                newCentroids.add(calcNewCentroid(c, clusters.get(c)));
            }
            centroids = newCentroids;
            clusters = new HashMap<>();
        }

        return lastClusters;
    }




    //euclidean distance between two attribute lists.
    private static double distance(Map<String, Double> a1, Map<String, Double> a2) {
        double sum = 0;
        for(String key : a1.keySet()) {
            sum += Math.pow(a1.get(key) - a2.get(key),2); //This assumes that every user will have the same attributes in their list
        }
        return Math.sqrt(sum);
    }

    //finds nearest centroid
    private static Centroid closestCentroid(ArrayList<Centroid> centroids, User user) {
        double minDist = Double.MAX_VALUE;
        Centroid closest = null;
        for(Centroid centroid : centroids) {
            double dist = distance(centroid.coords, user.attributes);
            if(dist < minDist) {
                minDist = dist;
                closest = centroid;
            }
        }
        return closest;
    }

    private static void setToCluster(Map<Centroid, ArrayList<User>> clusters, User user, Centroid centroid) {
        if(clusters.get(centroid) == null) {
            clusters.put(centroid, new ArrayList<>());
        }
        clusters.get(centroid).add(user);
    }



    private static Centroid calcNewCentroid(Centroid centroid, ArrayList<User> users) {
        if(users == null) {
            return centroid;
        }
        Map<String, Double> average = centroid.coords;
        for(String ab : abilities) {
            average.put(ab, 0.0); // set all centroid abilities to 0.0
        }
        for(User u : users) {
            for(String ab : abilities) {
                double current = average.get(ab);
                centroid.coords.put(ab, current + u.attributes.get(ab)); //increment average values by user values
            }
        }
        for(String ab : abilities) {
            double current = average.get(ab);
            average.put(ab, current / users.size()); // find average by dividing each attribute sum by total size of cluster
        }
        return new Centroid(average);
    }
}

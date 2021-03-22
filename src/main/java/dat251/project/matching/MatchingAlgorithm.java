package dat251.project.matching;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MatchingAlgorithm {
    static Map<String, Integer> user1Abilities = new LinkedHashMap<String,Integer>();
    static Map<String, Integer> user2Abilities = new LinkedHashMap<String,Integer>();
    static Map<String, Integer> user3Abilities = new LinkedHashMap<String,Integer>();

    // using Generics
//    Map<Integer, String> hm2
//            = new HashMap<Integer, String>();
//    Map<String, Integer> user2Abilities = new HashMap{};

    public static void initializeUser1() {
        user1Abilities.put("Back-end", 7);
        user1Abilities.put("Front-end", 3);
        user1Abilities.put("Algorithms", 3);
        user1Abilities.put("Databases", 10);
        user1Abilities.put("Ambition", 3);
    }

    public static void initializeUser2() {
        user2Abilities.put("Back-end", 3);
        user2Abilities.put("Front-end", 7);
        user2Abilities.put("Algorithms", 9);
        user2Abilities.put("Databases", 4);
        user2Abilities.put("Ambition", 6);
    }

    public static void initializeUser3() {
        user3Abilities.put("Back-end", 3);
        user3Abilities.put("Front-end", 7);
        user3Abilities.put("Algorithms", 9);
        user3Abilities.put("Databases", 4);
        user3Abilities.put("Ambition", 10);
    }

    public static double groupScore(Map<String, Integer> user, ArrayList<Map<String, Integer>> group) {
        int totalScore = 0;
        for (Map<String, Integer> groupUser : group) {
            totalScore += preferenceScore(groupUser, user);
            System.out.println("adding score: " + preferenceScore(groupUser, user));

        }

        return totalScore / group.size();
    }

    public static int preferenceScore(Map<String, Integer> u1, Map<String, Integer> u2) {
        int ambition = 10 - Math.abs(u1.get("Ambition") - u2.get("Ambition"));
        int score = 0 ;
        for(String key : u1.keySet()) {
            if (!key.equals("Ambition")){
                score += Math.max(u1.get(key), u2.get(key));
//                System.out.println(key + "  " +  Math.max(u1.get(key), u2.get(key)));
            }
        }
        return score * ambition;
    }

    public static void main(String[] args) {
        initializeUser1();
        initializeUser2();
        initializeUser3();

        ArrayList<Map<String, Integer>> group = new ArrayList<Map<String, Integer>>();
        group.add(user2Abilities);
        group.add(user3Abilities);


        System.out.println("group score:" + preferenceScore(user1Abilities, user2Abilities));
        System.out.println("group score:" + preferenceScore(user1Abilities, user3Abilities));

        System.out.println("group score:" + groupScore(user1Abilities, group));
    }

//    List<String> keyList = new ArrayList<String>(map.keySet());
//      for(int i = fromIndex; i < toIndex; i++) {
//        String key = keyList.get(i);
//        String value = map.get(key);
//    ...
//    }


}

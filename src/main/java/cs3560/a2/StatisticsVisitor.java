package cs3560.a2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StatisticsVisitor implements Visitor {
    private int userCount = 0;
    private int groupCount = 0;
    private int tweetCount = 0;
    private int positiveTweetCount = 0;
    private int numInvalidIDs = 0;
    private User lastUpdatedUser = null;
    private ArrayList<String> positiveWords;
    private Set<String> idsChecked;
    private Set<Object> visited;

    public StatisticsVisitor() {
        positiveWords = new ArrayList<>();
        positiveWords.add("good");
        positiveWords.add("great");
        positiveWords.add("excellent");
        idsChecked = new HashSet<>();
        visited = new HashSet<>();
    }

    public void visitUser(User user) {
        if (!visited.contains(user)) {
            visited.add(user);
            userCount++;
            tweetCount += user.getTweets().size();
            if (!isValidID(user.getUserId())) {
                numInvalidIDs++;
            }
            if (lastUpdatedUser == null || user.getLastUpdateTime() > lastUpdatedUser.getLastUpdateTime()) {
                lastUpdatedUser = user;
            }
            for (Tweet tweet : user.getTweets()) {
                if (tweet.contains(positiveWords)) {
                    positiveTweetCount++;
                }
            }
        }
    }

    public void visitUserGroup(UserGroup userGroup) {
        if (!visited.contains(userGroup)) {
            visited.add(userGroup);
            groupCount++;
            if (!isValidID(userGroup.getGroupId())) {
                numInvalidIDs++;
            }
            for (Object member : userGroup.getUsers()) {
                if (member instanceof User) {
                    visitUser((User) member);
                } else if (member instanceof UserGroup) {
                    visitUserGroup((UserGroup) member);
                }
            }
        }
    }

    public boolean isValidID(String id) {
        if (idsChecked.contains(id)) {
            return false;
        }
        idsChecked.add(id);

        boolean hasSpace = id.contains(" ");
        return !hasSpace;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public int getNumInvalidIDs() {
        return numInvalidIDs;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser != null ? lastUpdatedUser.getUserId() : "";
    }

    public double getPositiveTweetPercentage() {
        return tweetCount > 0 ? (double) positiveTweetCount / tweetCount * 100.0 : 0;
    }
}

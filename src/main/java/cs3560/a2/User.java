package cs3560.a2;

import java.util.ArrayList;
import java.util.List;

public class User implements MiniTwitterObserver {
    private final String userId;
    private final List<User> followers;
    private final List<User> following;
    private final List<Tweet> newsFeed;
    private final List<Tweet> tweets;
    UserView userView;

    public User(String userId) {
        this.userId = userId;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.userView = null;
    }

    public void follow(User user) {
        if (user != this && !following.contains(user)) {
            following.add(user);
            user.addFollower(this);
        }
    }

    public void setView(UserView userView) {
        this.userView = userView;
    }

    public UserView getView() {
        return userView;
    }

    public void unfollow(User user) {
        if (following.contains(user)) {
            following.remove(user);
            user.removeFollower(this);
        }
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void tweet(String message) {
        Tweet newTweet = new Tweet(message, this);
        update(newTweet);
        tweets.add(newTweet);
        notifyFollowers(newTweet);
    }

    @Override
    public void update(Tweet tweet) {
        newsFeed.add(tweet);
        if (userView != null) {
            userView.refreshNewsFeed();
        }
    }

    @Override
    public void addFollower(User observer) {
        if (!followers.contains(observer)) {
            followers.add(observer);
        }
    }

    @Override
    public void removeFollower(User observer) {
        followers.remove(observer);
    }

    @Override
    public void notifyFollowers(Tweet tweet) {
        for (MiniTwitterObserver follower : followers) {
            follower.update(tweet);
        }
    }

    public List<Tweet> getNewsFeed() {
        return new ArrayList<>(newsFeed);
    }

    public String getUserId() {
        return userId;
    }

    public void accept(Visitor visitor) {
        visitor.visitUser(this);
    }

    public void enableUserView() {
        if (userView != null) {
            userView.setVisible(true);
        }
    }

    public void disableUserView() {
        if (userView != null) {
            userView.setVisible(false);
        }
    }

    public ArrayList<User> getFollowers() {
        return new ArrayList<>(followers);
    }

    public ArrayList<User> getFollowing() {
        return new ArrayList<>(following);
    }

    @Override
    public String toString() {
        return userId;
    }
}

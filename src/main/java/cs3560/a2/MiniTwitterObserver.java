package cs3560.a2;

public interface MiniTwitterObserver {
    void update(Tweet tweet);

    void addFollower(User observer);

    void removeFollower(User observer);

    void notifyFollowers(Tweet tweet);
}


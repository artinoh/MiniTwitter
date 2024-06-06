package cs3560.a2;

import java.util.ArrayList;
import java.util.Date;

public class Tweet {
    private final String message;
    private final User author;
    private final Date timestamp;

    public Tweet(String message, User author) {
        this.message = message;
        this.author = author;
        timestamp = new Date();
    }

    public String getMessage() {
        return message;
    }

    public User getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return message + " by " + getAuthor().getUserId() + " at " + getTimestamp().toString();
    }

    public boolean contains(ArrayList<String> words) {
        //check without case sensitivity
        for (String word : words) {
            if (message.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Tweet)) {
            return false;
        }
        Tweet tweet = (Tweet) obj;
        return message.equals(tweet.message) && author.equals(tweet.author) && timestamp.equals(tweet.timestamp);
    }
}


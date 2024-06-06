package cs3560.a2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class UserView extends JFrame {
    private User user;
    private DefaultListModel<String> newsFeedModel;
    private JList<String> newsFeedList;
    private JTextField followTextField;
    private JTextField tweetTextField;

    public UserView(User user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        configureFrame();
        setupNewsFeedPanel();
        setupActionPanel();
        setupUserStatsPanel();
        pack();
    }

    private void configureFrame() {
        setTitle("User View - " + user.getUserId());
        setLayout(new BorderLayout());
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
    }

    private void setupNewsFeedPanel() {
        newsFeedModel = new DefaultListModel<>();
        user.getNewsFeed().forEach(tweet -> newsFeedModel.addElement(tweet.toString()));
        newsFeedList = new JList<>(newsFeedModel);
        add(new JScrollPane(newsFeedList), BorderLayout.CENTER);
    }

    private void setupActionPanel() {
        JPanel southPanel = new JPanel(new GridLayout(2, 2));
        followTextField = new JTextField();
        JButton followButton = new JButton("Follow");
        tweetTextField = new JTextField();
        JButton tweetButton = new JButton("Tweet");

        followButton.addActionListener(e -> handleFollow());
        tweetButton.addActionListener(e -> handleTweet());

        southPanel.add(followTextField);
        southPanel.add(followButton);
        southPanel.add(tweetTextField);
        southPanel.add(tweetButton);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void handleFollow() {
        String userIdToFollow = followTextField.getText();
        User toFollow = AdminControlPanel.getInstance().findUser(userIdToFollow);
        if (toFollow != null) {
            user.follow(toFollow);
            JOptionPane.showMessageDialog(this, "Following " + userIdToFollow);
            followTextField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "User not found: " + userIdToFollow, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleTweet() {
        String tweetContent = tweetTextField.getText();
        user.tweet(tweetContent);
        tweetTextField.setText("");
    }

    public void refreshNewsFeed() {
        newsFeedModel.clear();
        for (Tweet tweet : user.getNewsFeed()) {
            newsFeedModel.addElement(tweet.toString());
            addDividerToNewsFeed();
        }
    }

    private void setupUserStatsPanel() {
        JPanel userStatsPanel = new JPanel(new GridLayout(1, 2));
        JButton followersButton = new JButton("Followers");
        JButton followingButton = new JButton("Following");

        followersButton.addActionListener(e -> showFollowers());
        followingButton.addActionListener(e -> showFollowing());

        userStatsPanel.add(followersButton);
        userStatsPanel.add(followingButton);

        add(userStatsPanel, BorderLayout.NORTH);
    }

    private void showFollowers() {
        ArrayList<User> followers = user.getFollowers();
        ArrayList<String> followerIds = new ArrayList<>();
        for (User follower : followers) {
            followerIds.add(follower.getUserId());
        }
        JOptionPane.showMessageDialog(this, String.join(", ", followerIds), "Followers", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showFollowing() {
        ArrayList<User> following = user.getFollowing();
        ArrayList<String> followingIds = new ArrayList<>();
        for (User followee : following) {
            followingIds.add(followee.getUserId());
        }
        JOptionPane.showMessageDialog(this, String.join(", ", followingIds), "Following", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addDividerToNewsFeed() {
        newsFeedModel.addElement("--------------------------------------------------");
    }
}

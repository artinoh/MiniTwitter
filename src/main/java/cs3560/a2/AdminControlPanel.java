package cs3560.a2;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.HashMap;

public class AdminControlPanel {
    private static AdminControlPanel instance;
    private JFrame mainFrame;
    private JTree userTree;
    private DefaultTreeModel treeModel;
    private final HashMap<String, User> users = new HashMap<>();
    private JScrollPane treeViewPanel;
    DefaultMutableTreeNode rootNode;

    private AdminControlPanel() {
        createGUI();
    }

    public static synchronized AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }

    private void createGUI() {
        setupMainFrame();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTreeViewPanel(), createControlPanel());
        splitPane.setDividerLocation(300);
        mainFrame.add(splitPane, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private void setupMainFrame() {
        mainFrame = new JFrame("Admin Control Panel");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 400);
        mainFrame.setLayout(new BorderLayout());
    }

    private JScrollPane createTreeViewPanel() {
        UserGroup rootGroup = new UserGroup("Root");
        rootNode = new DefaultMutableTreeNode(rootGroup);
        treeModel = new DefaultTreeModel(rootNode);
        userTree = new JTree(treeModel);
        return new JScrollPane(userTree);
    }

    private JPanel createControlPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);

        setupControlPanelComponents(rightPanel, gbc);
        return rightPanel;
    }

    private void setupControlPanelComponents(JPanel rightPanel, GridBagConstraints gbc) {
        setupUserControls(rightPanel, gbc);
        setupGroupControls(rightPanel, gbc);
        setupUserViewButton(rightPanel, gbc);
        setupStatisticsButtons(rightPanel, gbc);
    }

    private void setupUserControls(JPanel panel, GridBagConstraints gbc) {
        addComponent(panel, new JLabel("User ID:"), gbc, 0, 0);
        JTextField userIdTextField = new JTextField(10);
        addComponent(panel, userIdTextField, gbc, 1, 0);
        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> addUser(userIdTextField.getText()));
        addComponent(panel, addUserButton, gbc, 2, 0);
    }

    private void setupGroupControls(JPanel panel, GridBagConstraints gbc) {
        addComponent(panel, new JLabel("Group ID:"), gbc, 0, 1);
        JTextField groupIdTextField = new JTextField(10);
        addComponent(panel, groupIdTextField, gbc, 1, 1);
        JButton addGroupButton = new JButton("Add Group");
        addGroupButton.addActionListener(e -> createGroup(groupIdTextField.getText()));
        addComponent(panel, addGroupButton, gbc, 2, 1);
    }

    private void setupUserViewButton(JPanel panel, GridBagConstraints gbc) {
        JButton openUserViewButton = new JButton("Open User View");
        openUserViewButton.addActionListener(e -> openUserView());
        gbc.gridwidth = 3;
        addComponent(panel, openUserViewButton, gbc, 0, 2);
    }

    private void addComponent(JPanel panel, Component component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    private void openUserView() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();
        if (selectedNode != null && selectedNode.getUserObject() instanceof User) {
            User user = (User) selectedNode.getUserObject();
            user.enableUserView();
        }
    }

    private void createGroup(String groupId) {
        if (groupId.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Group ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        handleGroupCreation(groupId);
    }

    private void handleGroupCreation(String groupId) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();

        if (selectedNode == null || selectedNode.getUserObject() instanceof User) {
            selectedNode = rootNode;
        }

        if (selectedNode != null && selectedNode.getUserObject() instanceof UserGroup) {
            UserGroup parentGroup = (UserGroup) selectedNode.getUserObject();
            UserGroup newGroup = new UserGroup(groupId);
            parentGroup.addSubgroup(newGroup);
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(newGroup);
            selectedNode.add(groupNode);
            treeModel.reload(selectedNode);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a valid user group to add a new group.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser(String userId) {
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "User ID cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userId.contains(" ")) {
            JOptionPane.showMessageDialog(mainFrame, "User ID cannot contain spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (users.containsKey(userId)) {
            JOptionPane.showMessageDialog(mainFrame, "User ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        handleUserAddition(userId);
    }

    private void handleUserAddition(String userId) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) userTree.getLastSelectedPathComponent();

        if (selectedNode == null || selectedNode.getUserObject() instanceof User) {
            selectedNode = rootNode;
        }

        if (selectedNode != null && selectedNode.getUserObject() instanceof UserGroup) {
            UserGroup parentGroup = (UserGroup) selectedNode.getUserObject();
            User newUser = new User(userId);
            UserView userView = new UserView(newUser);
            newUser.setView(userView);
            parentGroup.addUser(newUser);
            users.put(userId, newUser);
            DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(newUser);
            selectedNode.add(userNode);
            treeModel.reload(selectedNode);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please select a valid user group to add a new user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User findUser(String userId) {
        return users.get(userId);
    }

    public void display() {
        mainFrame.setVisible(true);
    }

    private void setupStatisticsButtons(JPanel panel, GridBagConstraints gbc) {
        JButton userTotalButton = new JButton("Show User Total");
        JButton groupTotalButton = new JButton("Show Group Total");
        JButton messageTotalButton = new JButton("Show Messages Total");
        JButton positivePercentageButton = new JButton("Show Positive Percentage");
        JButton lastUpdatedUserButton = new JButton("Show Last Updated User");
        JButton checkValidIDsButton = new JButton("Check Valid IDs");

        userTotalButton.addActionListener(e -> displayUserTotal());
        groupTotalButton.addActionListener(e -> displayGroupTotal());
        messageTotalButton.addActionListener(e -> displayMessagesTotal());
        positivePercentageButton.addActionListener(e -> displayPositivePercentage());
        lastUpdatedUserButton.addActionListener(e -> displayLastUpdatedUser());
        checkValidIDsButton.addActionListener(e -> checkValidIDs());

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(userTotalButton, gbc);

        gbc.gridx = 2;
        panel.add(groupTotalButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(messageTotalButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        panel.add(positivePercentageButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lastUpdatedUserButton, gbc);

        gbc.gridx = 2;
        panel.add(checkValidIDsButton, gbc);
    }

    private void displayUserTotal() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitAllNodes(rootNode, visitor);
        JOptionPane.showMessageDialog(mainFrame, "Total Users: " + visitor.getUserCount());
    }

    private void displayGroupTotal() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitAllNodes(rootNode, visitor);
        JOptionPane.showMessageDialog(mainFrame, "Total Groups: " + visitor.getGroupCount());
    }

    private void displayMessagesTotal() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitAllNodes(rootNode, visitor);
        JOptionPane.showMessageDialog(mainFrame, "Total Messages: " + visitor.getTweetCount());
    }

    private void displayPositivePercentage() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitAllNodes(rootNode, visitor);
        JOptionPane.showMessageDialog(mainFrame, "Positive Messages Percentage: " + visitor.getPositiveTweetPercentage() + "%");
    }

    private void displayLastUpdatedUser() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitAllNodes(rootNode, visitor);
        JOptionPane.showMessageDialog(mainFrame, "Last Updated User: " + visitor.getLastUpdatedUser());
    }

    private void checkValidIDs() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        visitAllNodes(rootNode, visitor);
        String message;
        int numInvalidIDs = visitor.getNumInvalidIDs();
        if (numInvalidIDs > 0) {
            message = "Invalid IDs: " + numInvalidIDs;
        } else {
            message = "All IDs are valid.";
        }
        JOptionPane.showMessageDialog(mainFrame, message);
    }

    private void visitAllNodes(DefaultMutableTreeNode node, Visitor visitor) {
        Object userObject = node.getUserObject();
        if (userObject instanceof UserGroup) {
            visitor.visitUserGroup((UserGroup) userObject);
        } else if (userObject instanceof User) {
            visitor.visitUser((User) userObject);
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            visitAllNodes((DefaultMutableTreeNode) node.getChildAt(i), visitor);
        }
    }
}

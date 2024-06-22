package cs3560.a2;

import java.util.ArrayList;
import java.util.List;

public class UserGroup {
    private String groupId;
    private List<User> users;
    private List<UserGroup> subgroups;
    long creationTime;

    public UserGroup(String groupId) {
        this.groupId = groupId;
        this.users = new ArrayList<>();
        this.subgroups = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
    }

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    public void addSubgroup(UserGroup group) {
        if (!subgroups.contains(group)) {
            subgroups.add(group);
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<UserGroup> getSubgroups() {
        return subgroups;
    }

    public void accept(Visitor visitor) {
        visitor.visitUserGroup(this);
        for (User user : users) {
            user.accept(visitor);
        }
        for (UserGroup subgroup : subgroups) {
            subgroup.accept(visitor);
        }
    }

    @Override
    public String toString() {
        return groupId + " Created at: " + creationTime;
    }

}

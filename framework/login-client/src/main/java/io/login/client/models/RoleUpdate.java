package io.login.client.models;

import java.util.List;

public class RoleUpdate {
    private String userName;
    private List<UserRole> userRole;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<UserRole> userRole) {
        this.userRole = userRole;
    }
}

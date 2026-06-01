package com.example.asterismos;

public class UserAdministration {
    private static UserAdministration instance;
    private String role = "user";

    private UserAdministration() {    }

    public static synchronized UserAdministration getInstance() {
        if (instance == null) {
            instance = new UserAdministration();
        }
        return instance;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "admin".equals(this.role);
    }
}

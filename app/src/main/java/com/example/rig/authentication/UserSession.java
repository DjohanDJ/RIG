package com.example.rig.authentication;

import com.example.rig.models.User;

public class UserSession {

    private static User currentUser = null;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserSession.currentUser = currentUser;
    }
}

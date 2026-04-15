package com.readora.user;

import com.readora.user.UserAccount;

public final class SessionManager {

    private static UserAccount currentUser;

    private SessionManager() {
    }

    public static void setCurrentUser(UserAccount user) {
        currentUser = user;
    }

    public static UserAccount getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
}
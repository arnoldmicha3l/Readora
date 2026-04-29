package com.readora.user;

public final class SessionManager {

    private static UserAccount currentUser;

    private SessionManager() {}

    public static synchronized void setCurrentUser(UserAccount user) {
        currentUser = user;
    }

    public static synchronized UserAccount getCurrentUser() {
        return currentUser;
    }

    public static synchronized boolean isLoggedIn() {
        return currentUser != null;
    }

    public static synchronized void clearSession() {
        currentUser = null;
    }
}
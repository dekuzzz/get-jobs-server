package com.getjob.backend.util;

public class UserContext {
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> emailHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> userTypeHolder = new ThreadLocal<>();

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void setEmail(String email) {
        emailHolder.set(email);
    }

    public static String getEmail() {
        return emailHolder.get();
    }

    public static void setUserType(String userType) {
        userTypeHolder.set(userType);
    }

    public static String getUserType() {
        return userTypeHolder.get();
    }

    public static void clear() {
        userIdHolder.remove();
        emailHolder.remove();
        userTypeHolder.remove();
    }
}


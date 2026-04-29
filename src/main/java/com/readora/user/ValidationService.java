package com.readora.user;

public final class ValidationService {

    private ValidationService() {}

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return true;
        }

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhone(String phone) {
        if (isBlank(phone)) {
            return true;
        }

        return phone.matches("^[0-9+\\-\\s]{7,20}$");
    }

    public static boolean isValidAge(String ageText) {
        if (isBlank(ageText)) {
            return true;
        }

        try {
            int age = Integer.parseInt(ageText.trim());
            return age >= 1 && age <= 120;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
package com.fsocial.accountservice.util;

public class PasswordUtils {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
    public static final int PASSWORD_LENGTH = 8;
}

package com.contactmanager.util;

public class ContactManagerException extends Exception {
    public ContactManagerException(String message) {
        super(message);
    }

    public ContactManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
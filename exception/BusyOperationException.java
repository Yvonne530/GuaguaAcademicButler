package org.example.edumanagementservice.exception;

public class BusyOperationException extends RuntimeException {
    public BusyOperationException(String message) {
        super(message);
    }
}
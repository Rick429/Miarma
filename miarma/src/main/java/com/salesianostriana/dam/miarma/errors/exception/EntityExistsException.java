package com.salesianostriana.dam.miarma.errors.exception;

public class EntityExistsException extends RuntimeException{
    public EntityExistsException(String message) {
        super(String.format(message));
    }
}

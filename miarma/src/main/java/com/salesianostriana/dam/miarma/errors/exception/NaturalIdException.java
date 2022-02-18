package com.salesianostriana.dam.miarma.errors.exception;

public class NaturalIdException extends EntityExistsException{

    public NaturalIdException(String message) {
        super(message);
    }
}

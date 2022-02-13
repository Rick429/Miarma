package com.salesianostriana.dam.miarma.errors.exception;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String message) {
        super(String.format(message));
    }
}

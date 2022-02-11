package com.salesianostriana.dam.miarma.errors.exception;

public class ListEntityNotFoundException extends EntidadNoEncontradaException{

    public ListEntityNotFoundException(Class clazz) {
        super(String.format("No se pueden encontrar elementos del tipo %s ", clazz.getName()));
    }
}
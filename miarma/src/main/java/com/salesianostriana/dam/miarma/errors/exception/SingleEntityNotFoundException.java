package com.salesianostriana.dam.miarma.errors.exception;

public class SingleEntityNotFoundException extends EntidadNoEncontradaException {

    public SingleEntityNotFoundException(String id, Class clazz) {
        super(String.format("No se puede encontrar una entidad del tipo %s con ID: %s", clazz.getName(), id));
    }
}

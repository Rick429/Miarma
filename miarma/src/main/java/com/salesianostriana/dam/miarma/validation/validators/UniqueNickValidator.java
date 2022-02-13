package com.salesianostriana.dam.miarma.validation.validators;

import com.salesianostriana.dam.miarma.users.repository.UserEntityRepository;
import com.salesianostriana.dam.miarma.validation.anotations.UniqueNick;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNickValidator implements ConstraintValidator<UniqueNick, String> {

    @Autowired
    private UserEntityRepository repository;

    @Override
    public void initialize(UniqueNick constraintAnnotation) { }

    @Override
    public boolean isValid(String nick, ConstraintValidatorContext context) {
        return !repository.existsByNick(nick);
    }
}

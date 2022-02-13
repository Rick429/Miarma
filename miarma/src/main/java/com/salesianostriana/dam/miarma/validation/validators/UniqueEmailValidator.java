package com.salesianostriana.dam.miarma.validation.validators;

import com.salesianostriana.dam.miarma.users.repository.UserEntityRepository;
import com.salesianostriana.dam.miarma.validation.anotations.UniqueEmail;
import com.salesianostriana.dam.miarma.validation.anotations.UniqueNick;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserEntityRepository repository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) { }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !repository.existsByEmail(email);
    }


}

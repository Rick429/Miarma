package com.salesianostriana.dam.miarma.validation.anotations;

import com.salesianostriana.dam.miarma.validation.validators.UniqueNickValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueNickValidator.class)
@Documented
public @interface UniqueNick {

    String message() default "El nick debe ser único";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

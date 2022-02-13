package com.salesianostriana.dam.miarma.validation.anotations;

import com.salesianostriana.dam.miarma.validation.validators.UniqueEmailValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "El email debe ser único";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

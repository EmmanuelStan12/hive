package com.bytebard.sharespace.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default """
            Passwords must include:\s
            - Minimum length of 8 characters.
            - Includes both uppercase and lowercase letters.
            - Contains at least one numeric digit.
            - Includes special characters, such as @, #, or ! (optional but recommended).
            """;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

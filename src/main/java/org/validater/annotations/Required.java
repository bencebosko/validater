package org.validater.annotations;

import org.validater.internal.RequiredValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation for checking if a field is null.
 * */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = RequiredValidator.class)
public @interface Required {
    String message() default "field is null";
}

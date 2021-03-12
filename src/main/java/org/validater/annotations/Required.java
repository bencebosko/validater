package org.validater.annotations;

import org.validater.internal.RequiredValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = RequiredValidator.class)
public @interface Required {
    String message() default "field is required";
}

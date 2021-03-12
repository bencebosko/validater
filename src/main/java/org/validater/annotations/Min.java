package org.validater.annotations;

import org.validater.internal.MinValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = MinValidator.class)
public @interface Min {
    long value();
    String message() default "value is less than min";
}

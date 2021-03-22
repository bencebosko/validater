package org.validater.annotations;

import org.validater.internal.LengthValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation for checking the length of Strings. Using the validation on other types will throw FieldValidationException.
 * Null values are not validated.
 * */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = LengthValidator.class)
public @interface Length {
    int value();
    String message() default "field exceeds length";
}

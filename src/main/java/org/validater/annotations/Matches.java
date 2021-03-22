package org.validater.annotations;

import org.validater.internal.MatchesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation for checking if a String matches a pattern. Null values are not validated.
 * */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = MatchesValidator.class)
public @interface Matches {
    String regex();
    String message() default "not matches the pattern";
}

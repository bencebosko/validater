package org.validater.annotations;

import org.validater.internal.DecimalMinValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation for checking if a numeric value is greater than the given limit. The validation is usable on any type of Number, but
 * the field's value will be converted to double. Null values are not validated.
 * */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = DecimalMinValidator.class)
public @interface DecimalMin {
    double value();
    String message() default "value is less than min";
}

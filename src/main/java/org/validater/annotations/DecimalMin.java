package org.validater.annotations;

import org.validater.internal.DecimalMinValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = DecimalMinValidator.class)
public @interface DecimalMin {
    double value();
    String message() default "value is less than min";
}

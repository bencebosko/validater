package org.validater.annotations;

import org.validater.internal.DecimalMaxValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = DecimalMaxValidator.class)
public @interface DecimalMax {
    double value();
    String message() default "value is greater than max";
}

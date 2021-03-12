package org.validater.annotations;

import org.validater.internal.MaxValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Validation(validator = MaxValidator.class)
public @interface Max {
    long value();
    String message() default "value is greater than max";
}

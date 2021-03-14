package org.validater.annotations;

import org.validater.FieldValidator;

import java.lang.annotation.*;

/**
 * We can create custom field validations with this annotation.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Validation {
    Class<? extends FieldValidator> validator();
}

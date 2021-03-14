package org.validater.annotations;

import org.validater.Validator;
import java.lang.annotation.*;

/**
 * We can map validators to objects by annotating them with this annotation.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidatedBy {
    Class<? extends Validator> validator();
}

package org.validater.annotations;

import org.validater.Validator;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidatedBy {
    Class<? extends Validator> validator();
}

package org.validater.internal;

import org.jetbrains.annotations.Nullable;
import org.validater.FieldValidator;
import org.validater.annotations.DecimalMax;
import org.validater.ValidationError;
import java.util.List;

public class DecimalMaxValidator implements FieldValidator<Number, DecimalMax> {

    @Override
    public Class<Number> fieldType() {
        return Number.class;
    }

    @Override
    public void validate(@Nullable Number value, DecimalMax annotation, List<ValidationError> errors) {
        if(value == null) {
            errors.add(new ValidationError("value is null"));
        }
        else if(value.doubleValue() > annotation.value())
            errors.add(new ValidationError(annotation.message()));
    }
}

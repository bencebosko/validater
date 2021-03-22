package org.validater.internal;

import org.jetbrains.annotations.Nullable;
import org.validater.FieldValidator;
import org.validater.ValidationError;
import org.validater.annotations.DecimalMin;
import java.util.List;

public final class DecimalMinValidator implements FieldValidator<Number, DecimalMin> {

    @Override
    public Class<Number> fieldType() {
        return Number.class;
    }

    @Override
    public void validate(@Nullable Number value, DecimalMin annotation, List<ValidationError> errors) {
        if(value != null && value.doubleValue() < annotation.value()) {
            errors.add(new ValidationError(annotation.message()));
        }
    }
}

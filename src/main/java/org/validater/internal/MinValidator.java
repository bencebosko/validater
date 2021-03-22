package org.validater.internal;

import org.jetbrains.annotations.Nullable;
import org.validater.FieldValidator;
import org.validater.ValidationError;
import org.validater.annotations.Min;
import java.util.List;

public final class MinValidator implements FieldValidator<Number, Min> {

    @Override
    public Class<Number> fieldType() {
        return Number.class;
    }

    @Override
    public void validate(@Nullable Number value, Min annotation, List<ValidationError> errors) {
        if (value != null && value.longValue() < annotation.value())
            errors.add(new ValidationError(annotation.message()));
    }
}

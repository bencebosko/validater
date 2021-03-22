package org.validater.internal;

import org.springframework.lang.Nullable;
import org.validater.FieldValidator;
import org.validater.ValidationError;
import org.validater.annotations.Length;
import java.util.List;

public final class LengthValidator implements FieldValidator<String, Length> {

    @Override
    public Class<String> fieldType() {
        return String.class;
    }

    @Override
    public void validate(@Nullable String value, Length annotation, List<ValidationError> errors) {
        if(value != null && value.length() > annotation.value())
            errors.add(new ValidationError(annotation.message()));
    }
}

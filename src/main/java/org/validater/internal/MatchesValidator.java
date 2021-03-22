package org.validater.internal;

import org.jetbrains.annotations.Nullable;
import org.validater.FieldValidator;
import org.validater.ValidationError;
import org.validater.annotations.Matches;
import java.util.List;

public final class MatchesValidator implements FieldValidator<String, Matches> {

    @Override
    public Class<String> fieldType() {
        return String.class;
    }

    @Override
    public void validate(@Nullable String value, Matches annotation, List<ValidationError> errors) {
        if(value != null && !value.matches(annotation.regex()))
            errors.add(new ValidationError(annotation.message()));
    }
}

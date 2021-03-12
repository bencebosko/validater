package org.validater.custom;

import org.validater.ValidationError;
import org.validater.Validator;
import java.util.List;
import java.util.Map;

public class TestDtoValidatorEx implements Validator<TestDto> {

    private TestDtoValidatorEx() {}

    @Override
    public Class<TestDto> type() {
        return null;
    }

    @Override
    public void validate(TestDto value, Map<String, List<ValidationError>> errorsByField) { }
}

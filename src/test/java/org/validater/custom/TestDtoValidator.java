package org.validater.custom;

import org.validater.ValidationError;
import org.validater.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestDtoValidator implements Validator<TestDto> {

    @Override
    public Class<TestDto> type() {
        return TestDto.class;
    }

    @Override
    public void validate(TestDto value, Map<String, List<ValidationError>> errorsByField) {
        errorsByField.put("email", Arrays.asList(new ValidationError("invalid email: " + value.getEmail())));
        errorsByField.put("userName", Arrays.asList(new ValidationError("invalid username: " + value.getUserName())));
        errorsByField.put("age", Arrays.asList(new ValidationError("invalid age: " + value.getAge())));
    }
}

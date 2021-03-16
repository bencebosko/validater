package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.validater.annotations.ValidatedBy;
import org.validater.custom.TestDto;
import org.validater.custom.TestDtoValidator;
import org.validater.custom.TestDtoValidatorEx;
import org.validater.exceptions.ObjectValidationException;
import org.validater.exceptions.ValidatorInstantiationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private static ValidationRunner validationRunner;

    @BeforeAll
    public static void init() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        validationRunner = factory.getValidationRunner();
    }

     @Test
     public void testInstantiationException() {

        @ValidatedBy(validator = TestDtoValidatorEx.class)
        class TestDto {}

         ValidatorInstantiationException exception = assertThrows(ValidatorInstantiationException.class, () -> {
             validationRunner.validate(new TestDto());
         });
         assertEquals("TestDtoValidatorEx", exception.getValidatorType().getSimpleName());
     }

    @Test
    public void testObjectTypeException() {

        @ValidatedBy(validator = TestDtoValidator.class)
        class TestDto{}

        ObjectValidationException exception = assertThrows(ObjectValidationException.class, () -> {
            validationRunner.validate(new TestDto());
        });
        assertEquals("TestDto", exception.getObject().getClass().getSimpleName());
    }

    @Test
    public void testValidation() {

        TestDto testDto = new TestDto();

        ValidationResult expected = new ValidationResult(new HashMap<String, List<ValidationError>>() {{
            put("email", Arrays.asList(new ValidationError("invalid email: " + testDto.getEmail())));
            put("userName", Arrays.asList(new ValidationError("invalid username: " + testDto.getUserName())));
            put("age", Arrays.asList(new ValidationError("invalid age: " + testDto.getAge())));
        }});

        ValidationResult result = validationRunner.validate(testDto);

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }
}

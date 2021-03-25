package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.validater.annotations.*;
import org.validater.custom.TestDto;
import org.validater.custom.TestDtoValidator;
import org.validater.custom.TestDtoValidatorEx;
import org.validater.exceptions.FieldValidationException;
import org.validater.exceptions.ObjectValidationException;
import org.validater.exceptions.ValidatorInstantiationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationRunnerTest {

    private static ValidationRunner validationRunner;

    @BeforeAll
    public static void init() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        validationRunner = factory.getValidationRunner();
    }

    @Test
    public void testFieldTypeException() {

        class TestObject {
            @DecimalMax(168.2)
            private int salary = 170;

            @Max(170)
            private float height = 175.4f;

            @Matches(regex = "30")
            private int age = 30;
        }

        FieldValidationException exception = assertThrows(FieldValidationException.class, () -> {
            validationRunner.validate(new TestObject());
        });
        assertEquals("age", exception.getField().getName());
    }

    @Test
    public void testInstantiationException() {

        class TestObject {
            @TestAnnotationEx
            String field;
        }

        ValidatorInstantiationException exception = assertThrows(ValidatorInstantiationException.class, () -> {
            validationRunner.validate(new TestObject());
        });
        assertEquals("TestAnnotationExValidator", exception.getValidatorType().getSimpleName());
    }

    @Test
    public void testMatches() {

        class TestObject {
            @Matches(regex = "")
            String str1;

            @Matches(regex = "[A-Za-z]*")
            String str2 = "Abc12";

            @Matches(regex = "[A-Za-z]*")
            String str3 = "Abcd";

        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("str2", List.of(new ValidationError("not matches the pattern")));
        }});

        ValidationResult result = validationRunner.validate(new TestObject());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testRequired() {

        class TestObject {
            @Required
            String field1;

            @Required
            String field2 = "not null";

            @Required
            String field3 = "";
        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("field1", List.of(new ValidationError("field is null")));
            put("field3", List.of(new ValidationError("field is null")));
        }});

        ValidationResult result = validationRunner.validate(new TestObject());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testDecimal() {

        class TestObject {
            @DecimalMax(45.78)
            Double field1;

            @DecimalMax(50)
            double field2 = 50.0001;

            @DecimalMax(45)
            int field3 = 40;

            @DecimalMin(70)
            Double field4;

            @DecimalMin(50)
            double field5 = 49.9999;

            @DecimalMin(50)
            int field6 = 62;
        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("field2", List.of(new ValidationError("value is greater than max")));
            put("field5", List.of(new ValidationError("value is less than min")));
        }});

        ValidationResult result = validationRunner.validate(new TestObject());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testInt() {

        class TestObject {
            @Max(50)
            Double field1;

            @Max(50)
            double field2 = 50.01;

            @Max(50)
            double field3 = 51.1;

            @Min(62)
            Integer field4;

            @Min(77)
            int field5 = 76;

            @Min(50)
            int field6 = 50;
        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("field3", List.of(new ValidationError("value is greater than max")));
            put("field5", List.of(new ValidationError("value is less than min")));
        }});

        ValidationResult result = validationRunner.validate(new TestObject());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testLength() {
        class TestObject {
            @Length(10)
            String field1;

            @Length(10)
            String field2 = "not null";

            @Length(2)
            String field3 = "abc";
        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("field3", List.of(new ValidationError("field exceeds length")));
        }});

        ValidationResult result = validationRunner.validate(new TestObject());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testMultiple() {

        class TestObject {

            @Required
            @DecimalMax(60)
            @DecimalMin(70)
            @Min(80)
            double field1 = 62.44;

        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("field1", List.of(
                    new ValidationError("value is greater than max"),
                    new ValidationError("value is less than min")));
        }});

        ValidationResult result = validationRunner.validate(new TestObject());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testGeneric() {

        class TestObject {

            @TestAnnotationGeneric
            List<Integer> numbers = Arrays.asList(1, 2, 3);
        }

        ClassCastException exception = assertThrows(ClassCastException.class, () -> {
            validationRunner.validate(new TestObject());
        });
    }

    @Test
    public void testSuperClass() {

        class TestObject {
            @Max(10)
            int field1 = 20;

            @Min(40)
            int field2 = 30;

        }

        class TestObjectSub extends TestObject {
            @Min(50)
            int field1 = 30;
        }

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("field1", List.of(new ValidationError("value is less than min")));
            put("field2", List.of(new ValidationError("value is less than min")));
        }});

        ValidationResult result = validationRunner.validate(new TestObjectSub());

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }

    @Test
    public void testValidatorInstantiationException() {

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

        ValidationResult expected = new ValidationResult(new HashMap<>() {{
            put("email", List.of(new ValidationError("invalid email: " + testDto.getEmail())));
            put("userName", List.of(new ValidationError("invalid username: " + testDto.getUserName())));
            put("age", List.of(new ValidationError("invalid age: " + testDto.getAge())));
        }});

        ValidationResult result = validationRunner.validate(testDto);

        assertFalse(result.isValid());
        assertEquals(expected, result);
    }
}

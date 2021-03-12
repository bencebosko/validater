package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Max;
import org.validater.annotations.Min;
import org.validater.annotations.Required;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FieldValidatorCacheTest {

    static ValidationRunner validationRunner;

    @BeforeAll
    public static void init() {
        validationRunner = new ValidationRunner();
    }

    @Test
    public void testCache() {

        class TestObject {

            @Required
            @Max(60)
            @Min(40)
            int field1 = 31;

            @Min(80)
            int field2 = 77;

            int field3 = 42;

            int field4 = 82;

            @Max(120)
            @Min(100)
            int field5 = 92;

            int filed6 = 100;

            @Max(120)
            int field7 = 100;

        }

        ValidationResult expected = new ValidationResult(new HashMap<String, List<ValidationError>>() {{
            put("field1", Arrays.asList(new ValidationError("value is less than min")));
            put("field2", Arrays.asList(new ValidationError("value is less than min")));
            put("field5", Arrays.asList(new ValidationError("value is less than min")));
        }});

        List<ValidationResult> results = new ArrayList<>();

        for(int i=0; i<100000; i++) {
            results.add(validationRunner.validate(new TestObject()));
        }

        for(ValidationResult result : results) {
            assertFalse(result.isValid());
            assertEquals(expected, result);
        }
    }

    @Disabled
    @Test
    public void testWithoutCache() {

        class TestObject {

            @Required
            @Max(60)
            @Min(40)
            int field1 = 31;

            @Min(80)
            int field2 = 77;

            int field3 = 42;

            int field4 = 82;

            @Max(120)
            @Min(100)
            int field5 = 92;

            int filed6 = 100;

            @Max(120)
            int field7 = 100;
        }

        ValidationResult expected = new ValidationResult(new HashMap<String, List<ValidationError>>() {{
            put("field1", Arrays.asList(new ValidationError("value is less than min")));
            put("field2", Arrays.asList(new ValidationError("value is less than min")));
            put("field5", Arrays.asList(new ValidationError("value is less than min")));
        }});

        List<ValidationResult> results = new ArrayList<>();

        validationRunner.cache(false);

        for(int i=0; i<100000; i++) {
            results.add(validationRunner.validate(new TestObject()));
        }

        for(ValidationResult result : results) {
            assertFalse(result.isValid());
            assertEquals(expected, result);
        }

        validationRunner.cache(true);
    }
}

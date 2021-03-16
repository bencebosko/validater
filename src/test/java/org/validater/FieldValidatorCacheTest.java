package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Max;
import org.validater.annotations.Min;
import org.validater.annotations.Required;
import org.validater.dto.TestDto1;
import org.validater.dto.TestDto2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldValidatorCacheTest {

    private static ValidationRunner validationRunner;

    @BeforeAll
    public static void init() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        validationRunner = factory.getValidationRunner();
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

    @Test
    public void testEagerCache() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        factory.setPackagesToScan("org.validater.dto");
        validationRunner = factory.getValidationRunner();

        ValidationCache cache = validationRunner.getCache();

        assertTrue(cache.isCached(TestDto1.class));
        assertTrue(cache.isCached(TestDto2.class));
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

        validationRunner.setCache(false);

        for(int i=0; i<100000; i++) {
            results.add(validationRunner.validate(new TestObject()));
        }

        for(ValidationResult result : results) {
            assertFalse(result.isValid());
            assertEquals(expected, result);
        }

        validationRunner.setCache(true);
    }
}

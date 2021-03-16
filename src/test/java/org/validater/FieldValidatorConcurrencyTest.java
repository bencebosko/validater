
package org.validater;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Max;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldValidatorConcurrencyTest {

    private static ValidationRunner validationRunner;

    @Disabled
    @Test
    public void testConcurrentCache() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();

        validationRunner = factory.getValidationRunner();

        class TestObject {

            @Max(20)
            int field1 = 30;
            @Max(20)
            int field2 = 30;
            @Max(20)
            int field3 = 30;
            @Max(20)
            int field4 = 30;
            @Max(20)
            int field5 = 30;
            @Max(20)
            int field6 = 30;
            @Max(20)
            int field7 = 30;
        }

        class ValidationTask implements Callable<ValidationResult> {

            @Override
            public ValidationResult call() throws Exception {
                return validationRunner.validate(new TestObject());
            }
        }

        ValidationResult expected = new ValidationResult(new HashMap<String, List<ValidationError>>() {{
            put("field1", Arrays.asList(new ValidationError("value is greater than max")));
            put("field2", Arrays.asList(new ValidationError("value is greater than max")));
            put("field3", Arrays.asList(new ValidationError("value is greater than max")));
            put("field4", Arrays.asList(new ValidationError("value is greater than max")));
            put("field5", Arrays.asList(new ValidationError("value is greater than max")));
            put("field6", Arrays.asList(new ValidationError("value is greater than max")));
            put("field7", Arrays.asList(new ValidationError("value is greater than max")));
        }});

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<ValidationResult>> results = new ArrayList<>();

        for(int i=0; i<10000; i++) {
            results.add(executor.submit(new ValidationTask()));
        }

        try {
            for (Future<ValidationResult> future : results)
                assertEquals(expected, future.get());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        executor.shutdown();
    }
}

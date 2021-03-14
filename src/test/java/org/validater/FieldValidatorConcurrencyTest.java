
package org.validater;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Max;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldValidatorConcurrencyTest {

    static ValidationRunner validationRunner;

    /**
     * Tests caching mechanism if the same type of objects are validated concurrently
     * and threads try to retrieve the objects from cache before the first thread finished
     * caching it.
     */

    @Test
    public void testConcurrentCacheRetrieval() {

        validationRunner = new ValidationRunner() {

            int threads = 0;

            @Override
            public synchronized ValidationResult validate(Object obj) {
                ValidationResult res = super.validate(obj);
                this.notify();
                return res;
            }

            @Override
            synchronized void validateField(Object obj, Field field, FieldValidator<Object, Annotation> validator, Annotation annotation,
                                            ValidationResult result, List<FieldValidation> cached) {

                super.validateField(obj, field, validator, annotation, result, cached);

                if(threads == 0) {
                    threads++;
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        class TestObject {

            @Max(40)
            int field1 = 56;
            @Max(60)
            int field2 = 92;
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
        }});

        ExecutorService executor = Executors.newCachedThreadPool();
        List<Future<ValidationResult>> results = new ArrayList<>();

        results.add(executor.submit(new ValidationTask()));

        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        for(int i=0; i<4; i++)
            results.add(executor.submit(new ValidationTask()));

        try {
            for (Future<ValidationResult> future : results) {
                assertEquals(expected, future.get());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        executor.shutdown();
    }

    /**
     * Tests when multiple threads try to run cached validation from the same cache
     */

    @Disabled
    @Test
    public void testConcurrentCacheValidation() {

        validationRunner = new ValidationRunner();

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

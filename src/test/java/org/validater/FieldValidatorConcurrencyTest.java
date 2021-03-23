
package org.validater;

import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    public static void init() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        validationRunner = factory.getValidationRunner();
    }

    @Test
    public void testConcurrentCacheAccess() {

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

        class TestObject2 extends TestObject {}
        class TestObject3 extends TestObject {}
        class TestObject4 extends TestObject {}
        class TestObject5 extends TestObject {}

        class ValidationTask implements Callable<ValidationResult> {

            private TestObject obj;

            ValidationTask(TestObject obj) {
                this.obj = obj;
            }

            @Override
            public ValidationResult call() throws Exception {
                return validationRunner.validate(obj);
            }
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        Random rand = new Random();
        TestObject[] testObjects =
                {new TestObject(), new TestObject2(), new TestObject3(), new TestObject4(), new TestObject5()};

        List<Future<ValidationResult>> results = new ArrayList<>();

        for(int i=0; i<100000; i++) {
            int ind = rand.nextInt(5);
            results.add(executor.submit(new ValidationTask(testObjects[ind])));

            if(i % 10 == 1) {
                ValidationCache cache = validationRunner.getCache();
                cache.clear();
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

        try {
            for (Future<ValidationResult> future : results)
                assertEquals(expected, future.get());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        executor.shutdown();
    }
}

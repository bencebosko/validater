package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Max;
import org.validater.annotations.Min;
import org.validater.annotations.Required;
import static java.lang.management.ManagementFactory.getThreadMXBean;

@Disabled
public class PerformanceTests {

    private static ValidationRunner validationRunner;
    private static ValidationRunner validationRunnerNoCache;

    @BeforeAll
    public static void init() {
        validationRunner = new ValidationRunner(new ValidationCacheImpl());

        validationRunnerNoCache = new ValidationRunner(new ValidationCacheImpl() {
            @Override
            public boolean isCached(Class<?> type) {
                return false;
            }
        });
    }

    @Test
    public void testCachePerformance() {
        Thread t = new Thread(new ValidationTask(validationRunner, new TestObject(), "CacheValidation"));
        t.start();
        try {
            t.join();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testNoCachePerformance() {
        Thread t = new Thread(new ValidationTask(validationRunnerNoCache, new TestObject(), "NoCacheValidation"));
        t.start();
        try {
            t.join();
        } catch (Exception ex) {

        }
    }

    private static class TestObject {

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

    private static class ValidationTask implements Runnable {

        ValidationRunner validationRunner;
        TestObject obj;
        String label;
        long threadCpuTime;

        ValidationTask(ValidationRunner runner, TestObject obj, String label) {
            this.validationRunner = runner;
            this.obj = obj;
            this.label = label;
        }

        @Override
        public void run() {
            for(int i=0; i<100000; i++) {
                validationRunner.validate(obj);
            }
            threadCpuTime = getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());

            System.out.println(label+ " ran in: " + (threadCpuTime / 1000000L) + " ms");

            validationRunner.getCache().clear();
        }
    }
}

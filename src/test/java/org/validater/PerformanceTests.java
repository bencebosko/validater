package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Max;
import org.validater.annotations.Min;
import org.validater.annotations.Required;

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

        ValidationCache cache = validationRunner.getCache();

        for(int i=0; i<100000; i++) {
            validationRunner.validate(new TestObject());
        }

        cache.clear();
    }

    @Test
    public void testNoCachePerformance() {

        for(int i=0; i<100000; i++) {
            validationRunnerNoCache.validate(new TestObject());
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
}

package org.validater;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.validater.annotations.Min;
import org.validater.dto.TestDto1;
import org.validater.dto.TestDto2;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationCacheTest {

    private static ValidationRunner validationRunner;
    private static ValidationCacheMock cache;

    @BeforeAll
    public static void init() {
        validationRunner = new ValidationRunner(new ValidationCacheMock());
        cache = (ValidationCacheMock)validationRunner.getCache();
    }

    @Test
    public void testCacheAccess() {

        class TestObject {
            @Min(10)
            int field = 20;
        }

        assertFalse(cache.isCached(TestObject.class));

        assertEquals(0, cache.getCacheGets());
        assertEquals(0, cache.getCacheAdds());

        validationRunner.validate(new TestObject());

        assertTrue(cache.isCached(TestObject.class));

        assertEquals(0, cache.getCacheGets());
        assertEquals(1, cache.getCacheAdds());

        validationRunner.validate(new TestObject());

        assertEquals(1, cache.getCacheGets());
        assertEquals(1, cache.getCacheAdds());
    }

    @Test
    public void testEagerCache() {
        ValidationRunnerFactory factory = new ValidationRunnerFactory();
        factory.setPackagesToScan("org.validater.dto");

        ValidationRunner validationRunner = factory.getValidationRunner();
        ValidationCache cache = validationRunner.getCache();

        assertTrue(cache.isCached(TestDto1.class));
        assertTrue(cache.isCached(TestDto2.class));
    }
}

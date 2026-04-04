package hse.java.lectures.lesson7.limiter;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@Tag("limiter")
class RateLimiterTest {

    @Test
    void exceedLimitTest() {
        RateLimiter limiter = new RateLimiter(ChronoUnit.SECONDS, 100);
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.check());
        }
        assertFalse(limiter.check());
    }

    @Test
    void afterPeriodWeCanCallCheck() throws InterruptedException {
        RateLimiter limiter = new RateLimiter(ChronoUnit.SECONDS, 100);
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.check());
        }

        Thread.sleep(500);

        assertFalse(limiter.check());

        Thread.sleep(500);
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.check(), Integer.toString(i));
        }
    }

}
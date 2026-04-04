package hse.java.lectures.lesson7.limiter;

import javax.imageio.plugins.tiff.TIFFImageReadParam;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Скользящий рейтлимитер: не больше заданного числа успешных {@link #check()} за последнюю секунду или минуту.
 */
public class RateLimiter {

    /**
     * @param unit        длина окна — только {@link ChronoUnit#SECONDS} или {@link ChronoUnit#MINUTES}
     * (скользящее окно 1 секунда или 1 минута)
     * @param maxRequests максимум успешных {@link #check()} за окно (должно быть > 0)
     */
    private ConcurrentLinkedQueue<Instant> events = new ConcurrentLinkedQueue<>();
    private ChronoUnit windowSizeTime;
    private final int maxRequests;

    public RateLimiter(ChronoUnit unit, int maxRequests) throws IllegalArgumentException {
        this.maxRequests = maxRequests;
        this.windowSizeTime = unit;
    }

    /**
     * Регистрирует попытку и возвращает, разрешена ли она в пределах лимита.
     */
    public boolean check() {
        synchronized (this) {
            var now = Instant.now();
            if (events.size() < maxRequests) {
                events.add(Instant.now());
                return true;
            }

            if (Duration.between(events.peek(), now).compareTo(windowSizeTime.getDuration()) < 0) {
                return false;
            } else {
                events.poll();
                events.add(now);
                return true;
            }
        }
    }

}

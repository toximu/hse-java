package hse.java.lectures.lesson7.dau;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.*;

public class DauServiceImpl implements DauService {
    private ConcurrentMap<Integer, Long> yesterdayStat = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, Set<Integer>> nowStat = new ConcurrentHashMap<>();

    private ScheduledExecutorService s = Executors.newScheduledThreadPool(1);

    DauServiceImpl() {

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime nextRun = now.withHour(0).withMinute(0).withSecond(0).withNano(0);

        long initialDelay = Duration.between(now, nextRun).getSeconds();
        long period = TimeUnit.DAYS.toSeconds(1);

        s.scheduleAtFixedRate(this::update,
                initialDelay, period, TimeUnit.SECONDS
        );
    }

    void update() {
        yesterdayStat.clear();
        for (var entry : nowStat.entrySet()) {
            yesterdayStat.put(entry.getKey(), (long) entry.getValue().size());
        }
        nowStat.clear();
    }


    @Override
    public void postEvent(Event event) {
        nowStat.computeIfAbsent(event.authorId(), k -> ConcurrentHashMap.newKeySet()).add(event.userId());
    }

    @Override
    public Map<Integer, Long> getDauStatistics(List<Integer> authorIds) {
        HashMap<Integer, Long> result = new HashMap<>();
        for (var id : authorIds) {
            var stat = yesterdayStat.get(id);
            result.put(id, stat == null ? 0 : stat);
        }
        return result;
    }

    @Override
    public Long getAuthorDauStatistics(int authorId) {
        var res = yesterdayStat.get(authorId);
        return (res == null ? 0 : res);
    }

    void goToNextDay() {
        update();
    }
}

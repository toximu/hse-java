package hse.java.lectures.lesson7.dau;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DauService {

    void postEvent(Event event);

    Map<Integer, Long> getDauStatistics(List<Integer> authorIds);

    Long getAuthorDauStatistics(int authorId);

}

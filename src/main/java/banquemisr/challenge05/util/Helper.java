package banquemisr.challenge05.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Helper {
    final static public DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static long zonedDateTimeDifference(LocalDateTime d1, LocalDateTime d2, ChronoUnit unit) {
        return unit.between(d1, d2);
    }

}

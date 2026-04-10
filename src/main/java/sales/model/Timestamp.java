package sales.model;

import java.time.LocalDateTime;

/**
 * @author Mason Hart
 */
public class Timestamp {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public Timestamp(){
        LocalDateTime date = LocalDateTime.now();
        this.year = (short) date.getYear();
        this.month = date.getMonth().getValue();
        this.day = date.getDayOfMonth();
        this.hour = date.getHour();
        this.minute = date.getMinute();
        this.second = date.getSecond();
    }

    public String toString(){
        return String.format("%d-%d-%dT%d:%d:%d",
                year,month,day,hour,minute,second);
    }
}

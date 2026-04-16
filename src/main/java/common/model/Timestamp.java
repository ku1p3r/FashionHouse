package common.model;

import java.time.LocalDateTime;

/**
 * @author Mason Hart
 */
public class Timestamp {

    public enum Weekday { MON, TUE, WED, THU, FRI, SAT, SUN }

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
                                            /* J   F   Mar A   May Jun Jul Aug Sep O   N  D */
    private final static int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final static Weekday[] monthStartDay = {
            Weekday.THU, Weekday.SUN, Weekday.SUN, Weekday.WED, Weekday.FRI, Weekday.MON, Weekday.WED, Weekday.SAT,
            Weekday.TUE, Weekday.THU, Weekday.SUN, Weekday.TUE};
    public final static int CURR_YEAR = 2026;

    public Timestamp(){
        LocalDateTime date = LocalDateTime.now();
        this.year = (short) date.getYear();
        this.month = date.getMonth().getValue();
        this.day = date.getDayOfMonth();
        this.hour = date.getHour();
        this.minute = date.getMinute();
        this.second = date.getSecond();
    }

    public Timestamp(int y, int m, int d, int h, int min){
        if (y != CURR_YEAR) {
            throw new IllegalArgumentException("Invalid Year");
        } else if (m < 1 || m > 12){
            throw new IllegalArgumentException("Invalid Month");
        } else if (d < 1 || d > daysPerMonth[m-1]){
            throw new IllegalArgumentException("Invalid Day");
        } else if (h < 0 || h > 23){
            throw new IllegalArgumentException("Invalid Hour");
        } else if (min < 0 || min > 59){
            throw new IllegalArgumentException("Invalid Minute");
        }
        this.year = y;
        this.month = m;
        this.day = d;
        this.hour = h;
        this.minute = min;
        this.second = 0;
    }

    public Timestamp(String s){
        String[] spl = s.split("T");
        if(spl.length != 2) throw new IllegalArgumentException("Invalid format");
        int y, m, d, h, min, sec;
        String[] date = spl[0].split("-");
        y = Integer.parseInt(date[0]);
        m = Integer.parseInt(date[1]);
        d = Integer.parseInt(date[2]);
        String[] time = spl[1].split(":");
        h = Integer.parseInt(time[0]);
        min = Integer.parseInt(time[1]);
        sec = Integer.parseInt(time[2]);
        this(y,m,d,h,min);
        this.second = sec;
    }

    public int getYear() { return this.year; }
    public int getMonth() { return this.month; }
    public int getDay() { return this.day; }
    public int getHour() { return this.hour; }
    public int getMinute() { return this.minute; }
    public int getSecond() { return this.second; }

    public String toString(){
        return String.format("%04d-%02d-%02dT%02d:%02d:%02d",
                year,month,day,hour,minute,second);
    }

    public int compareTo(Timestamp other){
        if(this.year != other.getYear()) return this.year - other.getYear();
        if(this.month != other.getMonth()) return this.month - other.getMonth();
        if(this.day != other.getDay()) return this.day - other.getDay();
        if(this.hour != other.getHour()) return this.hour - other.getHour();
        if(this.minute != other.getMinute()) return this.minute - other.getMinute();
        return this.second - other.getSecond();
    }

    public boolean isBefore(Timestamp other){
        return this.compareTo(other) < 0;
    }

    public boolean isAfter(Timestamp other){
        return this.compareTo(other) > 0;
    }

    public boolean isWithin(Timestamp l, Timestamp r){
        if(l.isAfter(r)) throw new IllegalArgumentException("Left timestamp is after right");
        return this.isAfter(l) && this.isBefore(r);
    }

    public Timestamp roundToNextMinute(){
        return this.second < 30
                ? new Timestamp(year,month,day,hour,minute)
                : new Timestamp(year,month,day,hour,minute + 1);
    }

    public Timestamp roundToNextHour() {
        return this.minute < 30
                ? new Timestamp(year,month,day,hour,0)
                : new Timestamp(year,month,day,hour + 1, 0);
    }

    public Weekday getWeekday(){
        return null; // TODO
    }

    public Weekday next(Weekday query){
        return null; // TODO
    }
}

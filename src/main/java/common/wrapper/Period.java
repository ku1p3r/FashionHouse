package common.wrapper;

import java.util.Objects;

public class Period {
    private int month;
    private int year;

    public Period(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth(){
        return month;
    }

    public int getYear(){
        return year;
    }

    public String getFileString(){
        return month + "-" + year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return month == period.month && year == period.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month, year);
    }
}

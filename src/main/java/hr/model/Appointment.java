package hr.model;

import common.model.Timestamp;

public class Appointment {

    private Timestamp start;
    private int minutesDuration;

    public Appointment(Timestamp start, int dur){
        this.start = start;
        this.minutesDuration = dur;
    }
}

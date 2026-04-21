package hr.model;

import common.model.Timestamp;

public class Appointment {

    private Timestamp start;
    private Timestamp end;

    public Appointment(Timestamp start, Timestamp end){
        this.start = start;
        this.end = end;
    }
}

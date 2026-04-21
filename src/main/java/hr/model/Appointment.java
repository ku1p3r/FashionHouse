package hr.model;

import common.model.Timestamp;

/**
 * This was unused for iteration 2, it will be resumed in iteration 3
 *
 * @author Mason Hart
 */
public class Appointment {

    private Timestamp start;
    private Timestamp end;

    public Appointment(Timestamp start, Timestamp end){
        this.start = start;
        this.end = end;
    }
}

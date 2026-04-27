package hr.model;

import common.model.Timestamp;

import java.util.Random;

/**
 * @author Mason Hart
 */
public class Application {

    private long id;
    private String title;
    private String description;
    private String dept;
    private Timestamp closeDate;
    private int numPositions;

    public Application(long id, String title, String desc, String dept, Timestamp closeDate, int num){
        this.id = id;
        this.title = title;
        this.description = desc;
        this.dept = dept;
        this.closeDate = closeDate;
        this.numPositions = num;
    }

    public Application(){
        this.id = new Random().nextLong(1,10000);
        this.title = "Empty";
        this.description = "Empty";
        this.dept = "OTHER";
        this.closeDate = new Timestamp();
        this.numPositions = 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public void setNumPositions(int numPositions) {
        this.numPositions = numPositions;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDept() {
        return dept;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public int getNumPositions() {
        return numPositions;
    }

    public int decrementPosition() {
        numPositions--;
        return numPositions;
    }

    public boolean isClosed(){
        Timestamp now = new Timestamp();
        return this.closeDate.isBefore(now);
    }

    public String toString(){
        return String.format("%d | \"%s\" | \"%.10s...\" | Dept: %s | Closes: %s | Openings: %d",
                this.id, this.title, this.description, this.dept, this.closeDate, this.numPositions);
    }

}

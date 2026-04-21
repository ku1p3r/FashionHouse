package hr.model;

/**
 * @author Mason Hart
 */
public class Candidate implements Comparable<Candidate>{

    private long id;
    private String name;
    private int experience;
    private String bio;

    public Candidate(long id, String name, int exp, String bio){
        this.id = id;
        this.name = name;
        this.experience = exp;
        this.bio = bio;
    }

    public long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getExperience(){
        return this.experience;
    }

    public String getBio() { return this.bio; }

    public String toString(){
        return String.format("%d | %s | %d yrs | %b",
                id, name, experience, getBio());
    }

    @Override
    public int compareTo(Candidate o) {
        return this.experience - o.getExperience();
    }
}

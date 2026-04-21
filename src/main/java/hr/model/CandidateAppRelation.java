package hr.model;

/**
 * Establishes the many-to-many relationship between candidates and applications
 *
 * @author Mason Hart
 */
public class CandidateAppRelation {

    private long candId;
    private long appId;
    private boolean rejected;

    public CandidateAppRelation(long candidateID, long appID, boolean rejected){
        this.candId = candidateID;
        this.appId = appID;
        this.rejected = rejected;
    }

    public long getCandId(){
        return this.candId;
    }

    public long getAppId(){
        return this.appId;
    }

    public boolean isRejected(){
        return this.rejected;
    }
}

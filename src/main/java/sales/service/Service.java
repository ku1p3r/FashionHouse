package sales.service;

/**
 * @author Mason Hart
 */
public interface Service {

    public void saveToDB() throws Exception;

    public Object getFromDB(int id);

}

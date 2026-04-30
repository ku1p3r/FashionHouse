package security.service;

import security.Campus;

import java.util.List;

/**
 * @author Mason Hart
 */
public interface CampusRepository {

    List<Campus> loadCampuses();

    void saveCampuses(List<Campus> campuses);
}

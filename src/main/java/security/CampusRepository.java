package security;

import java.util.List;

/**
 * Persistence port for campuses.
 */
public interface CampusRepository {

    List<Campus> loadCampuses();

    void saveCampuses(List<Campus> campuses);
}

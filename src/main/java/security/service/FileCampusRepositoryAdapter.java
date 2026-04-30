package security.service;

import security.Campus;
import security.CampusFileManager;

import java.util.List;

/**
 * Adapter that preserves existing campus file manager behavior.
 *
 * @author Mason Hart
 */
public class FileCampusRepositoryAdapter implements CampusRepository {

    @Override
    public List<Campus> loadCampuses() {
        return CampusFileManager.loadCampuses();
    }

    @Override
    public void saveCampuses(List<Campus> campuses) {
        CampusFileManager.saveCampuses(campuses);
    }
}

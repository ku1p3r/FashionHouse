package security;

import java.util.List;

/**
 * Adapter that preserves existing campus file manager behavior.
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

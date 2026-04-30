package security.service;

import security.GuardFileManager;
import security.SecurityGuard;

import java.util.List;

/**
 * Adapter that preserves existing file manager behavior.
 *
 * @author Mason Hart
 */
public class FileGuardRepositoryAdapter implements GuardRepository {

    @Override
    public List<SecurityGuard> loadGuards() {
        return GuardFileManager.loadGuards();
    }

    @Override
    public void saveGuards(List<SecurityGuard> guards) {
        GuardFileManager.saveGuards(guards);
    }
}

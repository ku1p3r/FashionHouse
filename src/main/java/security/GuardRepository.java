package security;

import java.util.List;

/**
 * Persistence port for security guards.
 */
public interface GuardRepository {

    List<SecurityGuard> loadGuards();

    void saveGuards(List<SecurityGuard> guards);
}

package security.service;

import security.SecurityGuard;

import java.util.List;

/**
 * @author Mason Hart
 */
public interface GuardRepository {

    List<SecurityGuard> loadGuards();

    void saveGuards(List<SecurityGuard> guards);
}

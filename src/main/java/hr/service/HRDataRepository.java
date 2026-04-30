package hr.service;

import common.model.Timestamp;
import hr.model.Application;
import hr.model.Candidate;
import hr.model.CandidateAppRelation;
import hr.model.Employee;
import java.util.List;

/**
 * Persistence port for HR data.
 */
public interface HRDataRepository {

    List<Application> loadApplications();

    List<Employee> loadEmployees();

    List<Candidate> loadCandidates();

    List<CandidateAppRelation> loadCandidateRelations();

    void saveApplication(Application application);

    void markCandidateRejected(long candidateId, long applicationId);

    void saveAcceptedEmployee(long id,
                              String name,
                              String title,
                              String dept,
                              Timestamp startDate,
                              int salary,
                              boolean active,
                              String pin);

    void updateApplicationOpenings(int applicationIndex, int openings);
}

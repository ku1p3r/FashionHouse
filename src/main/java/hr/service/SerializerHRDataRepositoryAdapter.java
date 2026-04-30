package hr.service;

import common.model.Timestamp;
import common.util.Serializer;
import hr.model.Application;
import hr.model.Candidate;
import hr.model.CandidateAppRelation;
import hr.model.Employee;
import java.util.ArrayList;
import java.util.List;

/**
 * Serializer-backed adapter for HR persistence.
 */
public class SerializerHRDataRepositoryAdapter implements HRDataRepository {

    private static final String APPLICATION_CSV = "res/hr/applications.csv";
    private static final String CANDIDATE_CSV = "res/hr/candidates.csv";
    private static final String EMPLOYEE_CSV = "res/hr/employees.csv";
    private static final String CANDIDATE_KEYS_CSV = "res/hr/cand_per_app.csv";

    private final Serializer appSerializer;
    private final Serializer candSerializer;
    private final Serializer empSerializer;
    private final Serializer keySerializer;

    public SerializerHRDataRepositoryAdapter() {
        this.appSerializer = new Serializer(APPLICATION_CSV);
        this.candSerializer = new Serializer(CANDIDATE_CSV);
        this.empSerializer = new Serializer(EMPLOYEE_CSV);
        this.keySerializer = new Serializer(CANDIDATE_KEYS_CSV);
    }

    @Override
    public List<Application> loadApplications() {
        List<Application> applications = new ArrayList<>();
        ArrayList<String> ids = appSerializer.get("id", String.class);
        for (int i = 0; i < ids.size(); i++) {
            Application app = new Application(
                appSerializer.get("id", i, Long.class),
                appSerializer.get("title", i, String.class),
                appSerializer.get("desc", i, String.class),
                appSerializer.get("dept", i, String.class),
                new Timestamp(appSerializer.get("close_date", i, String.class)),
                appSerializer.get("openings", i, Integer.class)
            );
            applications.add(app);
        }
        return applications;
    }

    @Override
    public List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();
        ArrayList<String> ids = empSerializer.get("id", String.class);
        for (int i = 0; i < ids.size(); i++) {
            Employee employee = new Employee(
                empSerializer.get("id", i, Long.class),
                empSerializer.get("name", i, String.class),
                empSerializer.get("position", i, String.class),
                empSerializer.get("dept", i, String.class),
                empSerializer.get("salary", i, Integer.class),
                new Timestamp(empSerializer.get("start", i, String.class)),
                empSerializer.get("active", i, Integer.class) > 0,
                empSerializer.get("pin", i, String.class)
            );
            employees.add(employee);
        }
        return employees;
    }

    @Override
    public List<Candidate> loadCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        ArrayList<String> ids = candSerializer.get("id", String.class);
        for (int i = 0; i < ids.size(); i++) {
            Candidate candidate = new Candidate(
                candSerializer.get("id", i, Long.class),
                candSerializer.get("name", i, String.class),
                candSerializer.get("experience", i, Integer.class),
                candSerializer.get("bio", i, String.class)
            );
            candidates.add(candidate);
        }
        return candidates;
    }

    @Override
    public List<CandidateAppRelation> loadCandidateRelations() {
        List<CandidateAppRelation> keys = new ArrayList<>();
        List<String> ids = keySerializer.get("cand_id", String.class);
        for (int i = 0; i < ids.size(); i++) {
            CandidateAppRelation relation = new CandidateAppRelation(
                keySerializer.get("cand_id", i, Long.class),
                keySerializer.get("app_id", i, Long.class),
                keySerializer.get("rejected", i, Integer.class) <= 0
            );
            keys.add(relation);
        }
        return keys;
    }

    @Override
    public void saveApplication(Application application) {
        appSerializer.push(
            application.getId(),
            application.getTitle(),
            application.getDescription(),
            application.getDept(),
            application.getCloseDate(),
            application.getNumPositions()
        );
        appSerializer.save();
    }

    @Override
    public void markCandidateRejected(long candidateId, long applicationId) {
        List<CandidateAppRelation> keys = loadCandidateRelations();
        for (int i = 0; i < keys.size(); i++) {
            CandidateAppRelation key = keys.get(i);
            if (candidateId == key.getCandId() && applicationId == key.getAppId()) {
                keySerializer.set("rejected", i, 1);
                keySerializer.save();
            }
        }
    }

    @Override
    public void saveAcceptedEmployee(long id,
                                     String name,
                                     String title,
                                     String dept,
                                     Timestamp startDate,
                                     int salary,
                                     boolean active,
                                     String pin) {
        empSerializer.push(
            id,
            name,
            title,
            dept,
            startDate,
            salary,
            active ? 1 : 0,
            pin
        );
        empSerializer.save();
    }

    @Override
    public void updateApplicationOpenings(int applicationIndex, int openings) {
        appSerializer.set("openings", applicationIndex, openings);
        appSerializer.save();
    }
}

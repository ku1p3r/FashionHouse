package hr.service;

import hr.model.Application;
import hr.model.Candidate;
import hr.model.CandidateAppRelation;
import hr.model.Employee;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Mason Hart
 */
public class HRService {
    private final HRDataRepository repository;

    private List<Application> applicationList;
    private List<Employee> employeeList;
    private List<Candidate> candidateList;

    private int selectedEmp;
    private int selectedApp;
    private Candidate selectedCandidate;

    public HRService() {
        this(new SerializerHRDataRepositoryAdapter());
    }

    public HRService(HRDataRepository repository){
        this.repository = repository;
        loadApplications();
        loadEmployees();
        loadCandidates();

        selectedEmp = 0;
        selectedApp = 0;
        selectedCandidate = candidateList.get(0);
    }

    private void loadApplications(){
        applicationList = new ArrayList<>(repository.loadApplications());
    }

    private void loadEmployees(){
        employeeList = new ArrayList<>(repository.loadEmployees());
    }

    private void loadCandidates(){
        candidateList = new ArrayList<>(repository.loadCandidates());
    }

    public List<CandidateAppRelation> loadKeys(){
        return repository.loadCandidateRelations();
    }

    public void createApplication(Application app) {
        try {
            repository.saveApplication(app);

            // Important: add the application to the loaded list of applications
            applicationList.add(app);
        } catch(Exception e){
            System.out.println("Fatal Error: Exception while saving sale");
            System.err.println(e);
        }
    }

    public List<Application> getApplications() {
        return this.applicationList;
    }

    public List<Employee> getEmployees() {
        return this.employeeList;
    }

    public void selectEmployee(int index) {
        selectedEmp = index-1;
    }

    public Employee getSelectedEmployee() {
        return employeeList.get(selectedEmp);
    }

    public void hireEmployee(Candidate c){
        // TODO - perhaps iteration 3
    }

    public void fireEmployee(long employeeId){
        // TODO - perhaps iteration 3
    }

    public List<Candidate> getCandidates(){
        List<Candidate> candidates = new ArrayList<>();
        List<CandidateAppRelation> keys = loadKeys();

        // see if keys match to add candidates
        Application app = applicationList.get(selectedApp);
        for (Candidate candidate : candidateList) {
            for (CandidateAppRelation key : keys) {
                if (candidate.getId() == key.getCandId() && app.getId() == key.getAppId() && key.isRejected()) {
                    System.out.println("adding candidate");
                    candidates.add(candidate);
                }
            }
        }

        candidates.sort(Collections.reverseOrder());

        return candidates;
    }

    public Candidate getSelectedCandidate() {
        return selectedCandidate;
    }

    public void rejectCandidate() {
        try {
            List<CandidateAppRelation> keys = loadKeys();
            Application curr = applicationList.get(selectedApp);

            for (int i = 0; i < keys.size(); i++) {
                CandidateAppRelation key = keys.get(i);
                if (selectedCandidate.getId() == key.getCandId() && curr.getId() == key.getAppId()) {
                    repository.markCandidateRejected(selectedCandidate.getId(), curr.getId());
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        loadCandidates();
    }

    public void acceptCandidate() {
        Random r = new Random();
        Application app = applicationList.get(selectedApp);
        String pin = String.format("%04d", r.nextInt(0, 10000));
        Employee e = new Employee(
                employeeList.size() + 1,
                selectedCandidate.getName(),
                app.getTitle(),
                app.getDept(),
                70,
                app.getCloseDate(),
                true,
                pin
        );
        try{
            repository.saveAcceptedEmployee(
                employeeList.size() + 1,
                selectedCandidate.getName(),
                app.getTitle(),
                app.getDept(),
                app.getCloseDate(),
                70,
                true,
                pin
            );
            employeeList.add(e);
            app.decrementPosition();

            // remove the candidate from the list
            rejectCandidate();

            // save to file
            repository.updateApplicationOpenings(selectedApp, app.getNumPositions());
        } catch(Exception err){
            System.out.println("An error occured when adding new employee to database");
            System.err.println(err);
        }

    }
    
    public void selectApplication(int choice) {
        this.selectedApp = choice - 1;
    }
    
    public Application getSelectedApplication() {
        return applicationList.get(selectedApp);
    }

    public void selectCandidate(Candidate c) {
        this.selectedCandidate = c;
    }
}

package hr.service;

import common.model.Timestamp;
import common.util.Serializer;
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

    private static final String applicationCSV = "res/hr/applications.csv";
    private static final String candidateCSV = "res/hr/candidates.csv";
    private static final String employeeCSV = "res/hr/employees.csv";
    private static final String candidateKeysCSV = "res/hr/cand_per_app.csv";

    private Serializer appSerializer = new Serializer(applicationCSV);
    private Serializer candSerializer = new Serializer(candidateCSV);
    private Serializer empSerializer = new Serializer(employeeCSV);
    private Serializer keySerializer = new Serializer(candidateKeysCSV);

    private List<Application> applicationList;
    private List<Employee> employeeList;
    private List<Candidate> candidateList;

    private int selectedEmp;
    private int selectedApp;
    private Candidate selectedCandidate;

    public HRService(){
        applicationList = new ArrayList<>();
        employeeList = new ArrayList<>();
        candidateList = new ArrayList<>();

        selectedEmp = 0;
        selectedApp = 0;

        // load applications
        ArrayList<String> ids = appSerializer.get("id", String.class);
        int rows = ids.size();
        for(int i = 0; i < rows; i++){
            Application a = new Application(
                    appSerializer.get("id", i, Long.class),
                    appSerializer.get("title", i, String.class),
                    appSerializer.get("desc", i, String.class),
                    appSerializer.get("dept", i, String.class),
                    new Timestamp(appSerializer.get("close_date", i, String.class)),
                    appSerializer.get("openings", i, Integer.class)
            );
//            System.out.println(a);
            if(!a.isClosed()){
                applicationList.add(a);
            }
        }

        // load employees
        ids = empSerializer.get("id", String.class);
        rows = ids.size();
        for(int i = 0; i < rows; i++){
            Employee e = new Employee(
                    empSerializer.get("id", i, Long.class),
                    empSerializer.get("name", i, String.class),
                    empSerializer.get("position", i, String.class),
                    empSerializer.get("dept", i, String.class),
                    empSerializer.get("salary", i, Integer.class),
                    new Timestamp(empSerializer.get("start", i, String.class)),
                    empSerializer.get("active", i, Integer.class) > 0,
                    empSerializer.get("pin", i, String.class)
            );
            employeeList.add(e);
        }

        // load all candidates
        ids = candSerializer.get("id", String.class);
        rows = ids.size();
        for(int i = 0; i < rows; i++){
            Candidate c = new Candidate(
                    candSerializer.get("id", i, Long.class),
                    candSerializer.get("name", i, String.class),
                    candSerializer.get("experience", i, Integer.class),
                    candSerializer.get("bio", i, String.class)
            );
//            System.out.println(c);
            candidateList.add(c);
        }
    }

    public void createApplication(Application app) {
        try {
            appSerializer.push(
                    app.getId(),
                    app.getTitle(),
                    app.getDescription(),
                    app.getDept(),
                    app.getCloseDate(),
                    app.getNumPositions()
            );
            appSerializer.save();

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
        List<CandidateAppRelation> keys = new ArrayList<>();

        // load keys
        List<String> ids = keySerializer.get("cand_id", String.class);
        int rows = ids.size();
        for(int i = 0; i < rows; i++){
            CandidateAppRelation rel = new CandidateAppRelation(
                    keySerializer.get("cand_id", i, Long.class),
                    keySerializer.get("app_id", i, Long.class),
                    keySerializer.get("rejected", i, Integer.class) < 0
            );
            keys.add(rel);
        }

        // see if keys match to add candidates
        Application curr = applicationList.get(selectedApp);
        for (Candidate candidate : candidateList) {
            for (CandidateAppRelation key : keys) {
                if (candidate.getId() == key.getCandId() && curr.getId() == key.getAppId() && !key.isRejected()) {
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
        keySerializer.set("rejected", (int)selectedCandidate.getId()-1, 0);
    }

    public void acceptCandidate() {
        Random r = new Random();
        Application app = applicationList.get(selectedApp);
        try{
            empSerializer.push(
                    employeeList.size() + 1,
                    selectedCandidate.getName(),
                    app.getTitle(),
                    app.getDept(),
                    app.getCloseDate(),
                    70,
                    1,
                    String.format("%04d", r.nextInt(0, 10000))
            );
        } catch(Exception e){
            System.out.println("An error occured when adding new employee to database");
            System.err.println(e);
        }

    }
    
    public void selectApplication(int choice) {
        this.selectedApp = choice-1;
    }
    
    public Application getSelectedApplication() {
        return applicationList.get(selectedApp);
    }

    public void selectCandidate(Candidate c) {
        this.selectedCandidate = c;
    }
}

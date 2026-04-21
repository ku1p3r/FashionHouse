package hr.ui;

import hr.ScreenInput;
import hr.model.Candidate;
import hr.service.HRService;

import java.util.List;
import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class ViewCandidatesScreen implements Screen {

    private HRService service;

    public ViewCandidatesScreen(HRService service){
        this.service = service;
    }

    private List<Candidate> candidates;

    @Override
    public void show() {
        candidates = service.getCandidates();
        System.out.printf("Candidate List (%d)\n\n", candidates.size());
        for(int i = 0; i < candidates.size(); i++){
            System.out.printf("%d: %s\n", i+1, candidates.get(i));
        }
        System.out.printf("\n0:back to app\n[1-%d]:view candidate\n---> ", candidates.size());
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 0){
            return ScreenInput.TO_EDIT_APPLICATION;
        } else if(choice >= 1){
            service.selectCandidate(candidates.get(choice-1));
            return ScreenInput.TO_CANDIDATE;
        } else {
            return ScreenInput.NONE;
        }
    }
}

package hr.ui;

import hr.ScreenInput;
import hr.model.Candidate;
import hr.service.HRService;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Mason Hart
 */
public class CandidateScreen implements Screen {

    private HRService service;

    private static final Map<Integer, ScreenInput> ACTION_MAP = Map.of(
            0, ScreenInput.TO_VIEW_CANDIDATES,
            1, ScreenInput.TO_SEND_OFFER,
            2, ScreenInput.TO_SEND_REJECT
    );

    public CandidateScreen(HRService service){
        this.service = service;
    }

    @Override
    public void show() {
        Candidate c = service.getSelectedCandidate();
        System.out.printf("Candidate #%d\n\n", c.getId());
        System.out.printf("Name: %s\nExp : %s\nBio: %s\n\n", c.getName(), c.getExperience(), c.getBio());
        System.out.print("0: back to all candidates\n1: offer this candidate the position\n2: reject this candidate\n---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();
        return ACTION_MAP.getOrDefault(choice, ScreenInput.NONE);
    }
}

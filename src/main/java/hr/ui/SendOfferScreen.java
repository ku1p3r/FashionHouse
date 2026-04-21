package hr.ui;

import hr.ScreenInput;
import hr.model.Application;
import hr.model.Candidate;
import hr.service.HRService;

import java.util.Scanner;

public class SendOfferScreen implements Screen {

    private HRService service;

    public SendOfferScreen(HRService service){
        this.service = service;
    }


    @Override
    public void show() {
        Candidate candidate = service.getSelectedCandidate();
        Application app = service.getSelectedApplication();

        System.out.printf("Send %s the offer \n\n", candidate.getName());
        System.out.printf("""
                Dear %s,
                We are thrilled to offer you the position of %s at Fashion House. We were very impressed with your skills and experience and believe you will be a fantastic addition to our team.
                Here are the key details of our offer:
                
                    Position: %s
                    Department: %s
                    Start Date: %s
                    Employment Type: Full-time
                    Location: [redacted]
                
                Attached you will find a formal job offer letter with comprehensive details regarding benefits, including [list 2-3 key benefits, e.g., health insurance, PTO, 401(k)].
                Please review the attached document, sign it, and return it to me via email by %s.
                We look forward to welcoming you to the team. Please feel free to reach out with any questions.
                Best regards,
                Fashion House Team
                
        """, candidate.getName(), app.getTitle(), app.getTitle(), app.getDept(), app.getCloseDate());
        System.out.print("Send Offer\n\n0:back to candidate\n1:send offer\n\n---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 0){
            return ScreenInput.TO_CANDIDATE;
        } else if(choice == 1){

            service.acceptCandidate();
            // OPT - send a notification to the relevant department

            return ScreenInput.TO_VIEW_APPLICATIONS;
        } else {
            return ScreenInput.NONE;
        }
    }
}

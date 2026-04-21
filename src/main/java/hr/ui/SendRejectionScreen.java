package hr.ui;

import hr.ScreenInput;
import hr.service.HRService;

import java.util.Scanner;

public class SendRejectionScreen implements Screen {

    private HRService service;

    public SendRejectionScreen(HRService service){
        this.service = service;
    }


    @Override
    public void show() {
        System.out.printf("Send %s the offer\n\nDear %s, Thank you for your interest in the %s position at Fashion House and for taking the time to submit your application.\nWe received a number of applications from qualified candidates, and after careful consideration, we have decided to move forward with another applicant whose skills and experience better align with our current needs.\nWe appreciate your interest in joining Fashion House and wish you the best of luck in your job search.\nBest regards, Fashion House\n\n",
                service.getSelectedCandidate().getName(), service.getSelectedCandidate().getName(), service.getSelectedApplication().getTitle());
        System.out.print("Please note that this is final, you cannot un-reject a candidate\n\n");
        System.out.printf("0: back to candidate\n1: send the rejection\n---> ");
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);
        int choice = scn.nextInt();

        if(choice == 0){
            return ScreenInput.TO_EDIT_APPLICATION;
        } else if(choice == 1){

            service.rejectCandidate();
            return ScreenInput.TO_VIEW_APPLICATIONS;
        } else {
            return ScreenInput.NONE;
        }
    }
}

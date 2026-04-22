package hr.ui;

import common.model.Timestamp;
import hr.ScreenInput;
import hr.model.Application;
import hr.model.Employee;
import hr.service.HRService;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Mason Hart
 */
public class CreateApplicationScreen implements Screen {

    /**
     * 0 - title
     * 1 - desc
     * 2 - dept
     * 3 - set close date
     * 4 - # open positions
     */
    int stage = 0;

    private HRService service;
    private Application newApp;
    private boolean enterInput;

    // ── show() prompt per stage ───────────────────────────────────────────────
    private static final Map<Integer, Runnable> STAGE_DISPLAY = new HashMap<>();
    static {
        STAGE_DISPLAY.put(0, () -> System.out.print("Enter job title ---> "));
        STAGE_DISPLAY.put(1, () -> System.out.print("Enter job description ---> "));
        STAGE_DISPLAY.put(2, () -> System.out.print("0: SALES\n1: ANALYTICS\n2: PRODUCTION\n3: LEGAL\n4: ADVERTISING\n5: HR\n6: SECURITY\n7: OTHER\nEnter department ---> "));
        STAGE_DISPLAY.put(3, () -> System.out.print("Enter close date ---> "));
        STAGE_DISPLAY.put(4, () -> System.out.print("Number of positions offered ---> "));
    }

    public CreateApplicationScreen(HRService service){
        this.newApp = new Application();
        this.service = service;
        this.enterInput = false;
    }

    @Override
    public void show() {
        if (!enterInput) {
            System.out.print("Create New Application\n\n");
            System.out.printf("Title : %s\nDesc  : %s\nDept  : %s\nCloses: %s\nNum   : %d\n\n",
                    newApp.getTitle(), newApp.getDescription(), newApp.getDept(), newApp.getCloseDate(), newApp.getNumPositions());
            System.out.printf("0:main menu\n1:view applications\n2:edit\n3:submit\n---> ");
        } else {
            Runnable display = STAGE_DISPLAY.get(stage);
            if (display != null) display.run();
        }
    }

    @Override
    public ScreenInput processInput() {
        Scanner scn = new Scanner(System.in);

        if (!enterInput) {
            // ── Main-menu choices ─────────────────────────────────────────────
            Map<Integer, Supplier<ScreenInput>> mainMenuActions = Map.of(
                    0, () -> { newApp = new Application(); return ScreenInput.TO_MAIN; },
                    1, () -> { newApp = new Application(); return ScreenInput.TO_VIEW_APPLICATIONS; },
                    2, () -> { stage = 0; enterInput = true; return ScreenInput.NONE; },
                    3, () -> { service.createApplication(newApp); newApp = new Application(); return ScreenInput.TO_EDIT_APPLICATION; }
            );
            int choice = scn.nextInt();
            return mainMenuActions.getOrDefault(choice, () -> ScreenInput.NONE).get();

        } else {
            // ── Stage-based field entry ───────────────────────────────────────
            Map<Integer, Consumer<String>> stageInputMap = new HashMap<>();
            stageInputMap.put(0, input -> { newApp.setTitle(input);                                              stage = 1; });
            stageInputMap.put(1, input -> { newApp.setDescription(input);                                        stage = 2; });
            stageInputMap.put(2, input -> { newApp.setDept(Employee.DEPARTMENTS[Integer.parseInt(input)]);       stage = 3; });
            stageInputMap.put(3, input -> { newApp.setCloseDate(new Timestamp(input + "T00:00:00"));             stage = 4; });
            stageInputMap.put(4, input -> { newApp.setNumPositions(Integer.parseInt(input)); stage = 0; enterInput = false; });

            String input = scn.nextLine();
            Consumer<String> handler = stageInputMap.get(stage);
            if (handler != null) handler.accept(input);
        }

        return ScreenInput.NONE;
    }
}

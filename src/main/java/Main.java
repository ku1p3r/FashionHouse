
import common.util.Terminal;

public class Main{

    /**
     * Primary main call for complete program.
     *
     * @param args
     */
    public static void main(String[] args){

        new MainMenuScreen(args).run();

        System.out.println(Terminal.YELLOW + "Shutting down." + Terminal.RESET);
    }
}

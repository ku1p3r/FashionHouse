package legal;

import java.util.Scanner;

public class RegisterLicense {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            new LegalRegisterScreen(scanner).run();
        } finally {
            scanner.close();
        }
    }
}

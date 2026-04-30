package legal;

import common.base.ScreenProgramTemplate;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

/**
 * Single-pass license registration CLI using {@link ScreenProgramTemplate}.
 */
final class LegalRegisterScreen extends ScreenProgramTemplate<Void, Boolean> {

    private final Scanner scanner;

    LegalRegisterScreen(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        System.out.println("=== License Registration ===");
    }

    @Override
    protected Boolean readInput(Void unused) {
        try {
            System.out.println("Enter company ID (UUID):");
            UUID companyID = UUID.fromString(scanner.nextLine());

            System.out.println("Enter company Name:");
            String companyName = scanner.nextLine();

            PartnerCompany partner = new PartnerCompany(companyID, companyName);

            System.out.println("Enter IP ID (UUID):");
            UUID id = UUID.fromString(scanner.nextLine());

            System.out.println("Enter IP name being licensed:");
            String IPName = scanner.nextLine();

            IntellectualProperty ip = new IntellectualProperty(id, IPName);

            System.out.println("Will the license be exclusive? Type Y for yes and anything else for no:");
            String input = scanner.nextLine().trim();

            LicenseType type;
            if (input.equalsIgnoreCase("Y")) {
                type = LicenseType.EXCLUSIVE;
            } else {
                type = LicenseType.NON_EXCLUSIVE;
            }

            System.out.println("Enter licensee region:");
            String region = scanner.nextLine();

            System.out.println("Enter licensee product:");
            String product = scanner.nextLine();

            System.out.println("Enter month amount for term:");
            int months = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter license fee rate as double:");
            double fee = Double.parseDouble(scanner.nextLine());

            LicenseService service = new LicenseService();

            LicenseAgreement license = service.registerLicense(
                    ip,
                    partner,
                    type,
                    region,
                    product,
                    LocalDate.now(),
                    LocalDate.now().plusMonths(months),
                    fee
            );

            System.out.println("License created with ID: " + license.getLicenseId());

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input format (UUID, number, etc.): " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
        return true;
    }

    @Override
    protected Void nextScreen(Void current, Boolean input) {
        return null;
    }

    @Override
    protected boolean shouldExit(Boolean input) {
        return Boolean.TRUE.equals(input);
    }
}

package advertising;

import java.util.List;
import java.util.Scanner;

/*
 * Fashion House Project
 * Advertising Module - Iteration 1
 * Test Runner for Register New Advertisement
 *
 * Runs and demonstrates the advertising use case.
 * Author: Gilbert
 */
public class AdvertisingMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdvertisingSystem system = new AdvertisingSystem();

        // Sample data
        system.addProduct(new Product(1, "Leather Jacket"));
        system.addProduct(new Product(2, "Silk Dress"));

        system.addCollection(new Collection(1, "Spring 2026"));
        system.addCollection(new Collection(2, "Luxury Nightwear"));

        system.addPlatform(new Platform("Instagram"));
        system.addPlatform(new Platform("Billboard"));
        system.addPlatform(new Platform("Magazine"));

        MarketingManager manager = new MarketingManager(1, "Gilbert");

        boolean running = true;

        while (running) {
            System.out.println("\n=== Advertising Module ===");
            System.out.println("1. Register New Advertisement");
            System.out.println("2. View All Advertisements");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("\n=== Register New Advertisement ===");

                    System.out.print("Enter advertisement title: ");
                    String title = scanner.nextLine();

                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();

                    System.out.print("Enter target audience: ");
                    String targetAudience = scanner.nextLine();

                    System.out.print("Enter duration (days): ");
                    int duration;
                    try {
                        duration = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid duration. Please enter a number.");
                        break;
                    }

                    System.out.print("Enter promoted item name: ");
                    String promotedItemName = scanner.nextLine();

                    System.out.print("Enter promoted item type (Product or Collection): ");
                    String promotedItemType = scanner.nextLine();

                    System.out.print("Enter platform name: ");
                    String platformName = scanner.nextLine();

                    manager.registerAdvertisement(
                            system,
                            title,
                            description,
                            targetAudience,
                            duration,
                            promotedItemName,
                            promotedItemType,
                            platformName
                    );
                    break;

                case "2":
                    System.out.println("\n=== Stored Advertisements ===");
                    List<Advertisement> advertisements = system.getAdvertisements();

                    if (advertisements.isEmpty()) {
                        System.out.println("No advertisements registered yet.");
                    } else {
                        for (Advertisement ad : advertisements) {
                            System.out.println(ad);
                        }
                    }
                    break;

                case "3":
                    running = false;
                    System.out.println("Exiting Advertising Module.");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }
}
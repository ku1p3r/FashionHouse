package advertising;

import catalog.ui.StoreSelectionScreen;
import java.util.Scanner;

/*
 * Fashion House Project
 * Advertising Module - Iteration 1,2
 * Test Runner for Register New Advertisement
 *
 * Runs and demonstrates the advertising use case.
 * Author: Gilbert
 */
public class AdvertisingMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AdvertisingSystem system = new AdvertisingSystem();
        StoreSelectionScreen storeSelectionScreen = new StoreSelectionScreen();

        // Sample data
        system.addProduct(new Product(1, "Leather Jacket"));
        system.addProduct(new Product(2, "Silk Dress"));

        system.addCollection(new Collection(1, "Spring 2026"));
        system.addCollection(new Collection(2, "Luxury Nightwear"));

        system.addPlatform(new Platform("Instagram"));
        system.addPlatform(new Platform("Billboard"));
        system.addPlatform(new Platform("Magazine"));

        system.addVenue(new Venue(1, "Paris Hall", "Paris"));
        system.addVenue(new Venue(2, "New York Runway Center", "New York"));

        system.addEventType(new EventType(1, "Fashion Show"));
        system.addEventType(new EventType(2, "Third-Party Event"));

        system.addPartner(new Partner(1, "Paris Fashion Group", "contact@parisfashion.com"));
        system.addPartner(new Partner(2, "Global Runway Partners", "info@globalrunway.com"));

        MarketingManager manager = new MarketingManager(1, "Gilbert");

        new AdvertisingProgramScreen(scanner, system, manager, storeSelectionScreen).run();
    }
}

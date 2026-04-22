package advertising;

import catalog.service.CatalogService;
import catalog.ui.StoreSelectionScreen;
import common.util.MenuInvoker;
import common.util.Terminal;
import java.util.List;
import java.util.Optional;
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

    private final AdvertisingSystem system = new AdvertisingSystem();
    private final Scanner scanner = new Scanner(System.in);
    private final StoreSelectionScreen storeSelectionScreen = new StoreSelectionScreen();
    private final MarketingManager manager = new MarketingManager(1, "Gilbert");

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        new AdvertisingMain().run();
    }

    private void run() {
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

        MenuInvoker menu = new MenuInvoker();
        menu.register("1", "Register New Advertisement",  this::registerNewAdvertisement)
            .register("2", "View All Advertisements",     this::viewAllAdvertisements)
            .register("3", "Schedule New Event",          this::scheduleNewEvent)
            .register("4", "View All Events",             this::viewAllEvents)
            .register("5", "Register Product in Event",   this::registerProductInEvent)
            .register("6", "View Event Registrations",    this::viewEventRegistrations)
            .register("7", "Exit",                        menu::stop);

        while (menu.isRunning()) {
            Terminal.printHeader("Advertising Module");
            menu.printOptions();
            Terminal.println();
            menu.execute(Terminal.prompt("Choose an option:"));
        }

        System.out.println("Exiting Advertising Module.");
        scanner.close();
    }

    // ── Command implementations ───────────────────────────────────────────────

    private void registerNewAdvertisement() {
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
            return;
        }
        System.out.print("Enter promoted item name: ");
        String promotedItemName = scanner.nextLine();
        System.out.print("Enter promoted item type (Product or Collection): ");
        String promotedItemType = scanner.nextLine();
        System.out.print("Enter platform name: ");
        String platformName = scanner.nextLine();

        manager.registerAdvertisement(system, title, description, targetAudience,
                duration, promotedItemName, promotedItemType, platformName);
    }

    private void viewAllAdvertisements() {
        System.out.println("\n=== Stored Advertisements ===");
        List<Advertisement> advertisements = system.getAdvertisements();
        if (advertisements.isEmpty()) {
            System.out.println("No advertisements registered yet.");
        } else {
            for (Advertisement ad : advertisements) {
                System.out.println(ad);
            }
        }
    }

    private void scheduleNewEvent() {
        System.out.println("\n=== Schedule New Event ===");
        System.out.print("Enter event name: ");        String eventName = scanner.nextLine();
        System.out.print("Enter event type: ");        String eventTypeName = scanner.nextLine();
        System.out.print("Enter venue name: ");        String venueName = scanner.nextLine();
        System.out.print("Enter event date: ");        String eventDate = scanner.nextLine();
        System.out.print("Enter event description: "); String eventDescription = scanner.nextLine();
        System.out.print("Enter image/poster path: "); String imagePath = scanner.nextLine();
        System.out.print("Enter partner name (or leave blank): "); String partnerName = scanner.nextLine();
        System.out.print("Enter security level (1=Low, 2=Medium, 3=High): ");
        int securityLevel;
        try {
            securityLevel = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid security level.");
            return;
        }

        if (!system.validateEventInformation(eventName, eventTypeName, venueName, eventDate,
                eventDescription, imagePath, partnerName, securityLevel)) {
            System.out.println("Event scheduling failed: invalid information.");
            return;
        }

        Event createdEvent = system.createEvent(eventName, eventTypeName, venueName, eventDate,
                eventDescription, imagePath, partnerName, securityLevel);
        System.out.println("Event scheduled successfully.");
        System.out.println(createdEvent);
    }

    private void viewAllEvents() {
        System.out.println("\n=== All Events ===");
        List<Event> events = system.viewEvents();
        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event event : events) {
                System.out.println("Event ID: "      + event.getEventId());
                System.out.println("Name: "          + event.getName());
                System.out.println("Date: "          + event.getDate());
                System.out.println("Description: "   + event.getDescription());
                System.out.println("Event Type: "    + event.getEventTypeName());
                System.out.println("Venue: "         + event.getVenueName());
                System.out.println("Partner: "       + event.getPartnerName());
                System.out.println("Image Path: "    + event.getImagePath());
                System.out.println("Security Level: "+ event.getSecurityLevel());
                System.out.println("----------------------------------------");
            }
        }
    }

    private void registerProductInEvent() {
        System.out.println("\n=== Register Product in Event ===");
        System.out.print("Enter event ID: ");
        int eventId;
        try {
            eventId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid event ID. Please enter a number.");
            return;
        }

        if (!system.eventExists(eventId)) {
            System.out.println("Event not found.");
            return;
        }

        CatalogService catalogService = storeSelectionScreen.run();
        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();

        Optional<common.model.Product> productOpt = catalogService.findById(productId);
        if (productOpt.isEmpty()) {
            System.out.println("Product not found.");
            return;
        }

        common.model.Product selectedProduct = productOpt.get();
        EventRegistration registration = system.registerProductInEvent(
                eventId, selectedProduct.getName(), "Product");
        System.out.println("Product/Collection registered in event successfully.");
        System.out.println(registration);
    }

    private void viewEventRegistrations() {
        System.out.println("\n=== View Event Registrations ===");
        System.out.print("Enter event ID: ");
        int registrationEventId;
        try {
            registrationEventId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid event ID. Please enter a number.");
            return;
        }

        if (!system.eventExists(registrationEventId)) {
            System.out.println("Event not found.");
            return;
        }

        List<EventRegistration> eventRegistrations = system.viewEventRegistrations(registrationEventId);

        if (eventRegistrations.isEmpty()) {
            System.out.println("No registrations found for this event.");
            return;
        }

        Event event = system.getEventById(registrationEventId);
        System.out.println("\n========== EVENT REGISTRATIONS ==========");
        System.out.println("Event Name        : " + (event != null ? event.getName() : "Unknown"));
        System.out.println("Event ID          : " + registrationEventId);
        System.out.println("Total Registrations: " + eventRegistrations.size());
        System.out.println("========================================");

        int count = 1;
        for (EventRegistration reg : eventRegistrations) {
            System.out.println("\n------------------------------------");
            System.out.println(" Registration #" + count++);
            System.out.println("------------------------------------");
            System.out.println(" Event ID      : " + reg.getEventId());
            System.out.println(" Product Name  : " + reg.getFeaturedItemName());
            System.out.println(" Type          : " + reg.getFeaturedItemType());
            System.out.println("------------------------------------");
        }
    }
}
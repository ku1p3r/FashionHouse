package advertising;

import catalog.service.CatalogService;
import catalog.ui.StoreSelectionScreen;
import common.base.ScreenProgramTemplate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Advertising CLI menu using {@link ScreenProgramTemplate}.
 */
final class AdvertisingProgramScreen extends ScreenProgramTemplate<Void, String> {

    private final Scanner scanner;
    private final AdvertisingSystem system;
    private final MarketingManager manager;
    private final StoreSelectionScreen storeSelectionScreen;

    AdvertisingProgramScreen(Scanner scanner,
                             AdvertisingSystem system,
                             MarketingManager manager,
                             StoreSelectionScreen storeSelectionScreen) {
        this.scanner = scanner;
        this.system = system;
        this.manager = manager;
        this.storeSelectionScreen = storeSelectionScreen;
    }

    @Override
    protected Void initialScreen() {
        return null;
    }

    @Override
    protected void render(Void unused) {
        System.out.println("\n=== Advertising Module ===");
        System.out.println("1. Register New Advertisement");
        System.out.println("2. View All Advertisements");
        System.out.println("3. Schedule New Event");
        System.out.println("4. View All Events");
        System.out.println("5. Register Product in Event");
        System.out.println("6. View Event Registrations");
        System.out.println("7. Exit");
    }

    @Override
    protected String readInput(Void unused) {
        System.out.print("Choose an option: ");
        return scanner.nextLine();
    }

    @Override
    protected Void nextScreen(Void current, String choice) {
        handleChoice(choice);
        return null;
    }

    @Override
    protected boolean shouldExit(String input) {
        return input != null && "7".equals(input.trim());
    }

    private void handleChoice(String choice) {
        if (choice == null) {
            return;
        }
        switch (choice.trim()) {
            case "1" -> {
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
            }

            case "2" -> {
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

            case "7" -> System.out.println("Exiting Advertising Module.");

            case "3" -> {
                System.out.println("\n=== Schedule New Event ===");

                System.out.print("Enter event name: ");
                String eventName = scanner.nextLine();

                System.out.print("Enter event type: ");
                String eventTypeName = scanner.nextLine();

                System.out.print("Enter venue name: ");
                String venueName = scanner.nextLine();

                System.out.print("Enter event date: ");
                String eventDate = scanner.nextLine();

                System.out.print("Enter event description: ");
                String eventDescription = scanner.nextLine();

                System.out.print("Enter image/poster path: ");
                String imagePath = scanner.nextLine();

                System.out.print("Enter partner name (or leave blank): ");
                String partnerName = scanner.nextLine();
                System.out.print("Enter security level (1=Low, 2=Medium, 3=High): ");
                int securityLevel;

                try {
                    securityLevel = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid security level.");
                    break;
                }

                if (!system.validateEventInformation(
                        eventName,
                        eventTypeName,
                        venueName,
                        eventDate,
                        eventDescription,
                        imagePath,
                        partnerName,
                        securityLevel
                )) {
                    System.out.println("Event scheduling failed: invalid information.");
                    break;

                }

                Event createdEvent = system.createEvent(
                        eventName,
                        eventTypeName,
                        venueName,
                        eventDate,
                        eventDescription,
                        imagePath,
                        partnerName,
                        securityLevel
                );

                System.out.println("Event scheduled successfully.");
                System.out.println(createdEvent);
            }

            case "4" -> {
                System.out.println("\n=== All Events ===");

                List<Event> events = system.viewEvents();

                if (events.isEmpty()) {
                    System.out.println("No events found.");
                } else {
                    for (Event event : events) {
                        System.out.println("Event ID: " + event.getEventId());
                        System.out.println("Name: " + event.getName());
                        System.out.println("Date: " + event.getDate());
                        System.out.println("Description: " + event.getDescription());
                        System.out.println("Event Type: " + event.getEventTypeName());
                        System.out.println("Venue: " + event.getVenueName());
                        System.out.println("Partner: " + event.getPartnerName());
                        System.out.println("Image Path: " + event.getImagePath());
                        System.out.println("Security Level: " + event.getSecurityLevel());
                        System.out.println("----------------------------------------");
                    }
                }
            }

            case "5" -> {
                System.out.println("\n=== Register Product in Event ===");

                System.out.print("Enter event ID: ");
                int eventId;
                try {
                    eventId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid event ID. Please enter a number.");
                    break;
                }

                if (!system.eventExists(eventId)) {
                    System.out.println("Event not found.");
                    break;
                }

                CatalogService catalogService = storeSelectionScreen.run();

                System.out.print("Enter Product ID: ");
                String productId = scanner.nextLine();

                Optional<common.model.Product> productOpt = catalogService.findById(productId);

                if (productOpt.isEmpty()) {
                    System.out.println("Product not found.");
                    break;
                }

                common.model.Product selectedProduct = productOpt.get();
                String featuredItemName = selectedProduct.getName();
                String featuredItemType = "Product";

                EventRegistration registration = system.registerProductInEvent(
                        eventId,
                        featuredItemName,
                        featuredItemType
                );

                System.out.println("Product/Collection registered in event successfully.");
                System.out.println(registration);
            }

            case "6" -> {
                System.out.println("\n=== View Event Registrations ===");

                System.out.print("Enter event ID: ");
                int registrationEventId;
                try {
                    registrationEventId = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid event ID. Please enter a number.");
                    break;
                }

                if (!system.eventExists(registrationEventId)) {
                    System.out.println("Event not found.");
                    break;
                }

                List<EventRegistration> eventRegistrations = system.viewEventRegistrations(registrationEventId);

                if (eventRegistrations.isEmpty()) {
                    System.out.println("No registrations found for this event.");
                } else {
                    Event event = system.getEventById(registrationEventId);

                    System.out.println("\n========== EVENT REGISTRATIONS ==========");

                    if (event != null) {
                        System.out.println("Event Name        : " + event.getName());
                    } else {
                        System.out.println("Event Name        : Unknown");
                    }

                    System.out.println("Event ID          : " + registrationEventId);
                    System.out.println("Total Registrations: " + eventRegistrations.size());
                    System.out.println("========================================");

                    int count = 1;
                    for (EventRegistration eventRegistration : eventRegistrations) {
                        System.out.println("\n------------------------------------");
                        System.out.println(" Registration #" + count++);
                        System.out.println("------------------------------------");
                        System.out.println(" Event ID      : " + eventRegistration.getEventId());
                        System.out.println(" Product Name  : " + eventRegistration.getFeaturedItemName());
                        System.out.println(" Type          : " + eventRegistration.getFeaturedItemType());
                        System.out.println("------------------------------------");
                    }
                }
            }
            default -> System.out.println("Invalid option. Please choose 1 through 7.");
        }
    }
}

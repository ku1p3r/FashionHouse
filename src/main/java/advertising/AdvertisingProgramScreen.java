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
 //new        
        System.out.println("8. Sign New Fashion Model");
        System.out.println("9. Assign Model to Event");
        System.out.println("10. Assign Model to Advertisement");
        System.out.println("11. View Models");
        System.out.println("12. View Models Assigments");
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
// new 

case "8" -> {
    signNewFashionModel();
}

case "9" -> {
    assignModelToEvent();
}

case "10" -> {
    assignModelToAdvertisement();
}
case "11"->{
    viewFashionModels();
}

case "12"->{
    viewModelAssignments(); 
}


            default -> System.out.println("Invalid option. Please choose 1 through 12.");
        }







    }




    //new

        /*
 * Signs a new fashion model and saves the profile to models.txt.
 *  Sign New Fashion Model.
 */
private void signNewFashionModel() {
    System.out.println("\n=== Sign New Fashion Model ===");

    System.out.print("Enter model ID: ");
    String id = scanner.nextLine();

    System.out.print("Enter model name: ");
    String name = scanner.nextLine();

    System.out.print("Enter agency: ");
    String agency = scanner.nextLine();

    System.out.print("Enter category (Runway/Commercial/Editorial): ");
    String category = scanner.nextLine();

    System.out.print("Enter image path: ");
    String imagePath = scanner.nextLine();

    FashionModel model = new FashionModel(id, name, agency, category, imagePath, "Active");

    try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("models.txt", true))) {
        writer.write(model.serialize());
        writer.newLine();

        System.out.println("\nModel signed successfully.");
        System.out.println(model.displayProfile());
    } catch (java.io.IOException e) {
        System.out.println("Error saving model: " + e.getMessage());
    }
}

/*
 * Assigns an existing fashion model to an event.
 * Strategy Pattern by selecting EventAssignmentStrategy.
 */
private void assignModelToEvent() {
    System.out.println("\n=== Assign Model to Event ===");

    viewFashionModels();

    System.out.print("Enter model ID: ");
    String modelId = scanner.nextLine();

    FashionModel model = findModelById(modelId);

    if ( model == null) {
        System.out.println("Model not found.");
        return;
    }
System.out.println("\nSelected Model:");
System.out.println(model.displayProfile());

    System.out.print("Enter event ID: ");
    String eventId = scanner.nextLine();

    

    /*
     * Strategy Pattern:
     *  choose the event assignment behavior here.
     */
    AssignmentStrategy strategy = new EventAssignmentStrategy();

    strategy.assign(model, eventId);
}

/*
 * Assigns an existing fashion model to an advertisement.
 * Uses Strategy Pattern with AdvertisementAssignmentStrategy.
 */
private void assignModelToAdvertisement() {
    System.out.println("\n=== Assign Model to Advertisement ===");

    viewFashionModels();

    System.out.print("Enter model ID: ");
    String modelId = scanner.nextLine();

    FashionModel model = findModelById(modelId);
    if ( model == null) {
        System.out.println("Model not found.");
        return;
    }
System.out.println("\nSelected Model:");
System.out.println(model.displayProfile());

    System.out.print("Enter advertisement ID: ");
    String advertisementId = scanner.nextLine();
    /*
     * Strategy Pattern:
     * Select advertisement assignment behavior
     */
    AssignmentStrategy strategy = new AdvertisementAssignmentStrategy();

    strategy.assign(model, advertisementId);
}


/*
 * Displays  models  
 */
private void viewFashionModels() {
    System.out.println("\n=== Available Fashion Models ===");

    try (java.io.BufferedReader reader =
            new java.io.BufferedReader(new java.io.FileReader("models.txt"))) {

        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            FashionModel model = FashionModel.deserialize(line);

            if (model.getStatus().equalsIgnoreCase("Active")) {
                System.out.println(model.displayProfile());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No active fashion models found.");
        }

    } catch (java.io.IOException e) {
        System.out.println("No fashion model records found yet.");
    }
}



/*
 * Displays saved model assignments to events and advertisements
 */
private void viewModelAssignments() {
    System.out.println("\n=== Model Assignments ===");

    System.out.println("\n--- Event Assignments ---");
    try (java.io.BufferedReader reader =
            new java.io.BufferedReader(new java.io.FileReader("model_event_assignments.txt"))) {

        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {




                // System.out.println("Model ID: " + parts[0] + " | Event ID: " + parts[1]);
                FashionModel model = findModelById(parts[0]);
                if (model != null){
                    System.out.println("Model: "+ model.getName()+ " ("+ model.getId()+ ") | Event ID: " + parts [1] );

                }else {
                    System.out.println("Model ID: " + parts[0] + " | Event ID: " + parts[1]);
                }


                found = true;
            }
        }

        if (!found) {
            System.out.println("No event assignments found.");
        }

    } catch (java.io.IOException e) {
        System.out.println("No event assignment records found yet.");
    }

    System.out.println("\n--- Advertisement Assignments ---");
    try (java.io.BufferedReader reader =
            new java.io.BufferedReader(new java.io.FileReader("model_ad_assignments.txt"))) {

        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");
            if (parts.length >= 2) {
                
                
               // System.out.println("Model ID: " + parts[0] + " | Advertisement ID: " + parts[1]);
        FashionModel model = findModelById(parts[0]);

                    if (model != null){
                    System.out.println("Model: "+ model.getName()+ " ("+ model.getId()+ ") | Advertisement ID: " + parts [1] );

                }else {
                    System.out.println("Model ID: " + parts[0] + " | Advertisement ID: " + parts[1]);
                }





                found = true;
            }
        }

        if (!found) {
            System.out.println("No advertisement assignments found.");
        }

    } catch (java.io.IOException e) {
        System.out.println("No advertisement assignment records found yet.");
    }
}

/*
 * returns a Model by ID from models.txt
 */
private FashionModel findModelById(String modelId) {
    try (java.io.BufferedReader reader =
            new java.io.BufferedReader(new java.io.FileReader("models.txt"))) {

        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            FashionModel model = FashionModel.deserialize(line);

            if (model.getId().equalsIgnoreCase(modelId)) {
                return model;
            }
        }

    } catch (java.io.IOException e) {
        System.out.println("Error reading models file.");
    }

    return null; 
}














}

package advertising;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Fashion House Project
 * Advertising Module - Iteration 1
 * Use Case: Register New Advertisement
 *
 * Handles advertisement validation, creation, loading, and file storage.
 * Author: Gilbert
 */
public class AdvertisingSystem {
    private static final String FILE_NAME = "advertisements.txt";
private static final String EVENTS_FILE_NAME = "events.txt";
private static final String REGISTRATIONS_FILE_NAME = "event_registrations.txt";



    private List<Product> products;
    private List<Collection> collections;
    private List<Platform> platforms;
    private List<Advertisement> advertisements;
    private int nextAdvertisementId;

///////added
private List<Event> events;
private List<EventRegistration> registrations;
private List<Venue> venues;
private List<EventType> eventTypes;
private List<Partner> partners;

//updated 
public AdvertisingSystem() {
    products = new ArrayList<>();
    collections = new ArrayList<>();
    platforms = new ArrayList<>();
    advertisements = new ArrayList<>();

    events = new ArrayList<>();
    registrations = new ArrayList<>();
    venues = new ArrayList<>();
    eventTypes = new ArrayList<>();
    partners = new ArrayList<>();

    nextAdvertisementId = 1;

    loadAdvertisementsFromFile();
    loadEventsFromFile();
 loadRegistrationsFromFile();
}


/////////
    public void addProduct(Product product) {
        products.add(product);
    }

    public void addCollection(Collection collection) {
        collections.add(collection);
    }

    public void addPlatform(Platform platform) {
        platforms.add(platform);
    }

/////added
public void addVenue(Venue venue) {
    venues.add(venue);
}

public void addEventType(EventType eventType) {
    eventTypes.add(eventType);
}

public void addPartner(Partner partner) {
    partners.add(partner);
}
///

//added

    public void displayAdForm() {
        System.out.println("Displaying advertisement registration form...");
    }

    public boolean validateInformation(String title, String description,
                                       String targetAudience, int duration,
                                       String promotedItemName,
                                       String promotedItemType,
                                       String platformName) {
        if (title == null || title.isBlank()) return false;
        if (description == null || description.isBlank()) return false;
        if (targetAudience == null || targetAudience.isBlank()) return false;
        if (duration <= 0) return false;
        if (promotedItemName == null || promotedItemName.isBlank()) return false;
        if (promotedItemType == null || promotedItemType.isBlank()) return false;
        if (platformName == null || platformName.isBlank()) return false;

        boolean itemExists = false;

        Map<String, java.util.function.Supplier<Boolean>> itemCheckers = Map.of(
                "product",    () -> checkProduct(promotedItemName),
                "collection", () -> checkCollection(promotedItemName)
        );
        java.util.function.Supplier<Boolean> checker = itemCheckers.get(promotedItemType.toLowerCase());
        if (checker != null) itemExists = checker.get();

        boolean platformExists = checkPlatform(platformName);

        return itemExists && platformExists;
    }

    public boolean checkProduct(String productName) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollection(String collectionName) {
        for (Collection collection : collections) {
            if (collection.getName().equalsIgnoreCase(collectionName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPlatform(String platformName) {
        for (Platform platform : platforms) {
            if (platform.getPlatformName().equalsIgnoreCase(platformName)) {
                return true;
            }
        }
        return false;
    }

//added


// checks:
// the venue exists
// the event type exists
// the partner exists

public boolean checkVenue(String venueName) {
    for (Venue venue : venues) {
        if (venue.getName().equalsIgnoreCase(venueName)) {
            return true;
        }
    }
    return false;
}

public boolean checkEventType(String typeName) {
    for (EventType eventType : eventTypes) {
        if (eventType.getTypeName().equalsIgnoreCase(typeName)) {
            return true;
        }
    }
    return false;
}

public boolean checkPartner(String partnerName) {
    for (Partner partner : partners) {
        if (partner.getName().equalsIgnoreCase(partnerName)) {
            return true;
        }
    }
    return false;
}

///


public boolean validateEventInformation(String name,
                                        String typeName,
                                        String venueName,
                                        String date,
                                        String description,
                                        String imagePath,
                                        String partnerName,
                                        int securityLevel) {

    if (name == null || name.isBlank()) return false;
    if (typeName == null || typeName.isBlank()) return false;
    if (venueName == null || venueName.isBlank()) return false;
    if (date == null || date.isBlank()) return false;
    if (description == null || description.isBlank()) return false;
    // added security checks
    if (securityLevel < 1 || securityLevel > 3) return false;
    return true;
}






//added
//creates a new Event
// gives it an id
// stores it in the events list
// returns it


public Event createEvent(String name,
                         String typeName,
                         String venueName,
                         String date,
                         String description,
                         String imagePath,
                         String partnerName,
                         int securityLevel ) {
    int eventId = events.size() + 1;

    Event event = new Event(
            eventId,
            name,
            date,
            description,
            typeName,
            venueName,
            partnerName,
            imagePath,
            securityLevel
    );

    events.add(event);
    saveEventsToFile();
    return event;
}


//added
// AdvertisingMain
// or  main 

// ask  system for all saved events.

public List<Event> viewEvents() {
    return events;
}

// added
// links a product or collection to an  event
// creates a EventRegistration
// stores it
// returns it

//
public EventRegistration registerProductInEvent(int eventId,
                                                String itemName,
                                                String itemType) {

    int registrationId = registrations.size() + 1;

    EventRegistration registration = new EventRegistration(
            registrationId,
            eventId,
            itemName,
            itemType
    );

    registrations.add(registration);
    saveRegistrationsToFile();
    return registration;
}


// find all products/collections registered to one event
// return only the registrations for that event

public List<EventRegistration> viewEventRegistrations(int eventId) {
    List<EventRegistration> result = new ArrayList<>();

    for (EventRegistration registration : registrations) {
        if (registration.getEventId() == eventId) {
            result.add(registration);
        }
    }

    return result;
}

// checks:

// the event exists
// the item name is not blank
// the item type is not blank
// if type is Product, that product exists
// if type is Collection, that collection exists

public boolean validateEventRegistration(int eventId,
                                         String itemName,
                                         String itemType) {
    boolean validEvent = false;

    for (Event event : events) {
        if (event.getEventId() == eventId) {
            validEvent = true;
            break;
        }
    }

    if (!validEvent) return false;
    if (itemName == null || itemName.isBlank()) return false;
    if (itemType == null || itemType.isBlank()) return false;

    Map<String, java.util.function.Supplier<Boolean>> itemCheckers = Map.of(
            "product",    () -> checkProduct(itemName),
            "collection", () -> checkCollection(itemName)
    );
    java.util.function.Supplier<Boolean> checker = itemCheckers.get(itemType.toLowerCase());
    return checker != null && checker.get();
}










///
    public Advertisement createAdvertisement(String title,
                                             String description,
                                             String targetAudience,
                                             int duration,
                                             String promotedItemName,
                                             String promotedItemType,
                                             String platformName) {
        return new Advertisement(
                nextAdvertisementId++,
                title,
                description,
                targetAudience,
                duration,
                platformName,
                promotedItemName,
                promotedItemType
        );
    }

    public boolean storeAdvertisement(Advertisement advertisement) {
        advertisements.add(advertisement);
        saveAdvertisementsToFile();
        return true;
    }

    public void registerAdvertisement(String title,
                                      String description,
                                      String targetAudience,
                                      int duration,
                                      String promotedItemName,
                                      String promotedItemType,
                                      String platformName) {
        displayAdForm();

        if (!validateInformation(title, description, targetAudience, duration,
                promotedItemName, promotedItemType, platformName)) {
            System.out.println("Advertisement registration failed: invalid information.");
            return;
        }

        Advertisement advertisement = createAdvertisement(
                title, description, targetAudience, duration,
                promotedItemName, promotedItemType, platformName
        );

        boolean saved = storeAdvertisement(advertisement);

        if (saved) {
            System.out.println("Advertisement registered successfully.");
            System.out.println(advertisement);
        } else {
            System.out.println("Advertisement registration failed: could not save advertisement.");
        }
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    private void saveAdvertisementsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Advertisement ad : advertisements) {
                writer.write(
                        ad.getAdvertisementId() + "|" +
                        ad.getTitle() + "|" +
                        ad.getDescription() + "|" +
                        ad.getTargetAudience() + "|" +
                        ad.getDuration() + "|" +
                        ad.getPlatformName() + "|" +
                        ad.getPromotedItemName() + "|" +
                        ad.getPromotedItemType()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving advertisements to file.");
        }
    }



    //added

//writes each event into:

// events.txt

public void saveEventsToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTS_FILE_NAME))) {
        for (Event event : events) {
            writer.write(
                    event.getEventId() + "|" +
                    event.getName() + "|" +
                    event.getDate() + "|" +
                    event.getDescription() + "|" +
                    event.getEventTypeName() + "|" +
                    event.getVenueName() + "|" +
                    event.getPartnerName() + "|" +
                    event.getImagePath() + "|" +
                    event.getSecurityLevel()
            );
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving events to file.");
    }
}



    //

    private void loadAdvertisementsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int maxId = 0;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 8) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    String targetAudience = parts[3];
                    int duration = Integer.parseInt(parts[4]);
                    String platformName = parts[5];
                    String promotedItemName = parts[6];
                    String promotedItemType = parts[7];

                    Advertisement ad = new Advertisement(
                            id,
                            title,
                            description,
                            targetAudience,
                            duration,
                            platformName,
                            promotedItemName,
                            promotedItemType
                    );

                    advertisements.add(ad);

                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }

            nextAdvertisementId = maxId + 1;

        } catch (IOException e) {
            // File may not exist yet on first run, which is okay.
        } catch (NumberFormatException e) {
            System.out.println("Error reading advertisement data from file.");
        }
    }


    //added

// reads from events.txt
// rebuilds your events list
// keeps data 


    public void loadEventsFromFile() {
    events.clear();

    try (BufferedReader reader = new BufferedReader(new FileReader(EVENTS_FILE_NAME))) {
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");

            if (parts.length >= 9) {
                int eventId = Integer.parseInt(parts[0]);
                String name = parts[1];
                String date = parts[2];
                String description = parts[3];
                String typeName = parts[4];
                String venueName = parts[5];
                String partnerName = parts[6];
                String imagePath = parts[7];
                int securityLevel = Integer.parseInt(parts[8]);

                Event event = new Event(
                        eventId,
                        name,
                        date,
                        description,
                        typeName,
                        venueName,
                        partnerName,
                        imagePath,
                        securityLevel
                );

                events.add(event);
            }
        }
    } catch (IOException e) {
        System.out.println("No existing events file found.");
    }
}

///added
public boolean eventExists(int eventId) {
    for (Event event : events) {
        if (event.getEventId() == eventId) {
            return true;
        }
    }
    return false;
}

// return event name 

public Event getEventById(int eventId) {
    for (Event event : events) {
        if (event.getEventId() == eventId) {
            return event;
        }
    }
    return null;
}

// reads from event_registrations.txt
// rebuilds your registrations list
// keeps product/collection registrations

public void loadRegistrationsFromFile() {
    registrations.clear();

    try (BufferedReader reader = new BufferedReader(new FileReader(REGISTRATIONS_FILE_NAME))) {
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\|");

            if (parts.length == 4) {
                int registrationId = Integer.parseInt(parts[0]);
                int eventId = Integer.parseInt(parts[1]);
                String featuredItemName = parts[2];
                String featuredItemType = parts[3];

                EventRegistration registration = new EventRegistration(
                        registrationId,
                        eventId,
                        featuredItemName,
                        featuredItemType
                );

                registrations.add(registration);
            }
        }
    } catch (IOException e) {
        System.out.println("No existing registrations file found.");
    }
}

// writes each registration into:
// event_registrations.txt

public void saveRegistrationsToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(REGISTRATIONS_FILE_NAME))) {
        for (EventRegistration registration : registrations) {
            writer.write(
                    registration.getRegistrationId() + "|" +
                    registration.getEventId() + "|" +
                    registration.getFeaturedItemName() + "|" +
                    registration.getFeaturedItemType()
            );
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving event registrations to file.");
    }
}
}
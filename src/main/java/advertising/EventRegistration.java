package advertising;

public class EventRegistration {
    private final int registrationId;
    private final int eventId;
    private final String featuredItemName;
    private final String featuredItemType;

    public EventRegistration(int registrationId, int eventId, String featuredItemName, String featuredItemType) {
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.featuredItemName = featuredItemName;
        this.featuredItemType = featuredItemType;
    }

    public int getRegistrationId() {
        return registrationId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getFeaturedItemName() {
        return featuredItemName;
    }

    public String getFeaturedItemType() {
        return featuredItemType;
    }

    @Override
    public String toString() {
        return "EventRegistration{" +
                "registrationId=" + registrationId +
                ", eventId=" + eventId +
                ", featuredItemName='" + featuredItemName + '\'' +
                ", featuredItemType='" + featuredItemType + '\'' +
                '}';
    }
}
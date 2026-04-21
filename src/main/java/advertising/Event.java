package advertising;

public class Event {
    private final int eventId;
    private final String name;
    private final String date;
    private final String description;
    private final String eventTypeName;
    private final String venueName;
    private final String partnerName;
    private final String imagePath;
    private  final int securityLevel;

    public Event(int eventId, String name, String date, String description,
                 String eventTypeName, String venueName, String partnerName, String imagePath, int securityLevel) {
        this.eventId = eventId;
        this.name = name;
        this.date = date;
        this.description = description;
        this.eventTypeName = eventTypeName;
        this.venueName = venueName;
        this.partnerName = partnerName;
        this.imagePath = imagePath;
        this.securityLevel = securityLevel;
    }

    public int getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public String getVenueName() {
        return venueName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public String getImagePath() {
        return imagePath;
    }
// added
public int getSecurityLevel(){
    return securityLevel;
}


    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", venueName='" + venueName + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", securityLevel=" + securityLevel +
                '}';
    }
}
package advertising;

public class Venue {
    private final int venueId;
    private final String name;
    private final String location;

    public Venue(int venueId, String name, String location) {
        this.venueId = venueId;
        this.name = name;
        this.location = location;
    }

    public int getVenueId() {
        return venueId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueId=" + venueId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
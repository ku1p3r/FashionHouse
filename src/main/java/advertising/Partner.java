package advertising;

public class Partner {
    private final int partnerId;
    private final String name;
    private final String contactInfo;

    public Partner(int partnerId, String name, String contactInfo) {
        this.partnerId = partnerId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    @Override
    public String toString() {
        return "Partner{" +
                "partnerId=" + partnerId +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
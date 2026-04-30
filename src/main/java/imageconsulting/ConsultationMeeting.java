package imageconsulting;

public class ConsultationMeeting {

    private String dateTime;
    private String clientId;
    private String consultantName;
    private String description;

    public ConsultationMeeting(String dateTime, String clientId,
                               String consultantName, String description) {
        this.dateTime = dateTime;
        this.clientId = clientId;
        this.consultantName = consultantName;
        this.description = description;
    }

    public String getDateTime() { return dateTime; }
    public String getClientId() { return clientId; }
    public String getConsultantName() { return consultantName; }
    public String getDescription() { return description; }
}
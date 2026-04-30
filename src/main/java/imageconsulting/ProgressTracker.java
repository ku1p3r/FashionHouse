package imageconsulting;

public class ProgressTracker implements Observer {

    @Override
    public void update(Client client, String message) {
        System.out.println("[ProgressTracker] " + client.getName()
                + " -> " + message);
    }
}
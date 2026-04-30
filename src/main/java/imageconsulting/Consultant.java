package imageconsulting;

import common.model.Timestamp;
import hr.model.Employee;
import java.util.Random;

public class Consultant extends Employee implements Observer {

    public Consultant(long id, String name) {
        super(
                id,
                name,
                "Image Consultant",
                "OTHER",
                40000,
                new Timestamp(),
                true,
                String.valueOf(new Random().nextInt(0, 10000))
        );
    }

    @Override
    public void update(Client client, String message) {
        System.out.println("[Consultant " + getName() + "] Update for "
                + client.getName() + ": " + message);
    }
}
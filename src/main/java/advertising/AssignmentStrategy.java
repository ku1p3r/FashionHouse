package advertising;

/*
 * Strategy Pattern interface.
 * Any class that assigns a fashion model must implement this method.
 * This allows the system to assign models to different targets
 * without hardcoding all assignment logic in AdvertisingMain.
 */
public interface AssignmentStrategy {

    // Assigns a fashion model to a target.
    void assign(FashionModel model, String targetId);
}
package imageconsulting;

import common.model.Product;

import java.util.*;

public class Client implements Subject {

    private String id;
    private String name;
    private String consultant;
    private String status;
    private boolean caseOpen;
    private String goals;
    private List<Product> wardrobe;

    private List<Observer> observers = new ArrayList<>();

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
        this.caseOpen = true;
        this.wardrobe = new ArrayList<>();
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(this, message);
        }
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        notifyObservers("Status updated to: " + newStatus);
    }

    public void assignClothing(Product product) {
        wardrobe.add(product);
        notifyObservers("Clothing assigned: " + product.getName());
    }

    public void closeCase() {
        this.caseOpen = false;
        notifyObservers("Client case closed.");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getConsultant() { return consultant; }
    public String getStatus() { return status; }
    public boolean isCaseOpen() { return caseOpen; }
    public String getGoals() { return goals; }

    public List<String> getWardrobeIds() {
        List<String> ids = new ArrayList<>();
        for (Product p : wardrobe) {
            ids.add(p.getId());
        }
        return ids;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCaseOpen(boolean caseOpen) {
        this.caseOpen = caseOpen;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public void setWardrobe(List<Product> wardrobe) {
        this.wardrobe = wardrobe;
    }
}
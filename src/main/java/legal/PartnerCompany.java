package legal;

import java.util.UUID;


public class PartnerCompany {
    private UUID id;
    private String name;

    public PartnerCompany(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() { return id; }

    public String getName() { return name; }
}

package legal;

import java.util.UUID;
import java.time.LocalDate;


public class LicenseAgreement {
    private UUID licenseId;
    private IntellectualProperty ip;
    private PartnerCompany partner;
    private LicenseType type;
    private String region;
    private String productCategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private double royaltyRate;

    public LicenseAgreement(
            IntellectualProperty ip,
            PartnerCompany partner,
            LicenseType type,
            String region,
            String productCategory,
            LocalDate startDate,
            LocalDate endDate,
            double royaltyRate) {

        this.licenseId = UUID.randomUUID();
        this.ip = ip;
        this.partner = partner;
        this.type = type;
        this.region = region;
        this.productCategory = productCategory;
        this.startDate = startDate;
        this.endDate = endDate;
        this.royaltyRate = royaltyRate;
    }

    public UUID getLicenseId() {
        return licenseId;
    }
}

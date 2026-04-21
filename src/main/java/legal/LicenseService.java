package legal;

import java.util.UUID;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LicenseService {

    private List<LicenseAgreement> licenses = new ArrayList<>();

    public LicenseAgreement registerLicense(
            IntellectualProperty ip,
            PartnerCompany partner,
            LicenseType type,
            String region,
            String productCategory,
            LocalDate startDate,
            LocalDate endDate,
            double royaltyRate
    ) {

        if (ip == null || partner == null) {
            throw new IllegalArgumentException("IP and Partner must be provided.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }

        if (royaltyRate < 0) {
            throw new IllegalArgumentException("Royalty rate cannot be negative.");
        }

        if (type == LicenseType.EXCLUSIVE && ip.isExclusivelyLicensed()) {
            throw new IllegalStateException("IP is already exclusively licensed.");
        }

        LicenseAgreement license = new LicenseAgreement(
                ip,
                partner,
                type,
                region,
                productCategory,
                startDate,
                endDate,
                royaltyRate
        );

        if (type == LicenseType.EXCLUSIVE) {
            ip.markAsExclusivelyLicensed();
        }

        licenses.add(license);

        return license;
    }
}

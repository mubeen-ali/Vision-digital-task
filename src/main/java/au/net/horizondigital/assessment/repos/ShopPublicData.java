package au.net.horizondigital.assessment.repos;

import java.sql.Time;

public interface ShopPublicData {
    long getId();
    String getContactNumber();
    String getLocation();
    Time getOpeningTime();
    Time getClosingTime();
}

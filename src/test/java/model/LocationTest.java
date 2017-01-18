package model;

import driverapi.Constants;
import junit.framework.TestCase;

import java.util.List;

/**
 */
public class LocationTest extends TestCase {

    public void testValidation() {
        Location location = new Location(1.234,2.2345);
        assertEquals(0,location.validate().size());

        location = new Location(101.234,2.2345);
        List<String> errors = location.validate();
        assertEquals(1,errors.size());
        assertEquals(Constants.LOCATION_VALIDATION_ERROR_MESSAGE, errors.get(0));
    }

}
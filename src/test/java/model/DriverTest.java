package model;

import driverapi.Constants;
import junit.framework.TestCase;

import java.util.List;

/**
 */
public class DriverTest extends TestCase {

    public void testCreateNewDriver(){
        Driver driver = new Driver("foo", true, new Location(1.123,2.234));
        assertEquals(true, driver.isActive());
        assertEquals(1.123, driver.getLocation().lat);


    }

    public void testValidateDriver(){
        Driver driver = new Driver("1", true, new Location(1.123,2.234));
        List<String> errors = driver.validate();
        assertEquals(0,errors.size());

        driver = new Driver("50001", true, new Location(1.123,2.234));
        errors = driver.validate();
        assertEquals(1,errors.size());
        assertEquals(Constants.DRIVER_VALIDATION_ERROR_MESSAGE,errors.get(0));

        driver = new Driver("101", true, new Location(111.123,2.234));
        errors = driver.validate();
        assertEquals(1,errors.size());
        assertEquals(Constants.LOCATION_VALIDATION_ERROR_MESSAGE,errors.get(0));

        driver = new Driver("5000001", true, new Location(111.123,2.234));
        errors = driver.validate();
        assertEquals(2,errors.size());
        assertEquals(Constants.DRIVER_VALIDATION_ERROR_MESSAGE,errors.get(0));
        assertEquals(Constants.LOCATION_VALIDATION_ERROR_MESSAGE,errors.get(1));

    }
}
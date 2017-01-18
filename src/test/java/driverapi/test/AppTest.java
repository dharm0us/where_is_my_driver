package driverapi.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.Gson;

import driverapi.service.cache.CacheService;
import driverapi.service.DriverLocationService;
import driverapi.service.GeoLocationService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import model.Driver;
import model.DriverDistance;
import model.Location;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    
    public void testConnection() {
        //check whether server is running or not
        String pong = CacheService.ping();
        assertEquals("PONG", pong);
   }
    
    public void testCacheKeyCreation() {
    	String key = CacheService.createBoxKey(new Location(11.23456, 9.45678));
    	String expected = "box-11234-9456";
    	assertEquals(expected, key);
    	
    	 key = CacheService.createBoxKey(new Location(-11.23456, -9.45678));
    	 expected = "box--11234--9456";
    	assertEquals(expected, key);
    	
    	
    	 key = CacheService.createDriverKey(2);
    	 expected = "driver-2";
    	assertEquals(expected, key);

    }
    
    public void testAddDriverToBox() throws InterruptedException {
    	CacheService.addDriverToBox(1, "b1");
    	CacheService.addDriverToBox(1, "b1");
    	CacheService.addDriverToBox(2, "b1");
    	Set<String> actualDrivers = new TreeSet<String>(CacheService.getDriversInABox("b1"));
    	TreeSet<String> expectedDrivers = new TreeSet<String>();
    	expectedDrivers.add("1");
    	expectedDrivers.add("2");

    	assertEquals(expectedDrivers, actualDrivers);
    }
    
    public void testJsonConversion() {
    	List<String> errors = new ArrayList<String>();
		errors.add("test error");
		String jsonString = new Gson().toJson(errors);
		assertEquals("[\"test error\"]", jsonString);
    }
    
    public void testLocationValidation() {
		//lat
    	assertTrue(GeoLocationService.isLocationValid(new Location(90.0d,11d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(89.0d,11d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(-89.0d,11d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(0.0d,11d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(10.0d,11d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(-10.0d,11d)));

		assertFalse(GeoLocationService.isLocationValid(new Location(90.01d,11d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(-90.01d,11d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(90.0001d,11d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(-90.0001d,11d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(100.0001d,11d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(-100.0001d,11d)));

		//lng
		assertTrue(GeoLocationService.isLocationValid(new Location(11d,90.0d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(11d,89.0d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(11d,-89.0d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(11d,0.0d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(11d,10.0d)));
		assertTrue(GeoLocationService.isLocationValid(new Location(11d,-10.0d)));

		assertFalse(GeoLocationService.isLocationValid(new Location(11.0d,90.01d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(11.0d,-90.01d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(11.0d,90.00001d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(11.0d,-90.00001d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(11.0d,100.00001d)));
		assertFalse(GeoLocationService.isLocationValid(new Location(11.0d,-100.00001d)));

    }
    
    public void testGetSetDriverLocation() {
    	Integer driverId = 1;
    	DriverLocationService.handleLocationUpdate(new Driver(driverId.toString(), true, new Location(1.1234, 2.3456)),0.0f);
		Driver driverFromCache= CacheService.getDriverFromCache(driverId);
		Location location = driverFromCache.getLocation();
		System.out.println(location);
    	assertEquals("1.1234", location.lat.toString());
    	assertEquals("2.3456",location.lng.toString());
    }
    
    public void testFindDrivers() {
    	CacheService.clear();
    	Integer driverId = 1;

		DriverLocationService.handleLocationUpdate(new Driver(driverId.toString(), true, new Location(1.1229876d, 2.545d)), 0.7f);
    	
    	Set<String> drivers = DriverLocationService.findDriversFromNearByBoundingBoxes(new Location(1.1234, 2.546), 100);
    	assertTrue(drivers.contains(driverId.toString()));
    	
    	drivers = DriverLocationService.findDriversFromNearByBoundingBoxes(new Location(1.1234, 2.547), 100);
    	assertFalse(drivers.contains(driverId.toString()));

    	drivers = DriverLocationService.findDriversFromNearByBoundingBoxes(new Location(1.1234, 2.547), 200);
    	assertTrue(drivers.contains(driverId.toString()));

    }
    
    public void testDriverSorting() {
    	TreeSet<DriverDistance> set = new TreeSet<DriverDistance>();
    	set.add(new DriverDistance("1", 1.1, new Location(1, 3)));
    	set.add(new DriverDistance("2", 2.1, new Location(4, 5)));
    	set.add(new DriverDistance("3", 0.1, new Location(6, 7)));
    	String expected = "312";
    	String actual = "";
    	for(DriverDistance dd: set) {
    		actual += dd.id;
    	}
    	assertEquals(expected, actual);
    }
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}

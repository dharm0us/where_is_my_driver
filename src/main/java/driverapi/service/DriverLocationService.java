package driverapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import driverapi.service.cache.CacheService;
import model.Driver;
import model.DriverDistance;
import model.Location;

public class DriverLocationService {

	
	public static Map<String,String> handleLocationUpdate(Driver driver, float accuracy ) {
		List<String> errors = driver.validate();
        if(errors.size() > 0) {
            //do error handling
            return new HashMap<String, String>(){{put("404","");}};
        }
		updateDriverLocation(driver);

        return new HashMap<String, String>(){{put("200","");}};
	}

	public static List<DriverDistance> findDrivers(Location queryLocation, int radius, int limit) {
		Set<String> drivers = findDriversFromNearByBoundingBoxes(queryLocation, radius);
		TreeSet<DriverDistance> sortedDriverDistances = new TreeSet<DriverDistance>();
		for(String driverId: drivers) {
			Driver driverFromCache= CacheService.getDriverFromCache(Integer.parseInt(driverId));
            if(driverFromCache == null) continue;
            if(!driverFromCache.isActive()) continue;
            Location driverLocation = driverFromCache.getLocation();
			double dist = GeoLocationService.computeDistance(driverLocation,queryLocation);
			DriverDistance d = new DriverDistance(driverId, dist, driverLocation);
			if(dist <= radius) sortedDriverDistances.add(d);
		}
    	List<DriverDistance> output = new ArrayList<DriverDistance>();
    	for(DriverDistance d : sortedDriverDistances) {
    		--limit;
    		if(limit < 0) break;
    		output.add(d);
    	}
    	return output;
	}
	
	public static Set<String> findDriversFromNearByBoundingBoxes(Location location, int radius) {
		/*
		 * To find drivers in a given radius
		 * Assumption : moving 3rd decimal place by 1 changes distance by 100 meters
		 * So divide radius by 100 to get {steps}
		 * now add .001 to both lat/lng and iterate in both directions for {steps} steps
		 */
		double lat = location.lat;
		double lng = location.lng;
		Set<String> driverSet = new HashSet<String>();
		int steps = radius/100;
		if(steps < 1) steps = 1;
		for(int i=-steps;i<=steps;i++) {
			for(int j=-steps;j<=steps;j++) {
				double latCurr = lat + i*.001;
				double lngCurr = lng + j * .001;
				String boxKey = CacheService.createBoxKey(new Location(latCurr, lngCurr));
                Set<String> drivers = null;
                try {
                    drivers = CacheService.getDriversInABox(boxKey);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                driverSet.addAll(drivers);
			}
		}
		
		return driverSet;	
	}

    private static void updateDriverLocation(Driver driver) {
        int driverIdInt = Integer.parseInt(driver.getDriverId());
        Driver driverFromCache = CacheService.getDriverFromCache(driverIdInt);
        if(driverFromCache != null) {
            String existingBox = CacheService.createBoxKey(driverFromCache.getLocation());
            CacheService.removeDriverFromBox(driverIdInt, existingBox);
        }

        String newBox = CacheService.createBoxKey(driver.getLocation());
        CacheService.addDriverToBox(driverIdInt, newBox);

		String driverKey = CacheService.createDriverKey(driverIdInt);
		CacheService.set(driverKey, driver.toString());

	}
}

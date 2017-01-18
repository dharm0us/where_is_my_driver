package driverapi.service;

import model.Location;

public class GeoLocationService {
	private static final double EARTH_RADIUS = 6372.8; // In kilometers

	public static boolean isLocationValid(Location l) {
		return Math.abs(l.lat) <= 90 && Math.abs(l.lng) <= 90 ;
	}

	static double computeDistance(Location l1, Location l2) {
        double lat1 = l1.lat;
        double lng1 = l1.lng;
        double lat2 = l2.lat;
        double lng2 = l2.lng;
        // haversine formula
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c*1000;
    }


}

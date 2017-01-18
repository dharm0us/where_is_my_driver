package model;

import driverapi.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Driver {
    private final String driverId;
    private final boolean active;
    private final Location location;

    public String getDriverId() {
        return driverId;
    }

    public boolean isActive() {
        return active;
    }

    public Location getLocation() {
        return location;
    }


    public Driver(String driverId, boolean active, Location location) {

        this.driverId = driverId;
        this.active = active;
        this.location = location;
    }

    public List<String> validate() {
         int driverIdInt = Integer.parseInt(driverId);
        List<String> errors = new ArrayList<String>();
        if(driverIdInt < 1 || driverIdInt > 50000) {
            errors.add(Constants.DRIVER_VALIDATION_ERROR_MESSAGE);
        }
        errors.addAll(location.validate());

        /*if(!GeoLocationService.isLocationValid(driverLocation)) {
            final String msg = "Lat/lng should be between +/-90";
            return new HashMap<String, String>(){{put("422",new Gson().toJson(new String[]{msg}));}};
        }*/

        return errors;
        //return new ArrayList<String>();
    }

    @Override
    public String toString() {
        return location.toString()+"@"+active;
    }
}

package model;

import driverapi.Constants;

import java.util.ArrayList;
import java.util.List;

public class Location {
    public Double lat;
    public Double lng;


    public Location(double lat, double lng) {
       this.lat = lat;
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.lat, lat) != 0) return false;
        return Double.compare(location.lng, lng) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public List<String> validate() {
        List<String> response = new ArrayList<String>();
        boolean isValid = Math.abs(lat) <= 90 && Math.abs(lng) <= 90 ;
        if(!isValid) {
            response.add(Constants.LOCATION_VALIDATION_ERROR_MESSAGE);
        }
        return  response;
    }

    @Override
    public String toString() {
        return lat+"@"+lng;
    }
}

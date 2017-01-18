package model;

public class DriverDistance implements Comparable<DriverDistance>{
	public String id;
	private Double distance;
    private Location location;

	public DriverDistance(String driverId, double dist, Location location) {
		this.id = driverId;
		this.distance = dist;
		this.location = location;
	}

	public int compareTo(DriverDistance o) {
		if(this.distance < o.distance) return -1;
		if(this.distance > o.distance) return 1;
		return 1;
}
	
	@Override
	public String toString() {
		return id+" "+distance.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DriverDistance driverDistance = (DriverDistance) o;

		if (id != null ? !id.equals(driverDistance.id) : driverDistance.id != null) return false;
		if (distance != null ? !distance.equals(driverDistance.distance) : driverDistance.distance != null) return false;
		return location != null ? location.equals(driverDistance.location) : driverDistance.location == null;

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (distance != null ? distance.hashCode() : 0);
		result = 31 * result + (location != null ? location.hashCode() : 0);
		return result;
	}
}

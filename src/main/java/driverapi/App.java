package driverapi;

import driverapi.service.DriverLocationService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	new FrontController(new DriverLocationService());
    }
}

package driverapi;

import static spark.Spark.*;

import java.util.List;
import java.util.Map;

import model.Driver;
import model.Location;
import org.eclipse.jetty.util.StringUtil;

import com.google.gson.Gson;

import driverapi.service.DriverLocationService;
import model.DriverDistance;
import spark.Request;
import spark.Response;
import spark.Route;

class FrontController {
    FrontController(final DriverLocationService driverLocationService) {
        put("/drivers/*/location", new Route() {

            public Object handle(Request request, Response response) {
                // process request
                    String driverId = request.splat()[0];
                    Double lat = Double.parseDouble(request.queryParams("latitude"));
                    Double lng = Double.parseDouble(request.queryParams("longitude"));
                    Boolean isActive = Boolean.parseBoolean(request.queryParams("isActive"));

                    Float accuracy = 0.7f;
                    String accu = request.queryParams("accuracy");
                    if (StringUtil.isNotBlank(accu)) {
                        accuracy = Float.parseFloat(accu);
                    }

                    Map<String, String> resp = DriverLocationService.handleLocationUpdate(new Driver(driverId, isActive, new Location(lat, lng)), accuracy
                    );
                    Map.Entry<String, String> entry = resp.entrySet().iterator().next();
                    String code = entry.getKey();
                    String body = entry.getValue();
                    response.status(Integer.parseInt(code));
                    return body;
            }


        });

        get("/drivers", new Route() {

            public Object handle(Request request, Response response) {
                // process request
                Double lat = Double.parseDouble(request.queryParams("latitude"));
                Double lng = Double.parseDouble(request.queryParams("longitude"));
                int radius = 500;
                String radiusParam = request.queryParams("radius");
                if (StringUtil.isNotBlank(radiusParam)) {
                    radius = Integer.parseInt(radiusParam);
                }

                int limit = 10;
                String limitParam = request.queryParams("limitParam");
                if (StringUtil.isNotBlank(limitParam)) {
                    limit = Integer.parseInt(limitParam);
                }

                List<DriverDistance> driverDistances = DriverLocationService.findDrivers(new Location(lat, lng), radius, limit);
                return new Gson().toJson(driverDistances);
            }


        });
    }
}
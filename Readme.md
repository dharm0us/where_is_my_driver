
How to run:

1. Start Redis
2. Through redis-cli set config set stop-writes-on-bgsave-error no
3. Open the project in eclipse. Navigate to App.java. Run as Java Application
4. test on localhost:457/driverDistances

PUT http://localhost:4567/drivers/5/location?latitude=1.237567&longitude=2.34567&isActive=false
PUT http://localhost:4567/drivers/5/location?latitude=1.237567&longitude=2.34567&isActive=true


GET http://localhost:4567/drivers?latitude=1.238567&longitude=2.34767

Running Tests:
Navigate to AppTest.java. Run as Junit Test in Eclipse.

Choice of stack:
Java + Redis + Spark(for rest)

Why Spark?
For REST, I wanted a simple solution. Not worrying about scale right now.

Why Redis(and not Memcache)?
Redis provides disk persistence + in memory data structures(I have used Set here)
I haven't used disk persistence here but it will be useful when we restart redis to load data in memory.

Requirements to run this:
Redis + JRE + Maven

Solution explained:
1. Divide the world map into virtual boxes of lat/lng.
Sensitive to 3rd decimal. Since moving third decimal by 1 moves 100 meters in distance.
So multiplying lat/lng with 1000 and converting to int gives us the boxId (lat+"-"+lng)

For updateDriver:
1. Add him to the corresponding box in Redis
2. Add an entry for him in driverDistance cache (driverId => (lat,lng))

For finding driverDistances:
1. For incoming lat,lng, radius find number of bounding boxes required to be checked by dividing radius by 100.
2. Find driverDistances in nearby boxes computed in 1.
3. Sort these driverDistances by distance and return {limit} driverDistances.

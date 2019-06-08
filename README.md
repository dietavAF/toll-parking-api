# Toll Parking API

The Toll Parking Management API is a Java Library API based on Spring Boot, Java 8, Maven 3, Springfox (for the Swagger API Documentation) and Mockito for Unit and Integration tests.

## Description

It should be possible to manage a toll parking with different parking slots for different type of cars:
* standard parking slots for sedan cars
* parking slots for electric cars (at 20kW power)
* parking slots for electric cars (at 50kW power)

The parking has got a pricing policy:
* a bill for every car which leaves the parking according only to the hours spent in parking (hourly)
* a bill for every car which leaves the parking according to a fixed price AND the hours spent in parking (fixedHourly)

This API should manage coming cars to park them (at the first free slot) or refuse them if the right parking is full and should manage the invoice for the leaving cars.

## How to Run

This application is packaged as a jar which has a Tomcat 8 server embedded. No Tomcat or JBoss installation is necessary.
First, you have to clone the repository.
Then, you can build the project, run the tests and run the application with: 

```
        1) mvn clean package
        2) java -jar -Dspring.profiles.active=hourly target/ParkingToll-1.0.0.jar
              or
           java -jar -Dspring.profiles.active=fixedHourly target/ParkingToll-1.0.0.jar
```

or you can build the project, run the tests and run the application using Maven:

```
        mvn clean package -Dspring.profiles.active=hourly spring-boot:run
           or
        mvn clean package -Dspring.profiles.active=fixedHourly spring-boot:run
```

The ```spring.profiles.active``` parameter can assume two values: hourly and fixedHourly (check the paragraph "Description"). 
They correspond to the Pricing Policies which can be adopted by the parking

Once the application runs you should see something like this

```
2019-06-09 01:29:37.576  INFO 45976 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-06-09 01:29:37.581  INFO 45976 --- [           main] com.api.library.ParkingTollApplication   : Started ParkingTollApplication in 5.056 seconds (JVM running for 13.273)
```

The Tomcat started port is important, as defines the port used by the application (in this case 8080).

## API Documentation

It is possible to have an interactive API Documentation by navigating your browser to [YOUR-URL]/swagger-ui.html, once you start the application. 
If we say that the port used by the application is 8080 (check the paragraph "How to Run"), you can try http://localhost:8080/swagger-ui.html 



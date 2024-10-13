package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.logging.Logger;

public class CosmosDBTestFunction {

    @FunctionName("CosmosDBTestFunction")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        Logger logger = context.getLogger();
        CosmosDBConnectionManager connectionManager = new CosmosDBConnectionManager();

        // Test flight data insertion
        FlightDataInserter flightInserter = new FlightDataInserter(connectionManager);
        String flightDataJson = "{ \"flight\": \"BA2490\", \"status\": \"On Time\", \"departure\": \"London Heathrow\", \"arrival\": \"New York JFK\", \"departureTime\": \"10:00 AM\", \"arrivalTime\": \"1:30 PM\" }";
        flightInserter.insertFlightData(flightDataJson);
        logger.info("Inserted flight data.");

        // Test weather data insertion
        WeatherDataInserter weatherInserter = new WeatherDataInserter(connectionManager);
        String weatherDataJson = "{ \"location\": \"London\", \"temperature\": 20, \"humidity\": 65, \"wind_speed\": 5, \"wind_direction\": \"NW\", \"pressure\": 1015, \"precipitation\": 2, \"visibility\": 10, \"status\": \"Sunny\" }";
        weatherInserter.insertWeatherData(weatherDataJson);
        logger.info("Inserted weather data.");

        // Test person data insertion
        PersonDataInserter personInserter = new PersonDataInserter(connectionManager);
        String personDataJson = "{ \"name\": \"John Doe\", \"age\": 30, \"occupation\": \"Engineer\" }";
        personInserter.insertPersonData(personDataJson);
        logger.info("Inserted person data.");

        connectionManager.close();

        return request.createResponseBuilder(HttpStatus.OK)
                      .body("Data inserted into Cosmos DB successfully.")
                      .build();
    }
}

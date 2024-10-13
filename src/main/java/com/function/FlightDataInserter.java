package com.function;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;

public class FlightDataInserter {
    private CosmosContainer container;

    public FlightDataInserter(CosmosDBConnectionManager connectionManager) {
        CosmosDatabase database = connectionManager.getDatabase();
        this.container = database.getContainer("FlightContainer");
    }

    public void insertFlightData(String flightDataJson) {
        CosmosItemResponse<Object> response = container.createItem(flightDataJson);
        System.out.println("Inserted flight data with response: " + response.getStatusCode());
    }
}

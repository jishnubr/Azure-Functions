package com.function;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;

public class WeatherDataInserter {
    private CosmosContainer container;

    public WeatherDataInserter(CosmosDBConnectionManager connectionManager) {
        CosmosDatabase database = connectionManager.getDatabase();
        this.container = database.getContainer("WeatherContainer");
    }

    public void insertWeatherData(String weatherDataJson) {
        CosmosItemResponse<Object> response = container.createItem(weatherDataJson);
        System.out.println("Inserted weather data with response: " + response.getStatusCode());
    }
}

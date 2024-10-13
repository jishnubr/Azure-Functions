package com.function;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;

public class PersonDataInserter {
    private CosmosContainer container;

    public PersonDataInserter(CosmosDBConnectionManager connectionManager) {
        CosmosDatabase database = connectionManager.getDatabase();
        this.container = database.getContainer("PersonContainer");
    }

    public void insertPersonData(String personDataJson) {
        CosmosItemResponse<Object> response = container.createItem(personDataJson);
        System.out.println("Inserted person data with response: " + response.getStatusCode());
    }
}

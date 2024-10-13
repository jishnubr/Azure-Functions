package com.function;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

public class CosmosDBConnectionManager {
    private static final String COSMOS_DB_ENDPOINT = "https://flightweatherpersondb.documents.azure.com:443/";
    private static final String COSMOS_DB_KEY = System.getenv("COSMOS_DB_PRIMARY_KEY");
    private static final String DATABASE_NAME = "FlightWeatherPersonDB";

    private CosmosClient client;
    private CosmosDatabase database;

    public CosmosDBConnectionManager() {
        this.client = new CosmosClientBuilder()
            .endpoint(COSMOS_DB_ENDPOINT)
            .key(COSMOS_DB_KEY)
            .consistencyLevel(ConsistencyLevel.EVENTUAL)
            .buildClient();
        this.database = client.getDatabase(DATABASE_NAME);
    }

    public CosmosClient getClient() {
        return client;
    }

    public CosmosDatabase getDatabase() {
        return database;
    }

    public void close() {
        client.close();
    }
}

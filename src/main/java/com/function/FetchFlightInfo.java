package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

public class FetchFlightInfo {
    @FunctionName("GetFlightInfo")
    public HttpResponseMessage get(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        String flightData = "{"
                         + "\"flight\": \"BA2490\","
                         + "\"status\": \"On Time\","
                         + "\"departure\": \"London Heathrow\","
                         + "\"arrival\": \"New York JFK\","
                         + "\"departureTime\": \"10:00 AM\","
                         + "\"arrivalTime\": \"1:30 PM\""
                         + "}";
        context.getLogger().info("Returning mock flight data: " + flightData);

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "application/json")
                      .body(flightData)
                      .build();
    }
}

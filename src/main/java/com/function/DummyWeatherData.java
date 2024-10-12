package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;
public class DummyWeatherData {
    @FunctionName("GetDummyWeatherData")
    public HttpResponseMessage get(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {
        
        String dummyData = "{\"location\": \"London\", \"temperature\": 20, \"status\": \"Sunny\"}";
        context.getLogger().info("Returning dummy weather data: " + dummyData);
        
        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "application/json")
                      .body(dummyData)
                      .build();
    }
}

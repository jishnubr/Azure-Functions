package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

public class GenerateHtmlTable {
    @FunctionName("GenerateHtmlTable")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        // Dummy data for the example
        String weatherData = "{\"location\": \"London\", \"temperature\": 20, \"humidity\": 65, \"wind_speed\": 5, \"wind_direction\": \"NW\", \"pressure\": 1015, \"precipitation\": 0, \"visibility\": 10, \"status\": \"Sunny\"}";
        
        String htmlContent = "<!DOCTYPE html><html><head><title>Weather Data</title>" +
                             "<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/5.0.0/css/bootstrap.min.css'>" +
                             "</head><body><div class='container'><h2>Weather Data</h2>" +
                             "<table class='table table-bordered'><thead><tr><th>Location</th><th>Temperature</th><th>Humidity</th><th>Wind Speed</th><th>Wind Direction</th><th>Pressure</th><th>Precipitation</th><th>Visibility</th><th>Status</th></tr></thead><tbody>";

        // Parse the dummy data and append rows to the table (in a real scenario, parse JSON)
        htmlContent += "<tr><td>London</td><td>20</td><td>65</td><td>5</td><td>NW</td><td>1015</td><td>0</td><td>10</td><td>Sunny</td></tr>";
        
        htmlContent += "</tbody></table></div></body></html>";

        context.getLogger().info("Generated HTML table for weather data.");

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "text/html")
                      .body(htmlContent)
                      .build();
    }
}

package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

public class GenerateHtmlTable {
    @FunctionName("GenerateHtmlTable")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        String weatherData = request.getBody().orElse("{}");

        String htmlContent = "<!DOCTYPE html><html><head><title>Weather Data</title>" +
                             "<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/5.0.0/css/bootstrap.min.css'>" +
                             "</head><body><div class='container'><h2>Weather Data</h2>" +
                             "<table class='table table-bordered'><thead><tr><th>Location</th><th>Temperature</th><th>Humidity</th><th>Wind Speed</th><th>Wind Direction</th><th>Pressure</th><th>Precipitation</th><th>Visibility</th><th>Status</th></tr></thead><tbody>";

        // Parse the received data and append rows to the table (here we assume the data is in a simple format for demonstration)
        htmlContent += "<tr><td>London</td><td>20</td><td>65</td><td>5</td><td>NW</td><td>1015</td><td>0</td><td>10</td><td>Sunny</td></tr>";
        
        htmlContent += "</tbody></table></div></body></html>";

        context.getLogger().info("Generated HTML table for weather data.");

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "text/html")
                      .body(htmlContent)
                      .build();
    }
}

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

        String htmlTemplate = "<!DOCTYPE html><html><head><title>Weather Data</title>" +
                              "<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/5.0.0/css/bootstrap.min.css'>" +
                              "<script src='https://cdn.jsdelivr.net/npm/vue@2'></script>" +
                              "</head><body><div id='app' class='container'><h2>Weather Data</h2>" +
                              "<table class='table table-bordered'><thead><tr><th>Location</th><th>Temperature</th><th>Humidity</th><th>Wind Speed</th><th>Wind Direction</th><th>Pressure</th><th>Precipitation</th><th>Visibility</th><th>Status</th></tr></thead><tbody>" +
                              "<tr v-for='data in weatherData'><td>{{ data.location }}</td><td>{{ data.temperature }}</td><td>{{ data.humidity }}</td><td>{{ data.wind_speed }}</td><td>{{ data.wind_direction }}</td><td>{{ data.pressure }}</td><td>{{ data.precipitation }}</td><td>{{ data.visibility }}</td><td>{{ data.status }}</td></tr>" +
                              "</tbody></table></div><script>" +
                              "new Vue({el: '#app',data: {weatherData: " + weatherData + "}});" +
                              "</script></body></html>";

        context.getLogger().info("Generated HTML table with Vue.js for weather data.");

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "text/html")
                      .body(htmlTemplate)
                      .build();
    }
}

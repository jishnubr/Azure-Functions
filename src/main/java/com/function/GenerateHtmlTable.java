package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Optional;
import java.util.Random;

public class GenerateHtmlTable {

    @FunctionName("GenerateHtmlTable")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {
        
        // Step 1: Retrieve the dataset from the request
        String jsonData = request.getBody().orElse("[]");
        context.getLogger().info("Received data: " + jsonData);
        
        try {
            // Step 2: Parse the dataset
            JSONArray dataArray = new JSONArray(jsonData);
            context.getLogger().info("Parsed JSON array: " + dataArray.toString());

            // Step 3: Set up random colors for text and border
            Random rand = new Random();
            String textColor = String.format("#%06x", rand.nextInt(0xffffff + 1));
            String borderColor = String.format("#%06x", rand.nextInt(0xffffff + 1));

            // Step 4: Initialize table header and row builders
            StringBuilder flightTableHeaders = new StringBuilder();
            StringBuilder flightTableRows = new StringBuilder();
            StringBuilder weatherTableHeaders = new StringBuilder();
            StringBuilder weatherTableRows = new StringBuilder();

            boolean hasFlightData = false;
            boolean hasWeatherData = false;

            // Step 5: Iterate through the dataset
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject obj = dataArray.getJSONObject(i);

                if (obj.has("flight")) {
                    // Step 6: Generate flight table headers if not done
                    if (!hasFlightData) {
                        flightTableHeaders.append("<th>Departure Time</th><th>Flight</th><th>Arrival</th><th>Arrival Time</th><th>Departure</th><th>Status</th>");
                        hasFlightData = true;
                    }
                    // Step 7: Generate flight table rows
                    flightTableRows.append("<tr>");
                    flightTableRows.append("<td>").append(obj.optString("departureTime", "N/A")).append("</td>");
                    flightTableRows.append("<td>").append(obj.optString("flight", "N/A")).append("</td>");
                    flightTableRows.append("<td>").append(obj.optString("arrival", "N/A")).append("</td>");
                    flightTableRows.append("<td>").append(obj.optString("arrivalTime", "N/A")).append("</td>");
                    flightTableRows.append("<td>").append(obj.optString("departure", "N/A")).append("</td>");
                    flightTableRows.append("<td>").append(obj.optString("status", "N/A")).append("</td>");
                    flightTableRows.append("</tr>");
                } else {
                    // Step 8: Generate weather table headers if not done
                    if (!hasWeatherData) {
                        for (String key : obj.keySet()) {
                            weatherTableHeaders.append("<th>").append(capitalize(key)).append("</th>");
                        }
                        hasWeatherData = true;
                    }
                    // Step 9: Generate weather table rows
                    weatherTableRows.append("<tr>");
                    for (String key : obj.keySet()) {
                        Object value = obj.opt(key);
                        if (value == null) {
                            weatherTableRows.append("<td>").append("N/A").append("</td>");
                        } else if (value instanceof String) {
                            weatherTableRows.append("<td>").append((String) value).append("</td>");
                        } else if (value instanceof Number) {
                            weatherTableRows.append("<td>").append(((Number) value).toString()).append("</td>");
                        } else {
                            weatherTableRows.append("<td>").append(String.valueOf(value)).append("</td>");
                        }
                    }
                    weatherTableRows.append("</tr>");
                }
            }

            // Step 10: Create the HTML template
            String htmlTemplate = "<!DOCTYPE html><html><head><title>Data Information</title>" +
                                  "<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/5.0.0/css/bootstrap.min.css'>" +
                                  "<script src='https://cdn.jsdelivr.net/npm/vue@2'></script>" +
                                  "<style>" +
                                  "body { background-color: #f8f9fa; }" +
                                  "h2 { color: #17a2b8; }" +
                                  "table { width: 100%; border: 2px solid " + borderColor + "; }" +
                                  "th { background-color: #343a40; color: #ffffff; }" +
                                  "tr:nth-child(even) { background-color: #e9ecef; }" +
                                  "tr:nth-child(odd) { background-color: #f8f9fa; }" +
                                  "td { color: " + textColor + "; }" +
                                  "</style>" +
                                  "</head><body><div id='app' class='container'>" +
                                  "<h2>Flight Information</h2>" +
                                  "<table class='table table-striped'><thead><tr>" + flightTableHeaders.toString() + "</tr></thead><tbody>" +
                                  flightTableRows.toString() + "</tbody></table>" +
                                  "<h2>Weather Information</h2>" +
                                  "<table class='table table-striped'><thead><tr>" + weatherTableHeaders.toString() + "</tr></thead><tbody>" +
                                  weatherTableRows.toString() + "</tbody></table></div><script>" +
                                  "new Vue({el: '#app', data: {dataArray: " + jsonData + "}});" +
                                  "</script></body></html>";

            context.getLogger().info("Generated HTML tables with Vue.js for dynamic data, with dynamic colors.");

            return request.createResponseBuilder(HttpStatus.OK)
                          .header("Content-Type", "text/html")
                          .body(htmlTemplate)
                          .build();

        } catch (Exception e) {
            context.getLogger().severe("Error generating HTML tables: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                          .body("Failed to generate HTML tables")
                          .build();
        }
    }

    // Utility method to capitalize strings
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

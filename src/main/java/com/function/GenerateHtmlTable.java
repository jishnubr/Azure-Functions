package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.google.gson.JsonArray;
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

        String jsonData = request.getBody().orElse("[]");  // Default to empty array if no data
        JSONArray dataArray = new JSONArray(jsonData);

        // Random color generation
        Random rand = new Random();
        String textColor = String.format("#%06x", rand.nextInt(0xffffff + 1));
        String borderColor = String.format("#%06x", rand.nextInt(0xffffff + 1));

        // Detect data type and generate appropriate table
        StringBuilder tableHeaders = new StringBuilder();
        StringBuilder tableRows = new StringBuilder();
        if (dataArray.length() > 0) {
            JSONObject firstObj = dataArray.getJSONObject(0);
            for (String key : firstObj.keySet()) {
                tableHeaders.append("<th>").append(capitalize(key)).append("</th>");
            }
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject obj = dataArray.getJSONObject(i);
                tableRows.append("<tr>");
                for (String key : firstObj.keySet()) {
                    tableRows.append("<td>").append(obj.getString(key)).append("</td>");
                }
                tableRows.append("</tr>");
            }
        }

        String htmlTemplate = "<!DOCTYPE html><html><head><title>Data Information</title>" +
                              "<link rel='stylesheet' href='https://stackpath.bootstrapcdn.com/bootstrap/5.0.0/css/bootstrap.min.css'>" +
                              "<script src='https://cdn.jsdelivr.net/npm/vue@2'></script>" +
                              "<style>" +
                              "body { background-color: #f8f9fa; }" +
                              "h2 { color: #17a2b8; }" +
                              "table { width: 100%; border: 2px solid " + borderColor + "; }" +
                              "th { background-color: #343a40; color: #ffffff; }" + // Changed to white for better contrast
                              "tr:nth-child(even) { background-color: #e9ecef; }" +
                              "tr:nth-child(odd) { background-color: #f8f9fa; }" +
                              "td { color: " + textColor + "; }" +
                              "</style>" +
                              "</head><body><div id='app' class='container'><h2>Data Information</h2>" +
                              "<table class='table table-striped'><thead><tr>" + tableHeaders.toString() + "</tr></thead><tbody>" +
                              tableRows.toString() + "</tbody></table></div><script>" +
                              "new Vue({el: '#app', data: {dataArray: " + jsonData + "}});" +
                              "</script></body></html>";

        context.getLogger().info("Generated HTML table with Vue.js for dynamic data, with dynamic colors.");

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "text/html")
                      .body(htmlTemplate)
                      .build();
    }

    private String capitalize(String str) {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

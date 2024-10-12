package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import org.json.JSONObject;

public class FetchFlightInfo {
    @FunctionName("FetchFlightInfo")
    public HttpResponseMessage get(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        try {
            String apiUrl = "https://mockflightinfo.com/api/flightinfo";  // Replace with actual API URL
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

            // Convert response to JSON object
            JSONObject flightInfo = new JSONObject(response.toString());
            context.getLogger().info("Fetched flight data: " + flightInfo.toString());

            return request.createResponseBuilder(HttpStatus.OK)
                          .header("Content-Type", "application/json")
                          .body(flightInfo.toString())
                          .build();

        } catch (Exception e) {
            context.getLogger().severe("Error fetching flight data: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                          .body("Error fetching flight data")
                          .build();
        }
    }
}

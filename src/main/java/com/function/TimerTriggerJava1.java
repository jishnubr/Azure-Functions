package com.function;

import java.time.*;
import java.net.*;
import java.io.*;
import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class TimerTriggerJava1 {
    @FunctionName("TimerTriggerJava1")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */10 * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Timer trigger function started at: " + LocalDateTime.now());

        String[] apiUrls = {
            "https://coupaapicall.azurewebsites.net/api/FetchFlightInfo",  // Flight info URL
            "https://coupaapicall.azurewebsites.net/api/GetDummyWeatherData"  // Weather data URL
        };
        String htmlApiUrl = "https://coupaapicall.azurewebsites.net/api/GenerateHtmlTable";  // URL for HTML table function
        String postHtmlUrl = "https://coupaapicall.azurewebsites.net/api/PostHtmlContent";  // URL to post HTML content
        JSONArray combinedData = new JSONArray();

        for (String apiUrl : apiUrls) {
            try {
                // Fetch data from API
                context.getLogger().info("Fetching data from: " + apiUrl);
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
                urlConnect.setConnectTimeout(10000); // Set timeout
                urlConnect.setRequestMethod("GET");
                urlConnect.connect();
                int responseCode = urlConnect.getResponseCode();
                context.getLogger().info("Response code from " + apiUrl + ": " + responseCode);
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    context.getLogger().info("Fetched data: " + content.toString());
                    // Ensure the data is in JSON array format
                    if (apiUrl.contains("GetDummyWeatherData")) {
                        combinedData.put(new JSONObject(content.toString()));
                    } else {
                        JSONArray flightDataArray = new JSONArray(content.toString());
                        for (int i = 0; i < flightDataArray.length(); i++) {
                            combinedData.put(flightDataArray.getJSONObject(i));
                        }
                    }
                } else {
                    context.getLogger().info("Failed to fetch data. Response code: " + responseCode);
                }
            } catch (Exception e) {
                context.getLogger().severe("Error processing data: " + e.getMessage());
            }
        }
        
        // Log the combined data array for debugging
        context.getLogger().info("Combined data array: " + combinedData.toString());
        
        // Send combined data to HTML table function
        try {
            URL htmlUrl = new URL(htmlApiUrl);
            context.getLogger().info("Sending combined data to HTML table function: " + htmlUrl);
            HttpURLConnection htmlConnect = (HttpURLConnection) htmlUrl.openConnection();
            htmlConnect.setConnectTimeout(10000); // Set timeout
            htmlConnect.setRequestMethod("POST");
            htmlConnect.setRequestProperty("Content-Type", "application/json");
            htmlConnect.setDoOutput(true);
            try (OutputStream os = htmlConnect.getOutputStream()) {
                byte[] input = combinedData.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int htmlResponseCode = htmlConnect.getResponseCode();
            context.getLogger().info("Response code from HTML table function: " + htmlResponseCode);
            if (htmlResponseCode == 200) {
                BufferedReader htmlIn = new BufferedReader(new InputStreamReader(htmlConnect.getInputStream()));
                StringBuilder htmlContent = new StringBuilder();
                String htmlLine;
                while ((htmlLine = htmlIn.readLine()) != null) {
                    htmlContent.append(htmlLine);
                }
                htmlIn.close();
                context.getLogger().info("Generated HTML table: " + htmlContent.toString());
                
                // Post HTML content to HostHtmlContent function
                URL postHtml = new URL(postHtmlUrl);
                context.getLogger().info("Posting HTML content to: " + postHtmlUrl);
                HttpURLConnection postConnect = (HttpURLConnection) postHtml.openConnection();
                postConnect.setConnectTimeout(10000); // Set timeout
                postConnect.setRequestMethod("POST");
                postConnect.setRequestProperty("Content-Type", "text/html");
                postConnect.setDoOutput(true);
                try (OutputStream os = postConnect.getOutputStream()) {
                    byte[] input = htmlContent.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                int postResponseCode = postConnect.getResponseCode();
                context.getLogger().info("Response code from posting HTML content: " + postResponseCode);
                if (postResponseCode == 200) {
                    context.getLogger().info("HTML content posted successfully.");
                } else {
                    context.getLogger().info("Failed to post HTML content. Response code: " + postResponseCode);
                }
            } else {
                context.getLogger().info("Failed to generate HTML table. Response code: " + htmlResponseCode);
            }
        } catch (Exception e) {
            context.getLogger().severe("Error generating or posting HTML table: " + e.getMessage());
        }
        context.getLogger().info("Java Timer trigger function completed at: " + LocalDateTime.now());
    }
}

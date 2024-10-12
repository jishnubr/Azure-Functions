package com.function;

import java.time.*;
import java.net.*;
import java.io.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class TimerTriggerJava1 {
    @FunctionName("TimerTriggerJava1")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        String[] apiUrls = {
            "https://coupaapicall.azurewebsites.net/api/FetchFlightInfo",  // Flight info URL
            "https://coupaapicall.azurewebsites.net/api/GetDummyWeatherData"  // Weather data URL
        };
        String htmlApiUrl = "https://coupaapicall.azurewebsites.net/api/GenerateHtmlTable";  // URL for HTML table function
        String postHtmlUrl = "https://coupaapicall.azurewebsites.net/api/PostHtmlContent";  // URL to post HTML content

        for (String apiUrl : apiUrls) {
            try {
                // Fetch data from API
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
                urlConnect.setConnectTimeout(10000); // Set timeout
                urlConnect.setRequestMethod("GET");
                urlConnect.connect();

                int responseCode = urlConnect.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnect.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    context.getLogger().info("Fetched data: " + content.toString());

                    // Send data to HTML table function
                    String data = content.toString();
                    URL htmlUrl = new URL(htmlApiUrl);
                    HttpURLConnection htmlConnect = (HttpURLConnection) htmlUrl.openConnection();
                    htmlConnect.setConnectTimeout(10000); // Set timeout
                    htmlConnect.setRequestMethod("POST");
                    htmlConnect.setRequestProperty("Content-Type", "application/json");
                    htmlConnect.setDoOutput(true);

                    try (OutputStream os = htmlConnect.getOutputStream()) {
                        byte[] input = data.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int htmlResponseCode = htmlConnect.getResponseCode();
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
                        if (postResponseCode == 200) {
                            context.getLogger().info("HTML content posted successfully.");
                        } else {
                            context.getLogger().info("Failed to post HTML content. Response code: " + postResponseCode);
                        }
                    } else {
                        context.getLogger().info("Failed to generate HTML table. Response code: " + htmlResponseCode);
                    }
                } else {
                    context.getLogger().info("Failed to fetch data. Response code: " + responseCode);
                }
            } catch (Exception e) {
                context.getLogger().severe("Error processing data: " + e.getMessage());
            }
        }
    }
}

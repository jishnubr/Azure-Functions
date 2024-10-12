package com.function;

import java.time.*;
import java.net.*;
import java.io.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

public class TimerTriggerJava1 {
    @FunctionName("TimerTriggerJava1")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */10 * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        String apiUrl = "https://coupaapicall.azurewebsites.net/api/GetDummyWeatherData";  // Your function app URL with endpoint
        String htmlApiUrl = "https://coupaapicall.azurewebsites.net/api/GenerateHtmlTable";  // URL for HTML table function

        try {
            // Fetch weather data
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
                context.getLogger().info("Dummy weather data: " + content.toString());

                // Send data to HTML table function
                String weatherData = content.toString();
                URL htmlUrl = new URL(htmlApiUrl);
                HttpURLConnection htmlConnect = (HttpURLConnection) htmlUrl.openConnection();
                htmlConnect.setConnectTimeout(10000); // Set timeout
                htmlConnect.setRequestMethod("POST");
                htmlConnect.setRequestProperty("Content-Type", "application/json");
                htmlConnect.setDoOutput(true);

                try (OutputStream os = htmlConnect.getOutputStream()) {
                    byte[] input = weatherData.getBytes("utf-8");
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
                } else {
                    context.getLogger().info("Failed to generate HTML table. Response code: " + htmlResponseCode);
                }
            } else {
                context.getLogger().info("Failed to fetch dummy weather data. Response code: " + responseCode);
            }
        } catch (Exception e) {
            context.getLogger().severe("Error processing weather data: " + e.getMessage());
        }
    }
}

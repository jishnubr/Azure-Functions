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

        String apiUrl = "https://<YOUR_FUNCTION_APP_URL>/api/GetDummyWeatherData";  // Replace with your actual function app URL

        try {
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
            } else {
                context.getLogger().info("Failed to fetch dummy weather data. Response code: " + responseCode);
            }
        } catch (Exception e) {
            context.getLogger().severe("Error checking dummy weather API: " + e.getMessage());
        }
    }
}

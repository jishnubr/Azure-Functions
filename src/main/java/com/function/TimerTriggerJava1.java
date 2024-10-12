package com.function;

import java.time.*;
import java.net.*;
import java.io.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer trigger.
 */
public class TimerTriggerJava1 {
    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("TimerTriggerJava1")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */1 * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setConnectTimeout(10000); // Set timeout
            urlConnect.connect();

            if (urlConnect.getResponseCode() == 200) {
                context.getLogger().info("Internet is available.");
            } else {
                context.getLogger().info("Failed to connect to the internet.");
            }
        } catch (Exception e) {
            context.getLogger().severe("Error checking internet connectivity: " + e.getMessage());
        }
    }
}

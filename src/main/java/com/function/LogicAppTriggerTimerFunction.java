package com.function;


import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class LogicAppTriggerTimerFunction {

    @FunctionName("LogicAppTriggerTimerFunction")
    public void run(
        @TimerTrigger(name = "timerInfo", schedule = "0 */5 * * * *") String timerInfo, // Triggers every 5 minutes
        final ExecutionContext context) {

        Logger logger = context.getLogger();
        HttpClient client = HttpClient.newHttpClient();

        // URL of the Logic App
        String logicAppUrl = "https://prod-07.northcentralus.logic.azure.com:443/workflows/c70af8b2f53d467b994cac283b37369b/triggers/When_a_HTTP_request_is_received/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2FWhen_a_HTTP_request_is_received%2Frun&sv=1.0&sig=TcU0p0s24cQfQ5Lf_3vmISR8plkefejIBV4ayjfeec0";

        // Data to be sent in the request body
        String jsonData = "{ \"id\": \"12345\" }";

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(logicAppUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            logger.info("Response status code: " + response.statusCode());
            logger.info("Response body: " + response.body());
        } catch (Exception e) {
            logger.severe("HTTP request to Logic App failed: " + e.getMessage());
        }

        logger.info("Timer function executed at: " + timerInfo);
    }
}

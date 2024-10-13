package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

public class HostHtmlContent {
    private static String htmlContent = "";

    @FunctionName("PostHtmlContent")
    public HttpResponseMessage post(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        htmlContent = request.getBody().orElse("");
        context.getLogger().info("Received HTML content: " + htmlContent);

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "text/plain")
                      .body("HTML content stored successfully")
                      .build();
    }

    @FunctionName("GetHtmlContent")
    public HttpResponseMessage get(
        @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) {

        context.getLogger().info("Returning stored HTML content.");

        return request.createResponseBuilder(HttpStatus.OK)
                      .header("Content-Type", "text/html")
                      .body(htmlContent)
                      .build();
    }
}

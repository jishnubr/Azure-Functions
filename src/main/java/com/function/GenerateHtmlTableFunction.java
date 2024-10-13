package com.function;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.apache.commons.lang3.StringEscapeUtils;
import java.util.logging.Level;

public class GenerateHtmlTableFunction {
    @FunctionName("GenerateHtmlTableFunction")
    public String run(
        @ServiceBusQueueTrigger(name = "message", queueName = "myqueue", connection = "ServiceBusConnectionString") String message,
        final ExecutionContext context
    ) {
        String htmlTable = "";
        try {
            // Parse XML/JSON data
            DataModel data = DataTransformer.transform(message);

            // Generate HTML table
            htmlTable = HtmlTableGenerator.generate(data);
        } catch (Exception e) {
            context.getLogger().log(Level.SEVERE, "Error processing message", e);
        }
        return htmlTable;
    }
}

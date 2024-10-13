package com.function;

import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import com.microsoft.azure.functions.annotation.TimerTrigger;
import com.microsoft.azure.functions.*;

public class FunctionConfig {
    @FunctionName("FunctionConfig")
    @StorageAccount("AzureWebJobsStorage")
    public void functionConfig(
        @TimerTrigger(name = "timerInfo", schedule = "0 */5 * * * *") String timerInfo,
        ExecutionContext context
    ) {
        context.getLogger().info("Function app config executed at: " + timerInfo);
    }
}

package com.example.strprojects.utils;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static final String TAG = "Utils";

    public static void shutdownAndAwaitTermination(ExecutorService pool, int timeout) {
        Log.d(TAG, "shutdownAndAwaitTermination: Shutdown init");
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(timeout, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(timeout, TimeUnit.SECONDS))
                    Log.d(TAG, "shutdownAndAwaitTermination: Pool did not terminate");
            }
            Log.d(TAG, "shutdownAndAwaitTermination: Pool terminated!");
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}

package com.example.aakarshak.explore.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Execute {

    //Singleton instance
    private static volatile Execute INSTANCE;

    //Executors for various needs
    private final Executor diskIO;
    private final Executor mainThread;

    private Execute(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public static Execute getInstance() {
        if (INSTANCE == null) {
            //When instance is not available
            synchronized (Execute.class) {
                //Apply lock and check for the instance again
                if (INSTANCE == null) {
                    //When there is no instance, create a new one
                    INSTANCE = new Execute(
                            //Single Thread Executor for Database/Disk operations
                            Executors.newSingleThreadExecutor(),
                            //MainThreadExecutor for UI Thread
                            new MainThreadExecutor()
                    );
                }
            }
        }
        //Returning the instance of Execute
        return INSTANCE;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        //Main Thread Handler attached to the Main Looper
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            //Posts runnables to the message queue of the Main Thread Handler
            mainThreadHandler.post(command);
        }
    }
}

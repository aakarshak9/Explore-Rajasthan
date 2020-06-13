package com.example.aakarshak.explore.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Execute {

    //Singleton instance
    private static volatile Execute INSTANCE;

    //Executors for various needs
    private final Executor diskIO;
    private final Executor mainThread;

    /**
     * Private Constructor to avoid direct instantiation of {@link Execute}
     *
     * @param diskIO     Executor for database/disk operations
     * @param mainThread Executor for running task on UI Thread
     */
    private Execute(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    /**
     * Singleton Constructor for {@link Execute}
     *
     * @return New or existing Instance of {@link Execute}
     */
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

    /**
     * Method that returns the Executor for Database/Disk operations
     *
     * @return Executor for Database/Disk operations
     */
    public Executor getDiskIO() {
        return diskIO;
    }

    /**
     * Method that returns the Executor for updating to Main Thread
     *
     * @return Executor for updating to Main Thread
     */
    public Executor getMainThread() {
        return mainThread;
    }

    /**
     * Executor implementation for executing tasks on Main Thread
     * using a Handler attached to the Main Looper
     */
    private static class MainThreadExecutor implements Executor {
        //Main Thread Handler attached to the Main Looper
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        /**
         * Executes the given command at some time in the future, in a Main Thread.
         *
         * @param command the runnable task
         * @throws java.util.concurrent.RejectedExecutionException if this task cannot be
         *                                                         accepted for execution
         * @throws NullPointerException                            if command is null
         */
        @Override
        public void execute(@NonNull Runnable command) {
            //Posts runnables to the message queue of the Main Thread Handler
            mainThreadHandler.post(command);
        }
    }
}

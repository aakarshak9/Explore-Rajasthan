package com.example.aakarshak.explore.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class UtlityWindow {

    private UtlityWindow() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    public static int getDisplayWindowWidth(Context context) {
        DisplayMetrics displayWindowMetrics = getDisplayWindowMetrics(context);
        if (displayWindowMetrics.widthPixels > 0) {
            //When the Window Manager service has provided the information
            //Returning the Width of the Window
            return displayWindowMetrics.widthPixels;
        }
        //When the Window Manager service did not provide the information
        //Returning the Width of the Device screen
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDisplayWindowHeight(Context context) {
        DisplayMetrics displayWindowMetrics = getDisplayWindowMetrics(context);
        if (displayWindowMetrics.heightPixels > 0) {
            //When the Window Manager service has provided the information
            //Returning the Height of the Window
            return displayWindowMetrics.heightPixels;
        }
        //When the Window Manager service did not provide the information
        //Returning the Height of the Device screen
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    private static DisplayMetrics getDisplayWindowMetrics(Context context) {
        //Creating an Instance of DisplayMetrics
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //Getting the Window Manager service
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            //When the Window Manager service is available, fill in the window metrics
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        //Returning the DisplayMetrics prepared
        return displayMetrics;
    }

}

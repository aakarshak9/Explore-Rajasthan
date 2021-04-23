package com.example.aakarshak.explore.utils;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public class UtilityColor {

    private UtilityColor() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    public static int[] obtainColorsFromTypedArray(Context context, @ArrayRes int colorArrayRes, @ColorRes int defaultColorRes) {
        //Obtaining the Typed Array of Colors from the Resources
        TypedArray typedArrayColors = context.getResources().obtainTypedArray(colorArrayRes);
        //Get the number of Colors
        int noOfColors = typedArrayColors.length();
        //Creating an integer array for the size
        int[] colors = new int[noOfColors];
        //Retrieving the default color from the resources
        int defaultColorInt = ContextCompat.getColor(context, defaultColorRes);
        //Iterating over the TypedArray to get the colors
        for (int index = 0; index < noOfColors; index++) {
            colors[index] = typedArrayColors.getColor(index, defaultColorInt);
        }
        //Release the TypedArray resource
        typedArrayColors.recycle();
        //Returning the colors read from the TypedArray
        return colors;
    }
}

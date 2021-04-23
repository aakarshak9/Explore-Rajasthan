package com.example.aakarshak.explore.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.palette.graphics.Palette;


public class ImageUtility {

    //Constant used when no Resource Id is found for a Drawable
    public static final int NO_RESOURCE_ID = 0;
    //Constant for the Resource Directory name
    private static final String RESOURCE_DIR_NAME = "res";
    //Definition Type constant for Drawable
    private static final String TYPE_DRAWABLE_RES = "drawable";


    private ImageUtility() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    @Nullable
    @WorkerThread
    public static Palette.Swatch extractVibrantSwatch(@Nullable Bitmap bitmap) {
        //Returning NULL when the Bitmap is NULL
        if (bitmap == null) {
            return null;
        }

        //Generating the Palette for the Bitmap
        Palette palette = Palette.from(bitmap).generate();
        //Reading the Vibrant Swatch
        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
        if (vibrantSwatch == null) {
            //When Vibrant is not available, pick the Dark Vibrant
            vibrantSwatch = palette.getDarkVibrantSwatch();
        }
        //Return the Vibrant/Dark-Vibrant Swatch extracted
        return vibrantSwatch;
    }

    @DrawableRes
    public static int findDrawableResourceId(String resourcePath, String packageName, Resources resources) {
        if (!TextUtils.isEmpty(resourcePath) && resourcePath.startsWith(RESOURCE_DIR_NAME)) {
            //When Resource Path is present and points to the Resources directory of the App

            //Determining the start and end for extracting the Resource Name from the Path
            int startIndex = resourcePath.lastIndexOf("/");
            int endIndex = resourcePath.lastIndexOf(".");
            //Returning the Resource Id found for the Resource Path passed
            return resources.getIdentifier(
                    resourcePath.substring(startIndex + 1, endIndex), //The Resource Name
                    TYPE_DRAWABLE_RES, //The 'res' type
                    packageName //The Package Name of the App
            );
        }
        //Returning '0' when the Resource Path was invalid
        return NO_RESOURCE_ID;
    }

    @WorkerThread
    public static Bitmap getOptimizedBitmapFromResource(Context context, Resources resources, @DrawableRes int imageResourceId) {
        //Get the device target dimensions (Normalizing to 50 percent of the value)
        int targetW = (int) (UtlityWindow.getDisplayWindowWidth(context) * 0.5);
        int targetH = (int) (UtlityWindow.getDisplayWindowHeight(context) * 0.5);

        //Creating an Instance of BitmapFactory Options to decode the dimensions of the original
        //Bitmap from the Image Resource
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true; //Decoding for Bounds only
        //Decoding the dimensions of the original bitmap
        BitmapFactory.decodeResource(resources, imageResourceId, bitmapOptions);

        //Reading the bitmap's original dimensions
        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        //Calculating the amount to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        //Decode the image using the scaling factor determined
        bitmapOptions.inJustDecodeBounds = false; //Decoding the Image
        bitmapOptions.inSampleSize = scaleFactor;

        //Decoding into Bitmap with the scaled down dimensions and returning the same
        return BitmapFactory.decodeResource(resources, imageResourceId, bitmapOptions);
    }

}

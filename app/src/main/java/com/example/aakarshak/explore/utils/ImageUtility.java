package com.example.aakarshak.explore.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;

/**
 * Utility class that deals with reading, extracting information and modifying {@link android.graphics.Bitmap}.
 *
 * @author Kaushik N Sanji
 */
public class ImageUtility {

    //Constant used when no Resource Id is found for a Drawable
    public static final int NO_RESOURCE_ID = 0;
    //Constant for the Resource Directory name
    private static final String RESOURCE_DIR_NAME = "res";
    //Definition Type constant for Drawable
    private static final String TYPE_DRAWABLE_RES = "drawable";

    /**
     * Private Constructor to avoid direct instantiation of {@link ImageUtility}
     */
    private ImageUtility() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    /**
     * Method that extracts a Vibrant or a Dark Vibrant Palette Swatch from the {@code bitmap}
     * given.
     * <p>
     * This method needs to be invoked from a background thread.
     * </p>
     *
     * @param bitmap The {@link Bitmap} of an Image from which the Vibrant Palette Swatch needs to
     *               be extracted.
     * @return A Vibrant or a Dark Vibrant {@link Palette.Swatch} for the given {@link Bitmap}.
     * If Vibrant is not available then the Dark Vibrant will be returned. Can be {@code null}
     * if the Dark Vibrant is also not available or when the {@link Bitmap} given is {@code null}.
     */
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

    /**
     * Method that finds the Resource Id of the Drawable given the Resource Path {@code resourcePath}
     * to the Drawable present in resources.
     *
     * @param resourcePath The Resource Path String to the Drawable whose Resource Id is to be found
     * @param packageName  The Package Name of the App
     * @param resources    Instance of {@link Resources}
     * @return The Resource Id of the Drawable resource if found or {@link #NO_RESOURCE_ID} when not found.
     * When {@code resourcePath} is invalid, {@link #NO_RESOURCE_ID} will be returned.
     */
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

    /**
     * Method that decodes and returns an Optimized {@link Bitmap} from the Drawable Resource pointed to by
     * the Resource Id {@code imageResourceId}.
     * <p>
     * This method needs to be invoked from a background thread.
     * </p>
     *
     * @param context         A {@link Context} to read the Window dimensions
     * @param resources       Instance of {@link Resources}
     * @param imageResourceId The Resource Id of the Drawable to be decoded.
     * @return An Optimized {@link Bitmap} decoded from the Drawable Resource {@code imageResourceId}
     */
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

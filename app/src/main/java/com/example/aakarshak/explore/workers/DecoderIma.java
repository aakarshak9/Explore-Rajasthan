package com.example.aakarshak.explore.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.aakarshak.explore.cache.Image_bitmap;
import com.example.aakarshak.explore.utils.ImageUtility;

public class DecoderIma extends AsyncTaskLoader<Bitmap> {

    //Constant used for logs
    private static final String LOG_TAG = DecoderIma.class.getSimpleName();
    //Stores the Resource Id of the Image that needs to be decoded
    private int mImageResourceId;

    //Stores the Bitmap Decoded
    private Bitmap mDecodedBitmap;

    /**
     * Constructor of the Loader {@link DecoderIma}
     *
     * @param context         A {@link Context} to retrieve the application context.
     * @param imageResourceId Integer representing the Resource Id
     *                        of the Image that needs to be decoded.
     */
    public DecoderIma(@NonNull Context context, @DrawableRes int imageResourceId) {
        super(context);
        mImageResourceId = imageResourceId;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     *
     * @return The result of the load operation which is a Bitmap downloaded from the URL
     * @throws android.support.v4.os.OperationCanceledException if the load is canceled during execution.
     */
    @Nullable
    @Override
    public Bitmap loadInBackground() {
        if (mImageResourceId > ImageUtility.NO_RESOURCE_ID) {
            //When we have the Resource Id of the Image
            try {
                //Looking up for the Image in Memory Cache for the given Resource Id
                Bitmap cachedBitmap = Image_bitmap.getBitmapFromCache(mImageResourceId);
                if (cachedBitmap != null) {
                    //When Bitmap image was present in Memory Cache, return the Bitmap retrieved
                    return cachedBitmap;
                } else {
                    //When Bitmap image was NOT present in Memory Cache, decode the Bitmap from the Image Resource Id
                    Bitmap decodedBitmap = ImageUtility.getOptimizedBitmapFromResource(getContext(), getContext().getResources(), mImageResourceId);
                    if (decodedBitmap != null) {
                        //On Successful decoding

                        //Uploading the Bitmap to GPU for caching in background thread (for faster loads)
                        decodedBitmap.prepareToDraw();

                        //Adding the decoded Bitmap to cache
                        Image_bitmap.addBitmapToCache(mImageResourceId, decodedBitmap);

                        //Returning the Bitmap decoded
                        return decodedBitmap;
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "loadInBackground: Failed while decoding the bitmap for the Resource "
                        + getContext().getResources().getResourceName(mImageResourceId), e);
            }
        }
        //For all else, returning null
        return null;
    }

    /**
     * Sends the result of the load to the registered listener. Should only be called by subclasses.
     *
     * @param newBitmap the result of the load which is a new Bitmap downloaded
     */
    @Override
    public void deliverResult(Bitmap newBitmap) {
        if (isReset()) {
            //Ignoring the result if the loader is already reset
            newBitmap = null;
            //Returning when the loader is already reset
            return;
        }

        //Storing a reference to the old Bitmap as we are about to deliver the result
        Bitmap oldBitmap = mDecodedBitmap;
        mDecodedBitmap = newBitmap;

        if (isStarted()) {
            //Delivering the result when the loader is started
            super.deliverResult(newBitmap);
        }

        //invalidating the old bitmap as it is not required anymore
        if (oldBitmap != null && oldBitmap != newBitmap) {
            oldBitmap = null;
        }
    }

    /**
     * Subclasses must implement this to take care of loading their data,
     * as per {@link #startLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #startLoading()}.
     */
    @Override
    protected void onStartLoading() {
        if (mDecodedBitmap != null) {
            //Deliver the result immediately if the Bitmap is already decoded
            deliverResult(mDecodedBitmap);
        }

        if (takeContentChanged() || mDecodedBitmap == null) {
            //Force a new Load when the Bitmap Image is not yet decoded
            //or the content has changed
            forceLoad();
        }
    }

    /**
     * Subclasses must implement this to take care of stopping their loader,
     * as per {@link #stopLoading()}.  This is not called by clients directly,
     * but as a result of a call to {@link #stopLoading()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onStopLoading() {
        //Canceling the load if any as the loader has entered Stopped state
        cancelLoad();
    }

    /**
     * Subclasses must implement this to take care of resetting their loader,
     * as per {@link #reset()}.  This is not called by clients directly,
     * but as a result of a call to {@link #reset()}.
     * This will always be called from the process's main thread.
     */
    @Override
    protected void onReset() {
        //Ensuring the loader has stopped
        onStopLoading();

        //Releasing the resources associated with the Loader
        releaseResources();
    }

    /**
     * Called if the task was canceled before it was completed.  Gives the class a chance
     * to clean up post-cancellation and to properly dispose of the result.
     *
     * @param data The value that was returned by {@link #loadInBackground}, or null
     *             if the task threw {@link android.support.v4.os.OperationCanceledException}.
     */
    @Override
    public void onCanceled(Bitmap data) {
        //Canceling any asynchronous load
        super.onCanceled(data);

        //Releasing the resources associated with the Loader, as the Loader is canceled
        releaseResources();
    }

    /**
     * Method to release the resources associated with the loader
     */
    private void releaseResources() {
        //Invalidating the Loader data
        if (mDecodedBitmap != null) {
            mDecodedBitmap = null;
            mImageResourceId = ImageUtility.NO_RESOURCE_ID;
        }
    }

    /**
     * Method that returns the Image Resource Id from which this loader decodes the image
     *
     * @return The Image Resource Id from which this loader decodes the image
     */
    int getImageResourceId() {
        return mImageResourceId;
    }
}

package com.example.aakarshak.explore.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.aakarshak.explore.cache.Image_bitmap;
import com.example.aakarshak.explore.utils.ImageUtility;

public class DecoderIma extends AsyncTaskLoader<Bitmap> {

    //Constant used for logs
    private static final String LOG_TAG = DecoderIma.class.getSimpleName();
    //Stores the Resource Id of the Image that needs to be decoded
    private int mImageResourceId;

    //Stores the Bitmap Decoded
    private Bitmap mDecodedBitmap;

    public DecoderIma(@NonNull Context context, @DrawableRes int imageResourceId) {
        super(context);
        mImageResourceId = imageResourceId;
    }

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

    @Override
    protected void onStopLoading() {
        //Canceling the load if any as the loader has entered Stopped state
        cancelLoad();
    }

    @Override
    protected void onReset() {
        //Ensuring the loader has stopped
        onStopLoading();

        //Releasing the resources associated with the Loader
        releaseResources();
    }

    @Override
    public void onCanceled(Bitmap data) {
        //Canceling any asynchronous load
        super.onCanceled(data);

        //Releasing the resources associated with the Loader, as the Loader is canceled
        releaseResources();
    }

    private void releaseResources() {
        //Invalidating the Loader data
        if (mDecodedBitmap != null) {
            mDecodedBitmap = null;
            mImageResourceId = ImageUtility.NO_RESOURCE_ID;
        }
    }

    int getImageResourceId() {
        return mImageResourceId;
    }
}

package com.example.aakarshak.explore.workers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ImageView;

import com.example.aakarshak.explore.R;

public class DecoderImaFrag extends Fragment
        implements LoaderManager.LoaderCallbacks<Bitmap> {

    //Constant used for logs and Fragment Tag
    private static final String LOG_TAG = DecoderImaFrag.class.getSimpleName();

    //Stores the ImageView component that needs to be updated when the Image is decoded
    private ImageView mImageView;

    //Stores the Resource Id of the Image that needs to be decoded
    private int mImageResourceId;

    //Stores the OnFailureListener instance
    private OnFailureListener mOnFailureListener;

    //Stores the OnSuccessListener instance
    private OnSuccessListener mOnSuccessListener;

    /**
     * Static Constructor of the Fragment
     *
     * @param fragmentManager FragmentManager that manages the Fragments
     * @param tagId           The integer position that identifies the view being inflated
     *                        in the RecyclerView's Adapter or any other unique id.
     * @return Instance of the Fragment {@link DecoderImaFrag}
     */
    public static DecoderImaFrag newInstance(FragmentManager fragmentManager, int tagId) {
        //Creating the Fragment Tag string using the tagId passed
        String fragmentTagStr = LOG_TAG + "_" + tagId;

        //Retrieving the Fragment from the FragmentManager if existing
        DecoderImaFrag decoderImaFrag =
                (DecoderImaFrag) fragmentManager.findFragmentByTag(fragmentTagStr);
        if (decoderImaFrag == null) {
            //When the Fragment is being added for the first time

            //Instantiating the Fragment
            decoderImaFrag = new DecoderImaFrag();
            //Adding the Fragment to Transaction and committing with state losses
            fragmentManager.beginTransaction().add(decoderImaFrag, fragmentTagStr).commitAllowingStateLoss();
        }

        //Returning the Fragment instance
        return decoderImaFrag;
    }

    /**
     * Method that loads the Image from Memory Cache or decodes the Image from the Resource Id passed
     * if necessary.
     *
     * @param imageView       The ImageView Component to which the Image needs to be updated
     * @param imageResourceId Integer representing the Resource Id of the Image that needs to be decoded
     */
    public void executeAndUpdate(ImageView imageView, @DrawableRes int imageResourceId) {
        //Delegating to other overloaded method with the derived instance for LoaderManager
        executeAndUpdate(imageView, imageResourceId, obtainLoaderManager(imageView));
    }

    /**
     * Method that loads the Image from Memory Cache or decodes the Image from the Resource Id passed
     * if necessary.
     *
     * @param imageView       The ImageView Component to which the Image needs to be updated
     * @param imageResourceId Integer representing the Resource Id of the Image that needs to be decoded
     * @param loaderManager   Instance of {@link LoaderManager} to use for decoding the image.
     */
    public void executeAndUpdate(ImageView imageView, @DrawableRes int imageResourceId, LoaderManager loaderManager) {
        //Saving the parameters passed
        mImageResourceId = imageResourceId;
        mImageView = imageView;

        if (loaderManager == null) {
            //When we do not have the LoaderManager instance for decoding the Image
            //throw a Runtime Exception
            throw new IllegalStateException("LoaderManager is not attached.");
        }

        //Getting the loader at the loaderId if any
        DecoderIma decoderIma = getImageDecoder(imageResourceId, loaderManager);

        //Resetting the ImageView to the default Thumbnail Image for lazy loading
        mImageView.setImageResource(R.drawable.layer_def_pic);

        //Boolean to check if we need to restart the loader
        boolean isNewImage = false;
        if (decoderIma != null) {
            //When we have a previously registered loader

            //Set the Loader to be restarted when the Image Resource Id passed is
            //not the same as that of the loader
            isNewImage = imageResourceId != decoderIma.getImageResourceId();
        }

        if (isNewImage) {
            //Restarting the Loader when the Image Resource Id is new
            loaderManager.restartLoader(imageResourceId, null, this);
        } else {
            //Invoking the Loader AS-IS if the Image Resource Id is same
            //or if the Loader is not yet registered with the loader id
            loaderManager.initLoader(imageResourceId, null, this);
        }

    }

    /**
     * Method that returns the instance of the {@link DecoderIma} for the
     * Loader Id {@code loaderId} passed.
     *
     * @param loaderId      The Id of the Loader whose Loader instance needs to be looked up
     * @param loaderManager Instance of {@link LoaderManager}
     * @return Instance of {@link DecoderIma} if found; else {@code null}
     */
    @Nullable
    private DecoderIma getImageDecoder(int loaderId, LoaderManager loaderManager) {
        //Getting the loader at the loaderId
        Loader<Bitmap> loader = loaderManager.getLoader(loaderId);
        if (loader != null && loader instanceof DecoderIma) {
            //Returning the DecoderIma instance
            return (DecoderIma) loader;
        } else {
            //Returning NULL when not found
            return null;
        }
    }

    /**
     * Method that retrieves the {@link FragmentActivity} instance required
     * for obtaining the {@link LoaderManager} instance.
     *
     * @param context The {@link Context} to retrieve the Activity from.
     * @return Instance of the {@link FragmentActivity} is any; else {@code null}
     */
    @Nullable
    private FragmentActivity obtainActivity(@Nullable Context context) {
        if (context == null) {
            //Return Null when Null
            return null;
        } else if (context instanceof FragmentActivity) {
            //Return the FragmentActivity instance when Context is of type FragmentActivity
            return (FragmentActivity) context;
        } else if (context instanceof ContextWrapper) {
            //Recall with the Base Context when Context is of type ContextWrapper
            return obtainActivity(((ContextWrapper) context).getBaseContext());
        }
        //Returning Null when we could not derive the Activity instance from the given Context
        return null;
    }

    /**
     * Method that retrieves the {@link LoaderManager} instance for the given view {@code imageView}
     *
     * @param imageView The {@link ImageView} to retrieve the {@link LoaderManager} from.
     * @return Instance of {@link LoaderManager} when the {@link FragmentActivity} was derivable
     * from {@code imageView}; else {@code null}
     */
    @Nullable
    private LoaderManager obtainLoaderManager(ImageView imageView) {
        //Obtaining the Activity from the ImageView
        FragmentActivity activity = obtainActivity(imageView.getContext());
        if (activity != null) {
            //When we have the Activity, return the LoaderManager instance
            return activity.getSupportLoaderManager();
        }
        //Returning Null when Activity could not be derived from ImageView
        return null;
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @NonNull
    @Override
    public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
        //Returning an Instance of DecoderIma to start the Image decoding
        return new DecoderIma(mImageView.getContext(), mImageResourceId);
    }

    /**
     * Called when a previously created loader has finished its load.
     * This is where we display the Bitmap image generated by the loader
     *
     * @param loader      The Loader that has finished.
     * @param bitmapImage The Bitmap Image generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Bitmap> loader, Bitmap bitmapImage) {
        if (bitmapImage != null && mImageView != null) {
            //When the bitmap was decoded successfully and the ImageView is still attached
            onDecodingSuccess(bitmapImage);
        } else if (mImageView != null) {
            //When the bitmap failed to be decoded and the ImageView is still attached
            onDecodingFailure();
        }
    }

    /**
     * Method invoked on Success of decoding the Image.
     * This method sets the decoded Image {@code bitmapImage} to the ImageView
     * and triggers the Success event if {@link OnSuccessListener} is registered.
     *
     * @param bitmapImage The {@link Bitmap} of the Image Resource decoded
     */
    private void onDecodingSuccess(Bitmap bitmapImage) {
        //Updating the ImageView when the Bitmap is decoded successfully
        mImageView.setImageBitmap(bitmapImage);
        //When the OnSuccessListener is registered, dispatch the success event
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess(bitmapImage);
        }
    }

    /**
     * Method invoked on failure of decoding the Image, or when the Image Resource Id was invalid.
     * This method sets the error image 'R.drawable.ic_all_no_picture' to the ImageView
     * and triggers the Failure event if {@link OnFailureListener} is registered.
     */
    private void onDecodingFailure() {
        //Resetting the ImageView to the error image when the Bitmap failed to be decoded
        mImageView.setImageResource(R.drawable.layer_all_noooo_picture);
        //When the OnFailureListener is registered, dispatch the failure event
        if (mOnFailureListener != null) {
            mOnFailureListener.onFailure();
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
        //Resetting the ImageView to the default Thumbnail Image
        mImageView.setImageResource(R.drawable.layer_def_pic);
    }

    /**
     * Method that registers the {@link OnFailureListener} to dispatch failure events.
     *
     * @param listener Instance of {@link OnFailureListener} that wishes to receive failure events.
     * @return Instance of the this Fragment {@link DecoderImaFrag} to chain method calls
     */
    public DecoderImaFrag setOnFailureListener(OnFailureListener listener) {
        mOnFailureListener = listener;
        return this;
    }

    /**
     * Method that registers the {@link OnSuccessListener} to dispatch success events.
     *
     * @param listener Instance of {@link OnSuccessListener} that wishes to receive success events.
     * @return Instance of the this Fragment {@link DecoderImaFrag} to chain method calls
     */
    public DecoderImaFrag setOnSuccessListener(OnSuccessListener listener) {
        mOnSuccessListener = listener;
        return this;
    }

    /**
     * Callback interface to be implemented by the Activity/Fragment
     * to receive failure events of Bitmap decoding operation
     */
    public interface OnFailureListener {
        /**
         * Callback method of {@link OnFailureListener} invoked when the Bitmap
         * fails to be decoded
         */
        void onFailure();
    }

    /**
     * Callback interface to be implemented by the Activity/Fragment
     * to receive success events of Bitmap decoding operation
     */
    public interface OnSuccessListener {
        /**
         * Callback method of {@link OnSuccessListener} invoked when the Bitmap
         * was decoded successfully.
         *
         * @param resultBitmap The Bitmap that was decoded successfully.
         */
        void onSuccess(@NonNull Bitmap resultBitmap);
    }
}

package com.example.aakarshak.explore.workers;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.aakarshak.explore.R;

public class DecoderImaFrag extends Fragment implements LoaderManager.LoaderCallbacks<Bitmap> {

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

    public void executeAndUpdate(ImageView imageView, @DrawableRes int imageResourceId) {
        //Delegating to other overloaded method with the derived instance for LoaderManager
        executeAndUpdate(imageView, imageResourceId, obtainLoaderManager(imageView));
    }

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

    @NonNull
    @Override
    public Loader<Bitmap> onCreateLoader(int id, @Nullable Bundle args) {
        //Returning an Instance of DecoderIma to start the Image decoding
        return new DecoderIma(mImageView.getContext(), mImageResourceId);
    }

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

    private void onDecodingSuccess(Bitmap bitmapImage) {
        //Updating the ImageView when the Bitmap is decoded successfully
        mImageView.setImageBitmap(bitmapImage);
        //When the OnSuccessListener is registered, dispatch the success event
        if (mOnSuccessListener != null) {
            mOnSuccessListener.onSuccess(bitmapImage);
        }
    }

    private void onDecodingFailure() {
        //Resetting the ImageView to the error image when the Bitmap failed to be decoded
        mImageView.setImageResource(R.drawable.layer_all_noooo_picture);
        //When the OnFailureListener is registered, dispatch the failure event
        if (mOnFailureListener != null) {
            mOnFailureListener.onFailure();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Bitmap> loader) {
        //Resetting the ImageView to the default Thumbnail Image
        mImageView.setImageResource(R.drawable.layer_def_pic);
    }

    public DecoderImaFrag setOnFailureListener(OnFailureListener listener) {
        mOnFailureListener = listener;
        return this;
    }

    public DecoderImaFrag setOnSuccessListener(OnSuccessListener listener) {
        mOnSuccessListener = listener;
        return this;
    }

    public interface OnFailureListener {

        void onFailure();
    }

    public interface OnSuccessListener {

        void onSuccess(@NonNull Bitmap resultBitmap);
    }
}

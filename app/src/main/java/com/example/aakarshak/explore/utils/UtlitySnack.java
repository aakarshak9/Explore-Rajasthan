package com.example.aakarshak.explore.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.example.aakarshak.explore.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class UtlitySnack {

    //Instance of the Snackbar injected
    private final Snackbar mSnackbar;

    public UtlitySnack(Snackbar snackbar) {
        mSnackbar = snackbar;
    }

    public UtlitySnack revealCompleteMessage() {
        //Retrieving the V of the Snackbar
        View snackbarView = mSnackbar.getView();
        //Finding the TextView of the Snackbar
        TextView snackbarTextView = snackbarView.findViewById(R.id.snackbar_text);
        //Overriding MaxLines limit to Integer's Max value
        snackbarTextView.setMaxLines(Integer.MAX_VALUE);
        //Returning the instance of this for method chaining
        return this;
    }

    public UtlitySnack setDismissAction(@StringRes int dismissActionLabelResId) {
        //Setting the duration to indefinite
        if (mSnackbar.getDuration() != BaseTransientBottomBar.LENGTH_INDEFINITE) {
            mSnackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        }
        //Setting the dismiss action and returning the instance of this for method chaining
        return setAction(dismissActionLabelResId, (view) -> mSnackbar.dismiss());
    }

    public UtlitySnack setDismissAction(String dismissActionLabel) {
        //Setting the duration to indefinite
        if (mSnackbar.getDuration() != BaseTransientBottomBar.LENGTH_INDEFINITE) {
            mSnackbar.setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
        }
        //Setting the dismiss action and returning the instance of this for method chaining
        return setAction(dismissActionLabel, (view) -> mSnackbar.dismiss());
    }

    public UtlitySnack setAction(@StringRes int actionLabelResId, final View.OnClickListener listener) {
        //Setting the Action Label and its Listener
        mSnackbar.setAction(actionLabelResId, listener);
        //Returning the instance of this for method chaining
        return this;
    }

    public UtlitySnack setAction(String actionLabel, final View.OnClickListener listener) {
        //Setting the Action Label and its Listener
        mSnackbar.setAction(actionLabel, listener);
        //Returning the instance of this for method chaining
        return this;
    }

    public void showSnack() {
        mSnackbar.show();
    }
}

package com.example.aakarshak.explore.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class UtlitySnack {

    //Instance of the Snackbar injected
    private Snackbar mSnackbar;

    /**
     * Constructor of {@link UtlitySnack}
     *
     * @param snackbar Instance of {@link Snackbar}
     */
    public UtlitySnack(Snackbar snackbar) {
        mSnackbar = snackbar;
    }

    /**
     * Method that removes the MaxLines limit on Snackbar's TextView.
     * Out of the box, it is restricted to two text lines.
     *
     * @return Instance of {@link UtlitySnack} to enable method chaining.
     */
    public UtlitySnack revealCompleteMessage() {
        //Retrieving the V of the Snackbar
        View snackbarView = mSnackbar.getView();
        //Finding the TextView of the Snackbar
        TextView snackbarTextView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        //Overriding MaxLines limit to Integer's Max value
        snackbarTextView.setMaxLines(Integer.MAX_VALUE);
        //Returning the instance of this for method chaining
        return this;
    }

    /**
     * Method that sets the Dismiss action on {@link Snackbar} with the action label as given by
     * {@code dismissActionLabelResId}. Since this is for Dismiss action, the duration of {@link Snackbar}
     * is set to {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @param dismissActionLabelResId The String resource of the Action Label for Dismiss action
     * @return Instance of {@link UtlitySnack} to enable method chaining.
     */
    public UtlitySnack setDismissAction(@StringRes int dismissActionLabelResId) {
        //Setting the duration to indefinite
        if (mSnackbar.getDuration() != Snackbar.LENGTH_INDEFINITE) {
            mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        }
        //Setting the dismiss action and returning the instance of this for method chaining
        return setAction(dismissActionLabelResId, (view) -> mSnackbar.dismiss());
    }

    /**
     * Method that sets the Dismiss action on {@link Snackbar} with the action label as given
     * by {@code dismissActionLabel}. Since this is for Dismiss action, the duration of {@link Snackbar}
     * is set to {@link Snackbar#LENGTH_INDEFINITE}.
     *
     * @param dismissActionLabel String containing the value of the Action Label for Dismiss action
     * @return Instance of {@link UtlitySnack} to enable method chaining.
     */
    public UtlitySnack setDismissAction(String dismissActionLabel) {
        //Setting the duration to indefinite
        if (mSnackbar.getDuration() != Snackbar.LENGTH_INDEFINITE) {
            mSnackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        }
        //Setting the dismiss action and returning the instance of this for method chaining
        return setAction(dismissActionLabel, (view) -> mSnackbar.dismiss());
    }

    /**
     * Method that sets the Action with its label {@code actionLabelResId} and listener {@code listener}.
     *
     * @param actionLabelResId The String resource of the Action Label.
     * @param listener         The Listener for the Action.
     * @return Instance of {@link UtlitySnack} to enable method chaining.
     */
    public UtlitySnack setAction(@StringRes int actionLabelResId, final View.OnClickListener listener) {
        //Setting the Action Label and its Listener
        mSnackbar.setAction(actionLabelResId, listener);
        //Returning the instance of this for method chaining
        return this;
    }

    /**
     * Method that sets the Action with its label {@code actionLabel} and listener {@code listener}.
     *
     * @param actionLabel The String containing the value of the Action Label.
     * @param listener    The Listener for the Action.
     * @return Instance of {@link UtlitySnack} to enable method chaining.
     */
    public UtlitySnack setAction(String actionLabel, final View.OnClickListener listener) {
        //Setting the Action Label and its Listener
        mSnackbar.setAction(actionLabel, listener);
        //Returning the instance of this for method chaining
        return this;
    }

    /**
     * Terminal method of {@link UtlitySnack} that shows the {@link Snackbar} prepared.
     */
    public void showSnack() {
        mSnackbar.show();
    }
}

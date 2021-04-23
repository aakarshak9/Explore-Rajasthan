package com.example.aakarshak.explore.extensions;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

public class BottomClassB extends CoordinatorLayout.Behavior<BottomNavigationView> {

    //The Duration of the Snap animation used by the ValueAnimator
    private static final long SNAP_ANIM_DURATION = 150L;
    //The Input type that initiates the scroll event, used for Snap behavior animation
    @ViewCompat.NestedScrollType
    private int mLastInputScrollType;
    //The ValueAnimator used for the Snap animation of the TranslationY property
    //of the BottomNavigationView
    private ValueAnimator mOffsetAnimator;

    public BottomClassB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            //When dependency is a Snackbar, adjust its layout parameters such that it appears above
            //the BottomNavigationView
            updateSnackbarParams(child, (Snackbar.SnackbarLayout) dependency);
            //(Not returning true since the BottomNavigationView is not dependent on Snackbar)
        }
        //Returning the value of super
        return super.layoutDependsOn(parent, child, dependency);
    }

    private void updateSnackbarParams(BottomNavigationView bottomNavigationView, Snackbar.SnackbarLayout snackbarLayout) {
        if (snackbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
            //When the Snackbar's layout is defined by the CoordinatorLayout
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) snackbarLayout.getLayoutParams();

            //Anchor the Snackbar to the TOP of BottomNavigationView
            layoutParams.setAnchorId(bottomNavigationView.getId());
            layoutParams.anchorGravity = Gravity.TOP;
            layoutParams.gravity = Gravity.TOP;

            //Set the corrected layout parameters to the Snackbar
            snackbarLayout.setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull BottomNavigationView child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        //Returning False when the Scroll axes is other than vertical which we are interested in
        if (axes != ViewCompat.SCROLL_AXIS_VERTICAL) {
            return false;
        }

        //Saving the Scroll Input type for the Snap Behavior animation
        mLastInputScrollType = type;

        //Clearing any pending animations when a new scroll has started
        if (mOffsetAnimator != null) {
            mOffsetAnimator.cancel();
        }

        //Returning True to receive the vertical scroll events
        return true;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull BottomNavigationView child,
                                  @NonNull View target, int dx, int dy,
                                  @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        //When a vertical scroll event is received, update the BottomNavigationView's vertical position
        //relatively by the number of pixels scrolled, clamping it from 0 to its total height
        //(This hides/displays the BottomNavigationView based on the amount and direction of scroll)
        child.setTranslationY(MathUtils.clamp(dy + child.getTranslationY(), 0f, child.getHeight()));
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int type) {
        if (mLastInputScrollType == ViewCompat.TYPE_TOUCH || type == ViewCompat.TYPE_NON_TOUCH) {
            //When the type of the input that initiated the scroll is of Touch type or the current type is non-touch/fling

            //Animate the relative vertical position of the BottomNavigationView to mimic the
            //snap animation behavior by displaying the BottomNavigationView when its
            //relative vertical position is less than half of its own height
            animateSnapBehavior(child, child.getTranslationY() < (child.getHeight() * 0.5f));
        }
    }

    private void animateSnapBehavior(BottomNavigationView bottomNavigationView, boolean visible) {
        if (mOffsetAnimator == null) {
            //When the ValueAnimator is not yet initialized

            //Initialize with DecelerateInterpolator for 150ms
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(new DecelerateInterpolator());
            mOffsetAnimator.setDuration(SNAP_ANIM_DURATION);
            //Add listener to update the relative vertical position of BottomNavigationView
            //based on the updated property animated value
            mOffsetAnimator.addUpdateListener(animation -> bottomNavigationView.setTranslationY((float) animation.getAnimatedValue()));
        } else {
            //Clear any pending animations when already initialized
            mOffsetAnimator.cancel();
        }

        //Setting the target translation of relative vertical position based on the "visible" boolean
        float targetTranslationY = visible ? 0f : bottomNavigationView.getHeight();
        //Setting the start and final values to cycle through for the vertical position animation
        mOffsetAnimator.setFloatValues(bottomNavigationView.getTranslationY(), targetTranslationY);
        //Starting the animation
        mOffsetAnimator.start();
    }
}

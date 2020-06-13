package com.example.aakarshak.explore.extensions;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class BottomClassB extends CoordinatorLayout.Behavior<BottomNavigationView> {

    //The Duration of the Snap animation used by the ValueAnimator
    private static final long SNAP_ANIM_DURATION = 150L;
    //The Input type that initiates the scroll event, used for Snap behavior animation
    @ViewCompat.NestedScrollType
    private int mLastInputScrollType;
    //The ValueAnimator used for the Snap animation of the TranslationY property
    //of the BottomNavigationView
    private ValueAnimator mOffsetAnimator;

    /**
     * Default constructor for inflating Behaviors from layout. The Behavior will have
     * the opportunity to parse specially defined layout parameters. These parameters will
     * appear on the child view tag.
     */
    public BottomClassB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Determine whether the supplied child view has another specific sibling view as a
     * layout dependency.
     * <p>
     * <p>This method will be called at least once in response to a layout request. If it
     * returns true for a given child and dependency view pair, the parent CoordinatorLayout
     * will:</p>
     * <ol>
     * <li>Always lay out this child after the dependent child is laid out, regardless
     * of child order.</li>
     * <li>Call {@link #onDependentViewChanged} when the dependency view's layout or
     * position changes.</li>
     * </ol>
     *
     * @param parent     the parent view of the given child
     * @param child      the child view to test
     * @param dependency the proposed dependency of child
     * @return true if child's layout depends on the proposed dependency's layout,
     * false otherwise
     */
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

    /**
     * Method that updates the {@link Snackbar}'s layout parameters such that the Snackbar
     * is docked on top of the {@code bottomNavigationView} instead of being displayed
     * behind the {@code bottomNavigationView} which is the default behavior.
     *
     * @param bottomNavigationView The {@link BottomNavigationView} child
     * @param snackbarLayout       The {@link Snackbar.SnackbarLayout} which is the dependency
     */
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

    /**
     * Called when a descendant of the CoordinatorLayout attempts to initiate a nested scroll.
     * <p>
     * <p>Any Behavior associated with any direct child of the CoordinatorLayout may respond
     * to this event and return true to indicate that the CoordinatorLayout should act as
     * a nested scrolling parent for this scroll. Only Behaviors that return true from
     * this method will receive subsequent nested scroll events.</p>
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     *                          associated with
     * @param child             the child view of the CoordinatorLayout this Behavior is associated with
     * @param directTargetChild the child view of the CoordinatorLayout that either is or
     *                          contains the target of the nested scroll operation
     * @param target            the descendant view of the CoordinatorLayout initiating the nested scroll
     * @param axes              the axes that this nested scroll applies to. See
     *                          {@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
     *                          {@link ViewCompat#SCROLL_AXIS_VERTICAL}
     * @param type              the type of input which cause this scroll event
     * @return true if the Behavior wishes to accept this nested scroll
     */
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

    /**
     * Called when a nested scroll in progress is about to update, before the target has
     * consumed any of the scrolled distance.
     * <p>
     * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     * </p>
     * <p>
     * <p><code>onNestedPreScroll</code> is called each time the nested scroll is updated
     * by the nested scrolling child, before the nested scrolling child has consumed the scroll
     * distance itself. <em>Each Behavior responding to the nested scroll will receive the
     * same values.</em> The CoordinatorLayout will report as consumed the maximum number
     * of pixels in either direction that any Behavior responding to the nested scroll reported
     * as consumed.</p>
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     *                          associated with
     * @param child             the child view of the CoordinatorLayout this Behavior is associated with
     * @param target            the descendant view of the CoordinatorLayout performing the nested scroll
     * @param dx                the raw horizontal number of pixels that the user attempted to scroll
     * @param dy                the raw vertical number of pixels that the user attempted to scroll
     * @param consumed          out parameter. consumed[0] should be set to the distance of dx that
     *                          was consumed, consumed[1] should be set to the distance of dy that
     *                          was consumed
     * @param type              the type of input which cause this scroll event
     */
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

    /**
     * Called when a nested scroll has ended.
     * <p>
     * <p>Any Behavior associated with any direct child of the CoordinatorLayout may elect
     * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
     * that returned true will receive subsequent nested scroll events for that nested scroll.
     * </p>
     * <p>
     * <p><code>onStopNestedScroll</code> marks the end of a single nested scroll event
     * sequence. This is a good place to clean up any state related to the nested scroll.
     * </p>
     *
     * @param coordinatorLayout the CoordinatorLayout parent of the view this Behavior is
     *                          associated with
     * @param child             the child view of the CoordinatorLayout this Behavior is associated with
     * @param target            the descendant view of the CoordinatorLayout that initiated
     *                          the nested scroll
     * @param type              the type of input which cause this scroll event
     */
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

    /**
     * Method that adjusts the relative vertical position of {@code bottomNavigationView} based on the value
     * of {@code visible} to mimic the Snap animation of AppBarLayout.
     *
     * @param bottomNavigationView The {@link BottomNavigationView} child
     * @param visible              Boolean that defines whether the {@link BottomNavigationView} needs to
     *                             be completely shown {@code true} or hidden away {@code false} for the Snap animation.
     */
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

package com.example.aakarshak.explore.ui.restaurants;

import com.example.aakarshak.explore.data.local.models.RestaurantClass;

public interface UserActionResto {
    /**
     * Callback Method of {@link UserActionResto} invoked when
     * the user clicks on the Item V itself. This should launch
     * the website link if any.
     *
     * @param itemPosition    The adapter position of the Item clicked
     * @param restaurantClass The {@link RestaurantClass} associated with the Item clicked
     */
    void onOpenLink(final int itemPosition, RestaurantClass restaurantClass);

    /**
     * Callback Method of {@link UserActionResto} invoked when
     * the user clicks on the Map ImageButton or the Location Info TextView of the Item V.
     * This should launch any Map application passing in the location information.
     *
     * @param itemPosition    The adapter position of the Item clicked
     * @param restaurantClass The {@link RestaurantClass} associated with the Item clicked
     */
    void onOpenLocation(final int itemPosition, RestaurantClass restaurantClass);

    /**
     * Callback Method of {@link UserActionResto} invoked when
     * the user clicks on the Call ImageButton or the Contact Info TextView of the Item V.
     * This should launch any Dialer application passing in the Contact Number.
     *
     * @param itemPosition    The adapter position of the Item clicked
     * @param restaurantClass The {@link RestaurantClass} associated with the Item clicked
     */
    void onOpenContact(final int itemPosition, RestaurantClass restaurantClass);

    /**
     * Callback Method of {@link UserActionResto} invoked when
     * the user clicks and holds on to the Contact Info TextView of the Item V. This should
     * launch a Share Intent passing in the Contact Number.
     *
     * @param itemPosition    The adapter position of the Item clicked and held
     * @param restaurantClass The {@link RestaurantClass} associated with the Item clicked and held
     */
    void onContactLongClicked(final int itemPosition, RestaurantClass restaurantClass);

    /**
     * Callback Method of {@link UserActionResto} invoked when
     * the user clicks and holds on to the Location Info TextView of the Item V. This should
     * launch a Share Intent passing in the location information.
     *
     * @param itemPosition    The adapter position of the Item clicked and held
     * @param restaurantClass The {@link RestaurantClass} associated with the Item clicked and held
     */
    void onLocationLongClicked(final int itemPosition, RestaurantClass restaurantClass);
}

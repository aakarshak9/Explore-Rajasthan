package com.example.aakarshak.explore.ui.parks;

import com.example.aakarshak.explore.data.local.models.ParkClass;

public interface UserActionPark {
    /**
     * Callback Method of {@link UserActionPark} invoked when
     * the user clicks on the Item V itself. This should launch
     * the website link if any.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param parkClass    The {@link ParkClass} associated with the Item clicked
     */
    void onOpenLink(final int itemPosition, ParkClass parkClass);

    /**
     * Callback Method of {@link UserActionPark} invoked when
     * the user clicks on the Map ImageButton or the Location Info TextView of the Item V.
     * This should launch any Map application passing in the location information.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param parkClass    The {@link ParkClass} associated with the Item clicked
     */
    void onOpenLocation(final int itemPosition, ParkClass parkClass);

    /**
     * Callback Method of {@link UserActionPark} invoked when
     * the user clicks and holds on to the Location Info TextView of the Item V. This should
     * launch a Share Intent passing in the location information.
     *
     * @param itemPosition The adapter position of the Item clicked and held
     * @param parkClass    The {@link ParkClass} associated with the Item clicked and held
     */
    void onLocationLongClicked(final int itemPosition, ParkClass parkClass);
}

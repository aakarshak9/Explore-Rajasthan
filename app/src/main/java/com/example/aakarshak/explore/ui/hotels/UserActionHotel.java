package com.example.aakarshak.explore.ui.hotels;

import com.example.aakarshak.explore.data.local.models.HotelsClass;

public interface UserActionHotel {
    /**
     * Callback Method of {@link UserActionHotel} invoked when
     * the user clicks on the Item V itself. This should launch
     * the website link if any.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param hotelsClass  The {@link HotelsClass} associated with the Item clicked
     */
    void onOpenLink(final int itemPosition, HotelsClass hotelsClass);

    /**
     * Callback Method of {@link UserActionHotel} invoked when
     * the user clicks on the Map ImageButton or the Location Info TextView of the Item V.
     * This should launch any Map application passing in the location information.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param hotelsClass  The {@link HotelsClass} associated with the Item clicked
     */
    void onOpenLocation(final int itemPosition, HotelsClass hotelsClass);

    /**
     * Callback Method of {@link UserActionHotel} invoked when
     * the user clicks on the Call ImageButton or the Contact Info TextView of the Item V.
     * This should launch any Dialer application passing in the Contact Number.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param hotelsClass  The {@link HotelsClass} associated with the Item clicked
     */
    void onOpenContact(final int itemPosition, HotelsClass hotelsClass);

    /**
     * Callback Method of {@link UserActionHotel} invoked when
     * the user clicks and holds on to the Contact Info TextView of the Item V. This should
     * launch a Share Intent passing in the Contact Number.
     *
     * @param itemPosition The adapter position of the Item clicked and held
     * @param hotelsClass  The {@link HotelsClass} associated with the Item clicked and held
     */
    void onContactLongClicked(final int itemPosition, HotelsClass hotelsClass);

    /**
     * Callback Method of {@link UserActionHotel} invoked when
     * the user clicks and holds on to the Location Info TextView of the Item V. This should
     * launch a Share Intent passing in the location information.
     *
     * @param itemPosition The adapter position of the Item clicked and held
     * @param hotelsClass  The {@link HotelsClass} associated with the Item clicked and held
     */
    void onLocationLongClicked(final int itemPosition, HotelsClass hotelsClass);
}

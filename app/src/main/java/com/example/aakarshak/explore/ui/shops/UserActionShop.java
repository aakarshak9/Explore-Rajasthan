package com.example.aakarshak.explore.ui.shops;

import com.example.aakarshak.explore.data.local.models.ShopClass;

public interface UserActionShop {

    /**
     * Callback Method of {@link UserActionShop} invoked when
     * the user clicks on the Item V itself. This should launch
     * the website link if any.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param shopClass    The {@link ShopClass} associated with the Item clicked
     */
    void onOpenLink(final int itemPosition, ShopClass shopClass);

    /**
     * Callback Method of {@link UserActionShop} invoked when
     * the user clicks on the Map ImageButton or the Location Info TextView of the Item V.
     * This should launch any Map application passing in the location information.
     *
     * @param itemPosition The adapter position of the Item clicked
     * @param shopClass    The {@link ShopClass} associated with the Item clicked
     */
    void onOpenLocation(final int itemPosition, ShopClass shopClass);

    /**
     * Callback Method of {@link UserActionShop} invoked when
     * the user clicks and holds on to the Location Info TextView of the Item V. This should
     * launch a Share Intent passing in the location information.
     *
     * @param itemPosition The adapter position of the Item clicked and held
     * @param shopClass    The {@link ShopClass} associated with the Item clicked and held
     */
    void onLocationLongClicked(final int itemPosition, ShopClass shopClass);
}

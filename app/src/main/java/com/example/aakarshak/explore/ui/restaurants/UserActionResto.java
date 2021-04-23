package com.example.aakarshak.explore.ui.restaurants;

import com.example.aakarshak.explore.data.local.models.RestaurantClass;

public interface UserActionResto {

    void onOpenLink(final int itemPosition, RestaurantClass restaurantClass);

    void onOpenLocation(final int itemPosition, RestaurantClass restaurantClass);

    void onOpenContact(final int itemPosition, RestaurantClass restaurantClass);

    void onContactLongClicked(final int itemPosition, RestaurantClass restaurantClass);

    void onLocationLongClicked(final int itemPosition, RestaurantClass restaurantClass);
}

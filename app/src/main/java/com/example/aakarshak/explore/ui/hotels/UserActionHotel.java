package com.example.aakarshak.explore.ui.hotels;

import com.example.aakarshak.explore.data.local.models.HotelsClass;

public interface UserActionHotel {

    void onOpenLink(final int itemPosition, HotelsClass hotelsClass);

    void onOpenLocation(final int itemPosition, HotelsClass hotelsClass);

    void onOpenContact(final int itemPosition, HotelsClass hotelsClass);

    void onContactLongClicked(final int itemPosition, HotelsClass hotelsClass);

    void onLocationLongClicked(final int itemPosition, HotelsClass hotelsClass);
}

package com.example.aakarshak.explore.ui.parks;

import com.example.aakarshak.explore.data.local.models.ParkClass;

public interface UserActionPark {

    void onOpenLink(final int itemPosition, ParkClass parkClass);

    void onOpenLocation(final int itemPosition, ParkClass parkClass);

    void onLocationLongClicked(final int itemPosition, ParkClass parkClass);
}

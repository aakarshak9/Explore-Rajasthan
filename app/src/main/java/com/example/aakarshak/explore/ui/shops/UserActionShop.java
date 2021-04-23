package com.example.aakarshak.explore.ui.shops;

import com.example.aakarshak.explore.data.local.models.ShopClass;

public interface UserActionShop {

    void onOpenLink(final int itemPosition, ShopClass shopClass);

    void onOpenLocation(final int itemPosition, ShopClass shopClass);

    void onLocationLongClicked(final int itemPosition, ShopClass shopClass);
}

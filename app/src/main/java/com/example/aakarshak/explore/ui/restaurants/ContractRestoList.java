package com.example.aakarshak.explore.ui.restaurants;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.RestaurantClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.NaviPresenterBase;

import java.util.List;

public interface ContractRestoList {

    interface V extends BaseV<PresenterBase> {

        void showProgressIndicator();

        void hideProgressIndicator();

        void loadRestaurants(List<RestaurantClass> restaurantClassList);

        void showError(@StringRes int messageId, @Nullable Object... args);

        void showNoLinkError();

        void launchWebLink(String webUrl);

        void launchMapLocation(String location);

        void dialNumber(String contactNumber);

        void launchShareContact(String contactNumber);

        void launchShareLocation(String location);
    }

    interface PresenterBase extends NaviPresenterBase {

        void updateRestaurants(List<RestaurantClass> restaurantClasses);

        void openLink(String webUrl);

        void openLocation(String location);

        void openContact(String contactNumber);

        void shareContact(String contactNumber);

        void shareLocation(String location);
    }
}

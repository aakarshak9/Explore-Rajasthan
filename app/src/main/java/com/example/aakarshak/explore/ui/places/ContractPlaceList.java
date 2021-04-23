package com.example.aakarshak.explore.ui.places;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.NaviPresenterBase;

import java.util.List;

public interface ContractPlaceList {

    interface V extends BaseV<PresenterBase> {

        void showProgressIndicator();

        void hideProgressIndicator();

        void loadPlaces(List<PlaceClass> placeClassList);

        void showError(@StringRes int messageId, @Nullable Object... args);

        void showNoLinkError();

        void launchWebLink(String webUrl);

        void launchMapLocation(String location);

        void launchShareLocation(String location);
    }

    interface PresenterBase extends NaviPresenterBase {

        void updatePlaces(@Nullable List<PlaceClass> placeClasses);

        void openLink(String webUrl);

        void openLocation(String location);

        void shareLocation(String location);
    }
}

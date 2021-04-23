package com.example.aakarshak.explore.ui.parks;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.NaviPresenterBase;

import java.util.List;

public interface ContractPlist {

    interface V extends BaseV<PresenterBase> {

        void showProgressIndicator();

        void hideProgressIndicator();

        void loadParks(List<ParkClass> parkClassList);

        void showError(@StringRes int messageId, @Nullable Object... args);

        void showNoLinkError();

        void launchWebLink(String webUrl);

        void launchMapLocation(String location);

        void launchShareLocation(String location);
    }

    interface PresenterBase extends NaviPresenterBase {

        void updateParks(List<ParkClass> parkClasses);

        void openLink(String webUrl);

        void openLocation(String location);

        void shareLocation(String location);
    }
}

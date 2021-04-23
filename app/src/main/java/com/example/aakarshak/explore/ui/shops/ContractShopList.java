package com.example.aakarshak.explore.ui.shops;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.ShopClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.NaviPresenterBase;

import java.util.List;

public interface ContractShopList {

    interface V extends BaseV<PresenterBase> {

        void showProgressIndicator();

        void hideProgressIndicator();

        void loadShops(List<ShopClass> shopClassList);

        void showError(@StringRes int messageId, @Nullable Object... args);

        void showNoLinkError();

        void launchWebLink(String webUrl);

        void launchMapLocation(String location);

        void launchShareLocation(String location);
    }

    interface PresenterBase extends NaviPresenterBase {

        void updateShops(List<ShopClass> shopClasses);

        void openLink(String webUrl);

        void openLocation(String location);

        void shareLocation(String location);
    }

}

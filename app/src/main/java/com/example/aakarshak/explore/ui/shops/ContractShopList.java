package com.example.aakarshak.explore.ui.shops;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.ShopClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.NaviPresenterBase;

import java.util.List;

public interface ContractShopList {

    /**
     * V Interface implemented by {@link ShopFragmentl}
     */
    interface V extends BaseV<PresenterBase> {
        /**
         * Method that displays the Progress indicator
         */
        void showProgressIndicator();

        /**
         * Method that hides the Progress indicator
         */
        void hideProgressIndicator();

        /**
         * Method that updates the RecyclerView's Adapter with new {@code shopClassList} data.
         *
         * @param shopClassList List of {@link ShopClass}s loaded from the Resources.
         */
        void loadShops(List<ShopClass> shopClassList);

        /**
         * Method invoked when an error is encountered during information
         * retrieval process.
         *
         * @param messageId String Resource of the error Message to be displayed
         * @param args      Variable number of arguments to replace the format specifiers
         *                  in the String resource if any
         */
        void showError(@StringRes int messageId, @Nullable Object... args);

        /**
         * Method invoked when there is no Web Page URL of the {@link ShopClass} item being clicked.
         */
        void showNoLinkError();

        /**
         * Method invoked when the user clicks on the Item V itself. This should launch
         * a browser application for the URL {@code webUrl} of the Web Page passed.
         *
         * @param webUrl String containing the URL of the Web Page
         */
        void launchWebLink(String webUrl);

        /**
         * Method invoked when the user clicks on the Map ImageButton or the Location Info
         * TextView of the Item V. This should launch any Map application
         * passing in the {@code location} information.
         *
         * @param location String containing the Location information to be sent to a Map application.
         */
        void launchMapLocation(String location);

        /**
         * Method invoked when the user clicks and holds on to the Location Info TextView of the Item V.
         * This should launch a Share Intent passing in the location information.
         *
         * @param location String containing the Location information to be shared via an Intent.
         */
        void launchShareLocation(String location);
    }

    /**
     * PresenterBase Interface implemented by {@link ShopPresenterl}
     */
    interface PresenterBase extends NaviPresenterBase {

        /**
         * Method that updates the list of {@link ShopClass} data to be shown, to the V.
         *
         * @param shopClasses The List of {@link ShopClass} data to be shown by the V.
         */
        void updateShops(List<ShopClass> shopClasses);

        /**
         * Method invoked when the user clicks on the Item V itself. This should launch
         * the {@code webUrl} link if any.
         *
         * @param webUrl String containing the URL of the Web Page to be
         *               launched in a browser application
         */
        void openLink(String webUrl);

        /**
         * Method invoked when the user clicks on the Map ImageButton or the Location Info
         * TextView of the Item V. This should launch any Map application
         * passing in the {@code location} information.
         *
         * @param location String containing the Location information to be sent to a Map application.
         */
        void openLocation(String location);

        /**
         * Method invoked when the user clicks and holds on to the Location Info TextView of the Item V.
         * This should launch a Share Intent passing in the location information.
         *
         * @param location String containing the Location information to be shared via an Intent.
         */
        void shareLocation(String location);
    }

}

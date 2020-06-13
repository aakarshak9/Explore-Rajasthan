package com.example.aakarshak.explore.ui.places;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.NaviPresenterBase;

import java.util.List;

public interface ContractPlaceList {

    /**
     * V Interface implemented by {@link PlaceFragmentl}
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
         * Method that updates the RecyclerView's Adapter with new {@code placeClassList} data.
         *
         * @param placeClassList List of {@link PlaceClass}s loaded from the Resources.
         */
        void loadPlaces(List<PlaceClass> placeClassList);

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
         * Method invoked when there is no Web Page URL of the {@link PlaceClass} item being clicked.
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
     * PresenterBase Interface implemented by {@link PlacePresenterl}
     */
    interface PresenterBase extends NaviPresenterBase {

        /**
         * Method that updates the list of {@link PlaceClass} data to be shown, to the V.
         *
         * @param placeClasses The List of {@link PlaceClass} data to be shown by the V.
         */
        void updatePlaces(@Nullable List<PlaceClass> placeClasses);

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

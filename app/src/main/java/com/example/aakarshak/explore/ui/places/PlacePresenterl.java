package com.example.aakarshak.explore.ui.places;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.ui.BaseV;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlacePresenterl implements ContractPlaceList.PresenterBase {

    //Constant used for logs
    private static final String LOG_TAG = PlacePresenterl.class.getSimpleName();

    //Instance of the App Repository
    @NonNull
    private final AppClassRepo mAppClassRepo;

    //The V Interface of this PresenterBase
    @NonNull
    private final ContractPlaceList.V mPlaceListView;

    //Boolean to avoid unnecessary downloads of the Places data
    private AtomicBoolean mPlacesLoadedBoolean = new AtomicBoolean(false);

    /**
     * Constructor of {@link PlacePresenterl}
     *
     * @param appClassRepo  Instance of {@link AppClassRepo} for accessing the data
     * @param placeListView The V Instance {@link ContractPlaceList.V} of this PresenterBase
     */
    public PlacePresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractPlaceList.V placeListView) {
        mAppClassRepo = appClassRepo;
        mPlaceListView = placeListView;

        //Registering the V with the PresenterBase
        mPlaceListView.setPresenter(this);
    }

    /**
     * Method that initiates the work of a PresenterBase which is invoked by the V
     * that implements the {@link BaseV}
     */
    @Override
    public void start() {

        //Download the list of Places to be shown when not downloaded previously
        if (mPlacesLoadedBoolean.compareAndSet(false, true)) {
            loadPlaces();
        }
    }

    /**
     * Method that downloads the list of Places to be updated to the V
     */
    private void loadPlaces() {
        //Display progress indicator
        mPlaceListView.showProgressIndicator();

        //Retrieving the Places from the Repository
        mAppClassRepo.getPlaceListEntries(R.array.place_list_entries, new IResourceClassRepo.GetResourceCallback<List<PlaceClass>>() {
            /**
             * Method invoked when the {@code results} are obtained for the data requested.
             *
             * @param results The {@code results} in the generic type passed which
             *                is a List of {@link PlaceClass} data downloaded
             */
            @Override
            public void onResults(List<PlaceClass> results) {
                //Update the Places to be shown
                updatePlaces(results);

                //Hide progress indicator
                mPlaceListView.hideProgressIndicator();
            }

            /**
             * Method invoked when the results could not be obtained for the data requested
             * due to some error.
             *
             * @param messageId The String resource of the error message
             * @param args Variable number of arguments to replace the format specifiers
             *             in the String resource if any
             */
            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mPlaceListView.hideProgressIndicator();

                //Show the error message
                mPlaceListView.showError(messageId, args);
            }
        });
    }

    /**
     * Method that updates the list of {@link PlaceClass} data to be shown, to the V.
     *
     * @param placeClasses The List of {@link PlaceClass} data to be shown by the V.
     */
    @Override
    public void updatePlaces(@Nullable List<PlaceClass> placeClasses) {
        if (placeClasses != null && placeClasses.size() > 0) {
            //When we have the list of Places

            //Submitting the list of Places to the V
            mPlaceListView.loadPlaces(placeClasses);

            //Updating the boolean to TRUE, to prevent further downloads of the same data
            mPlacesLoadedBoolean.set(true);
        }
    }

    /**
     * Method invoked when the User clicks on the Refresh Menu button
     * shown by the {@link com.example.aakarshak.explore.ui.MainActivity}
     */
    @Override
    public void onRefreshMenuClicked() {
        loadPlaces();
    }

    /**
     * Method invoked when the user clicks on the Item V itself. This should launch
     * the {@code webUrl} link if any.
     *
     * @param webUrl String containing the URL of the Web Page to be
     *               launched in a browser application
     */
    @Override
    public void openLink(String webUrl) {
        if (TextUtils.isEmpty(webUrl)) {
            //When we do not have the URL, show an error message
            mPlaceListView.showNoLinkError();
        } else {
            //When we have the URL, delegate to the V to launch the Web page
            mPlaceListView.launchWebLink(webUrl);
        }
    }

    /**
     * Method invoked when the user clicks on the Map ImageButton or the Location Info
     * TextView of the Item V. This should launch any Map application
     * passing in the {@code location} information.
     *
     * @param location String containing the Location information to be sent to a Map application.
     */
    @Override
    public void openLocation(String location) {
        //Delegating to the V to launch the Map application for the location address
        mPlaceListView.launchMapLocation(location);
    }

    /**
     * Method invoked when the user clicks and holds on to the Location Info TextView of the Item V.
     * This should launch a Share Intent passing in the location information.
     *
     * @param location String containing the Location information to be shared via an Intent.
     */
    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mPlaceListView.launchShareLocation(location);
    }

}

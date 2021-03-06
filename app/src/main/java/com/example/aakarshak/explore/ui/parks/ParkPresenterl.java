package com.example.aakarshak.explore.ui.parks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.MainActivity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParkPresenterl implements ContractPlist.PresenterBase {

    //Constant used for Logs
    private static final String LOG_TAG = ParkPresenterl.class.getSimpleName();

    //Instance of the App Repository
    @NonNull
    private final AppClassRepo mAppClassRepo;

    //The V Interface of this PresenterBase
    @NonNull
    private final ContractPlist.V mParkListView;

    //Boolean to avoid unnecessary downloads of the Parks data
    private AtomicBoolean mParksLoadedBoolean = new AtomicBoolean(false);

    /**
     * Constructor of {@link ParkPresenterl}
     *
     * @param appClassRepo Instance of {@link AppClassRepo} for accessing the data
     * @param parkListView The V Instance {@link ContractPlist.V} of this PresenterBase
     */
    public ParkPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractPlist.V parkListView) {
        mAppClassRepo = appClassRepo;
        mParkListView = parkListView;

        //Registering the V with the PresenterBase
        mParkListView.setPresenter(this);
    }

    /**
     * Method that initiates the work of a PresenterBase which is invoked by the V
     * that implements the {@link BaseV}
     */
    @Override
    public void start() {

        //Download the list of Parks to be shown when not downloaded previously
        if (mParksLoadedBoolean.compareAndSet(false, true)) {
            loadParks();
        }
    }

    /**
     * Method that downloads the list of Parks to be updated to the V
     */
    private void loadParks() {
        //Display progress indicator
        mParkListView.showProgressIndicator();

        //Retrieving the Parks from the Repository
        mAppClassRepo.getParkListEntries(R.array.park_list_entries, new IResourceClassRepo.GetResourceCallback<List<ParkClass>>() {
            /**
             * Method invoked when the {@code results} are obtained for the data requested.
             *
             * @param results The {@code results} in the generic type passed
             *                which is a List of {@link ParkClass} data downloaded
             */
            @Override
            public void onResults(List<ParkClass> results) {
                //Update the Parks to be shown
                updateParks(results);

                //Hide progress indicator
                mParkListView.hideProgressIndicator();
            }

            /**
             * Method invoked when the results could not be obtained for the data requested
             * due to some error.
             *
             * @param messageId The String resource of the error message
             * @param args      Variable number of arguments to replace the format specifiers
             */
            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mParkListView.hideProgressIndicator();

                //Show the error message
                mParkListView.showError(messageId, args);
            }
        });
    }

    /**
     * Method that updates the list of {@link ParkClass} data to be shown, to the V.
     *
     * @param parkClasses The List of {@link ParkClass} data to be shown by the V.
     */
    @Override
    public void updateParks(List<ParkClass> parkClasses) {
        if (parkClasses != null && parkClasses.size() > 0) {
            //When we have the list of Parks

            //Submitting the list of Parks to the V
            mParkListView.loadParks(parkClasses);

            //Updating the boolean to TRUE, to prevent further downloads of the same data
            mParksLoadedBoolean.set(true);
        }
    }

    /**
     * Method invoked when the User clicks on the Refresh Menu button
     * shown by the {@link MainActivity}
     */
    @Override
    public void onRefreshMenuClicked() {
        loadParks();
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
            mParkListView.showNoLinkError();
        } else {
            //When we have the URL, delegate to the V to launch the Web page
            mParkListView.launchWebLink(webUrl);
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
        mParkListView.launchMapLocation(location);
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
        mParkListView.launchShareLocation(location);
    }

}

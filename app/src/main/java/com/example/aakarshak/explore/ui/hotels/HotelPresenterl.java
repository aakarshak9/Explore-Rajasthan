package com.example.aakarshak.explore.ui.hotels;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.HotelsClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.MainActivity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HotelPresenterl implements ContractHlist.PresenterBase {

    //Constant used for Logs
    private static final String LOG_TAG = HotelPresenterl.class.getSimpleName();

    //Instance of the App Repository
    @NonNull
    private final AppClassRepo mAppClassRepo;

    //The V Interface of this PresenterBase
    @NonNull
    private final ContractHlist.V mHotelListView;

    //Boolean to avoid unnecessary downloads of the Hotels data
    private AtomicBoolean mHotelsLoadedBoolean = new AtomicBoolean(false);

    /**
     * Constructor of {@link HotelPresenterl}
     *
     * @param appClassRepo  Instance of {@link AppClassRepo} for accessing the data
     * @param hotelListView The V Instance {@link ContractHlist.V} of this PresenterBase
     */
    public HotelPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractHlist.V hotelListView) {
        mAppClassRepo = appClassRepo;
        mHotelListView = hotelListView;

        //Registering the V with the PresenterBase
        mHotelListView.setPresenter(this);
    }

    /**
     * Method that initiates the work of a PresenterBase which is invoked by the V
     * that implements the {@link BaseV}
     */
    @Override
    public void start() {

        //Download the list of Hotels to be shown when not downloaded previously
        if (mHotelsLoadedBoolean.compareAndSet(false, true)) {
            loadHotels();
        }
    }

    /**
     * Method that downloads the list of Hotels to be updated to the V
     */
    private void loadHotels() {
        //Display progress indicator
        mHotelListView.showProgressIndicator();

        //Retrieving the Hotels from the Repository
        mAppClassRepo.getHotelListEntries(R.array.hotel_list_entries, new IResourceClassRepo.GetResourceCallback<List<HotelsClass>>() {
            /**
             * Method invoked when the {@code results} are obtained for the data requested.
             *
             * @param results The {@code results} in the generic type passed
             *                which is a List of {@link HotelsClass} data downloaded
             */
            @Override
            public void onResults(List<HotelsClass> results) {
                //Update the Hotels to be shown
                updateHotels(results);

                //Hide progress indicator
                mHotelListView.hideProgressIndicator();
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
                mHotelListView.hideProgressIndicator();

                //Show the error message
                mHotelListView.showError(messageId, args);
            }
        });

    }

    /**
     * Method that updates the list of {@link HotelsClass} data to be shown, to the V.
     *
     * @param hotelsClasses The List of {@link HotelsClass} data to be shown by the V.
     */
    @Override
    public void updateHotels(List<HotelsClass> hotelsClasses) {
        if (hotelsClasses != null && hotelsClasses.size() > 0) {
            //When we have the list of Hotels

            //Submitting the list of Hotels to the V
            mHotelListView.loadHotels(hotelsClasses);

            //Updating the boolean to TRUE, to prevent further downloads of the same data
            mHotelsLoadedBoolean.set(true);
        }
    }

    /**
     * Method invoked when the User clicks on the Refresh Menu button
     * shown by the {@link MainActivity}
     */
    @Override
    public void onRefreshMenuClicked() {
        loadHotels();
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
            mHotelListView.showNoLinkError();
        } else {
            //When we have the URL, delegate to the V to launch the Web page
            mHotelListView.launchWebLink(webUrl);
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
        mHotelListView.launchMapLocation(location);
    }

    /**
     * Method invoked when the user clicks on the Call ImageButton or the Contact Info
     * TextView of the Item V. This should launch any Dialer application
     * passing in the Contact Number {@code contactNumber}.
     *
     * @param contactNumber String containing the Contact Number information to be sent
     *                      to a Dialer application.
     */
    @Override
    public void openContact(String contactNumber) {
        //Delegating to the V to launch the Phone Dialer
        mHotelListView.dialNumber(contactNumber);
    }

    /**
     * Method invoked when the user clicks and holds on to the Contact Info TextView
     * of the Item V. This should launch a Share Intent passing
     * in the Contact Number.
     *
     * @param contactNumber String containing the Contact Number information to be
     *                      shared via an Intent.
     */
    @Override
    public void shareContact(String contactNumber) {
        if (!TextUtils.isEmpty(contactNumber)) {
            //Delegating to the V to share the Contact information when available
            mHotelListView.launchShareContact(contactNumber);
        }
    }

    /**
     * Method invoked when the user clicks and holds on to the Location Info
     * TextView of the Item V. This should launch a Share Intent passing
     * in the location information.
     *
     * @param location String containing the Location information to be shared via an Intent.
     */
    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mHotelListView.launchShareLocation(location);
    }


}

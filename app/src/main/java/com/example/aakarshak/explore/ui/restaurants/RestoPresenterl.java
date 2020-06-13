package com.example.aakarshak.explore.ui.restaurants;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.RestaurantClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.MainActivity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RestoPresenterl implements ContractRestoList.PresenterBase {

    //Constant used for logs
    private static final String LOG_TAG = RestoPresenterl.class.getSimpleName();

    //Instance of the App Repository
    @NonNull
    private final AppClassRepo mAppClassRepo;

    //The V Interface of this PresenterBase
    @NonNull
    private final ContractRestoList.V mRestaurantListView;

    //Boolean to avoid unnecessary downloads of the Restaurants data
    private AtomicBoolean mRestaurantsLoadedBoolean = new AtomicBoolean(false);

    /**
     * Constructor of {@link RestoPresenterl}
     *
     * @param appClassRepo       Instance of {@link AppClassRepo} for accessing the data
     * @param restaurantListView The V Instance {@link ContractRestoList.V} of this PresenterBase
     */
    public RestoPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractRestoList.V restaurantListView) {
        mAppClassRepo = appClassRepo;
        mRestaurantListView = restaurantListView;

        //Registering the V with the PresenterBase
        mRestaurantListView.setPresenter(this);
    }

    /**
     * Method that initiates the work of a PresenterBase which is invoked by the V
     * that implements the {@link BaseV}
     */
    @Override
    public void start() {

        //Download the list of Restaurants to be shown when not downloaded previously
        if (mRestaurantsLoadedBoolean.compareAndSet(false, true)) {
            loadRestaurants();
        }
    }

    /**
     * Method that downloads the list of Restaurants to be updated to the V
     */
    private void loadRestaurants() {
        //Display progress indicator
        mRestaurantListView.showProgressIndicator();

        //Retrieving the Restaurants from the Repository
        mAppClassRepo.getRestaurantListEntries(R.array.restaurant_list_entries, new IResourceClassRepo.GetResourceCallback<List<RestaurantClass>>() {
            /**
             * Method invoked when the {@code results} are obtained for the data requested.
             *
             * @param results The {@code results} in the generic type passed which
             *                is a List of {@link RestaurantClass} data downloaded
             */
            @Override
            public void onResults(List<RestaurantClass> results) {
                //Update the Restaurants to be shown
                updateRestaurants(results);

                //Hide progress indicator
                mRestaurantListView.hideProgressIndicator();
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
                mRestaurantListView.hideProgressIndicator();

                //Show the error message
                mRestaurantListView.showError(messageId, args);
            }
        });
    }

    /**
     * Method that updates the list of {@link RestaurantClass} data to be shown, to the V.
     *
     * @param restaurantClasses The List of {@link RestaurantClass} data to be shown by the V.
     */
    @Override
    public void updateRestaurants(List<RestaurantClass> restaurantClasses) {
        if (restaurantClasses != null && restaurantClasses.size() > 0) {
            //When we have the list of Restaurants

            //Submitting the list of Restaurants to the V
            mRestaurantListView.loadRestaurants(restaurantClasses);

            //Updating the boolean to TRUE, to prevent further downloads of the same data
            mRestaurantsLoadedBoolean.set(true);
        }
    }

    /**
     * Method invoked when the User clicks on the Refresh Menu button
     * shown by the {@link MainActivity}
     */
    @Override
    public void onRefreshMenuClicked() {
        loadRestaurants();
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
            mRestaurantListView.showNoLinkError();
        } else {
            //When we have the URL, delegate to the V to launch the Web page
            mRestaurantListView.launchWebLink(webUrl);
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
        mRestaurantListView.launchMapLocation(location);
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
        mRestaurantListView.dialNumber(contactNumber);
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
            mRestaurantListView.launchShareContact(contactNumber);
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
        mRestaurantListView.launchShareLocation(location);
    }

}

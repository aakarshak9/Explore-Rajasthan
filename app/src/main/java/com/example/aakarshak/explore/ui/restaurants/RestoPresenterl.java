package com.example.aakarshak.explore.ui.restaurants;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.RestaurantClass;

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
    private final AtomicBoolean mRestaurantsLoadedBoolean = new AtomicBoolean(false);

    public RestoPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractRestoList.V restaurantListView) {
        mAppClassRepo = appClassRepo;
        mRestaurantListView = restaurantListView;

        //Registering the V with the PresenterBase
        mRestaurantListView.setPresenter(this);
    }

    @Override
    public void start() {

        //Download the list of Restaurants to be shown when not downloaded previously
        if (mRestaurantsLoadedBoolean.compareAndSet(false, true)) {
            loadRestaurants();
        }
    }

    private void loadRestaurants() {
        //Display progress indicator
        mRestaurantListView.showProgressIndicator();

        //Retrieving the Restaurants from the Repository
        mAppClassRepo.getRestaurantListEntries(R.array.restaurant_list_entries, new IResourceClassRepo.GetResourceCallback<List<RestaurantClass>>() {

            @Override
            public void onResults(List<RestaurantClass> results) {
                //Update the Restaurants to be shown
                updateRestaurants(results);

                //Hide progress indicator
                mRestaurantListView.hideProgressIndicator();
            }

            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mRestaurantListView.hideProgressIndicator();

                //Show the error message
                mRestaurantListView.showError(messageId, args);
            }
        });
    }

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

    @Override
    public void onRefreshMenuClicked() {
        loadRestaurants();
    }

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

    @Override
    public void openLocation(String location) {
        //Delegating to the V to launch the Map application for the location address
        mRestaurantListView.launchMapLocation(location);
    }

    @Override
    public void openContact(String contactNumber) {
        //Delegating to the V to launch the Phone Dialer
        mRestaurantListView.dialNumber(contactNumber);
    }

    @Override
    public void shareContact(String contactNumber) {
        if (!TextUtils.isEmpty(contactNumber)) {
            //Delegating to the V to share the Contact information when available
            mRestaurantListView.launchShareContact(contactNumber);
        }
    }

    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mRestaurantListView.launchShareLocation(location);
    }

}

package com.example.aakarshak.explore.ui.places;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.PlaceClass;

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
    private final AtomicBoolean mPlacesLoadedBoolean = new AtomicBoolean(false);

    public PlacePresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractPlaceList.V placeListView) {
        mAppClassRepo = appClassRepo;
        mPlaceListView = placeListView;

        //Registering the V with the PresenterBase
        mPlaceListView.setPresenter(this);
    }

    @Override
    public void start() {

        //Download the list of Places to be shown when not downloaded previously
        if (mPlacesLoadedBoolean.compareAndSet(false, true)) {
            loadPlaces();
        }
    }

    private void loadPlaces() {
        //Display progress indicator
        mPlaceListView.showProgressIndicator();

        //Retrieving the Places from the Repository
        mAppClassRepo.getPlaceListEntries(R.array.place_list_entries, new IResourceClassRepo.GetResourceCallback<List<PlaceClass>>() {

            @Override
            public void onResults(List<PlaceClass> results) {
                //Update the Places to be shown
                updatePlaces(results);

                //Hide progress indicator
                mPlaceListView.hideProgressIndicator();
            }

            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mPlaceListView.hideProgressIndicator();

                //Show the error message
                mPlaceListView.showError(messageId, args);
            }
        });
    }

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

    @Override
    public void onRefreshMenuClicked() {
        loadPlaces();
    }

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

    @Override
    public void openLocation(String location) {
        //Delegating to the V to launch the Map application for the location address
        mPlaceListView.launchMapLocation(location);
    }

    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mPlaceListView.launchShareLocation(location);
    }

}

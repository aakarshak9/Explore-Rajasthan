package com.example.aakarshak.explore.ui.hotels;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.HotelsClass;

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
    private final AtomicBoolean mHotelsLoadedBoolean = new AtomicBoolean(false);


    public HotelPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractHlist.V hotelListView) {
        mAppClassRepo = appClassRepo;
        mHotelListView = hotelListView;

        //Registering the V with the PresenterBase
        mHotelListView.setPresenter(this);
    }

    @Override
    public void start() {

        //Download the list of Hotels to be shown when not downloaded previously
        if (mHotelsLoadedBoolean.compareAndSet(false, true)) {
            loadHotels();
        }
    }

    private void loadHotels() {
        //Display progress indicator
        mHotelListView.showProgressIndicator();

        //Retrieving the Hotels from the Repository
        mAppClassRepo.getHotelListEntries(R.array.hotel_list_entries, new IResourceClassRepo.GetResourceCallback<List<HotelsClass>>() {

            @Override
            public void onResults(List<HotelsClass> results) {
                //Update the Hotels to be shown
                updateHotels(results);

                //Hide progress indicator
                mHotelListView.hideProgressIndicator();
            }

            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mHotelListView.hideProgressIndicator();

                //Show the error message
                mHotelListView.showError(messageId, args);
            }
        });

    }

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

    @Override
    public void onRefreshMenuClicked() {
        loadHotels();
    }

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

    @Override
    public void openLocation(String location) {
        //Delegating to the V to launch the Map application for the location address
        mHotelListView.launchMapLocation(location);
    }

    @Override
    public void openContact(String contactNumber) {
        //Delegating to the V to launch the Phone Dialer
        mHotelListView.dialNumber(contactNumber);
    }

    @Override
    public void shareContact(String contactNumber) {
        if (!TextUtils.isEmpty(contactNumber)) {
            //Delegating to the V to share the Contact information when available
            mHotelListView.launchShareContact(contactNumber);
        }
    }

    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mHotelListView.launchShareLocation(location);
    }


}

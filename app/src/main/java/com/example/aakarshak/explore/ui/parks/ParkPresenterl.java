package com.example.aakarshak.explore.ui.parks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private final AtomicBoolean mParksLoadedBoolean = new AtomicBoolean(false);

    public ParkPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractPlist.V parkListView) {
        mAppClassRepo = appClassRepo;
        mParkListView = parkListView;

        //Registering the V with the PresenterBase
        mParkListView.setPresenter(this);
    }

    @Override
    public void start() {

        //Download the list of Parks to be shown when not downloaded previously
        if (mParksLoadedBoolean.compareAndSet(false, true)) {
            loadParks();
        }
    }

    private void loadParks() {
        //Display progress indicator
        mParkListView.showProgressIndicator();

        //Retrieving the Parks from the Repository
        mAppClassRepo.getParkListEntries(R.array.park_list_entries, new IResourceClassRepo.GetResourceCallback<List<ParkClass>>() {

            @Override
            public void onResults(List<ParkClass> results) {
                //Update the Parks to be shown
                updateParks(results);

                //Hide progress indicator
                mParkListView.hideProgressIndicator();
            }

            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mParkListView.hideProgressIndicator();

                //Show the error message
                mParkListView.showError(messageId, args);
            }
        });
    }


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

    @Override
    public void onRefreshMenuClicked() {
        loadParks();
    }

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

    @Override
    public void openLocation(String location) {
        //Delegating to the V to launch the Map application for the location address
        mParkListView.launchMapLocation(location);
    }

    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mParkListView.launchShareLocation(location);
    }

}

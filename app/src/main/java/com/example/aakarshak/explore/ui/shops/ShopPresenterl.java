package com.example.aakarshak.explore.ui.shops;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.ShopClass;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ShopPresenterl implements ContractShopList.PresenterBase {

    //Constant used for Logs
    private static final String LOG_TAG = ShopPresenterl.class.getSimpleName();

    //Instance of the App Repository
    @NonNull
    private final AppClassRepo mAppClassRepo;

    //The V Interface of this PresenterBase
    @NonNull
    private final ContractShopList.V mShopListView;

    //Boolean to avoid unnecessary downloads of the Shops data
    private final AtomicBoolean mShopsLoadedBoolean = new AtomicBoolean(false);

    public ShopPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractShopList.V shopListView) {
        mAppClassRepo = appClassRepo;
        mShopListView = shopListView;

        //Registering the V with the PresenterBase
        mShopListView.setPresenter(this);
    }

    @Override
    public void start() {

        //Download the list of Shops to be shown when not downloaded previously
        if (mShopsLoadedBoolean.compareAndSet(false, true)) {
            loadShops();
        }
    }

    private void loadShops() {
        //Display progress indicator
        mShopListView.showProgressIndicator();

        //Retrieving the Shops from the Repository
        mAppClassRepo.getShopListEntries(R.array.shop_list_entries, new IResourceClassRepo.GetResourceCallback<List<ShopClass>>() {

            @Override
            public void onResults(List<ShopClass> results) {
                //Update the Shops to be shown
                updateShops(results);

                //Hide progress indicator
                mShopListView.hideProgressIndicator();
            }

            @Override
            public void onFailure(int messageId, @Nullable Object... args) {
                //Hide progress indicator
                mShopListView.hideProgressIndicator();

                //Show the error message
                mShopListView.showError(messageId, args);
            }
        });
    }

    @Override
    public void updateShops(List<ShopClass> shopClasses) {
        if (shopClasses != null && shopClasses.size() > 0) {
            //When we have the list of Shops

            //Submitting the list of Shops to the V
            mShopListView.loadShops(shopClasses);

            //Updating the boolean to TRUE, to prevent further downloads of the same data
            mShopsLoadedBoolean.set(true);
        }
    }

    @Override
    public void onRefreshMenuClicked() {
        loadShops();
    }

    @Override
    public void openLink(String webUrl) {
        if (TextUtils.isEmpty(webUrl)) {
            //When we do not have the URL, show an error message
            mShopListView.showNoLinkError();
        } else {
            //When we have the URL, delegate to the V to launch the Web page
            mShopListView.launchWebLink(webUrl);
        }
    }

    @Override
    public void openLocation(String location) {
        //Delegating to the V to launch the Map application for the location address
        mShopListView.launchMapLocation(location);
    }

    @Override
    public void shareLocation(String location) {
        //Delegating to the V to share the location information
        mShopListView.launchShareLocation(location);
    }


}

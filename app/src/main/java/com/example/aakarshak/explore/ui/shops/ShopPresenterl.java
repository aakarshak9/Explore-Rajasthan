package com.example.aakarshak.explore.ui.shops;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.ShopClass;
import com.example.aakarshak.explore.ui.BaseV;
import com.example.aakarshak.explore.ui.MainActivity;

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
    private AtomicBoolean mShopsLoadedBoolean = new AtomicBoolean(false);

    /**
     * Constructor of {@link ShopPresenterl}
     *
     * @param appClassRepo Instance of {@link AppClassRepo} for accessing the data
     * @param shopListView The V Instance {@link ContractShopList.V} of this PresenterBase
     */
    public ShopPresenterl(@NonNull AppClassRepo appClassRepo, @NonNull ContractShopList.V shopListView) {
        mAppClassRepo = appClassRepo;
        mShopListView = shopListView;

        //Registering the V with the PresenterBase
        mShopListView.setPresenter(this);
    }

    /**
     * Method that initiates the work of a PresenterBase which is invoked by the V
     * that implements the {@link BaseV}
     */
    @Override
    public void start() {

        //Download the list of Shops to be shown when not downloaded previously
        if (mShopsLoadedBoolean.compareAndSet(false, true)) {
            loadShops();
        }
    }

    /**
     * Method that downloads the list of Shops to be updated to the V
     */
    private void loadShops() {
        //Display progress indicator
        mShopListView.showProgressIndicator();

        //Retrieving the Shops from the Repository
        mAppClassRepo.getShopListEntries(R.array.shop_list_entries, new IResourceClassRepo.GetResourceCallback<List<ShopClass>>() {
            /**
             * Method invoked when the {@code results} are obtained for the data requested.
             *
             * @param results The {@code results} in the generic type passed which
             *                is a List of {@link ShopClass} data downloaded
             */
            @Override
            public void onResults(List<ShopClass> results) {
                //Update the Shops to be shown
                updateShops(results);

                //Hide progress indicator
                mShopListView.hideProgressIndicator();
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
                mShopListView.hideProgressIndicator();

                //Show the error message
                mShopListView.showError(messageId, args);
            }
        });
    }

    /**
     * Method that updates the list of {@link ShopClass} data to be shown, to the V.
     *
     * @param shopClasses The List of {@link ShopClass} data to be shown by the V.
     */
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

    /**
     * Method invoked when the User clicks on the Refresh Menu button
     * shown by the {@link MainActivity}
     */
    @Override
    public void onRefreshMenuClicked() {
        loadShops();
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
            mShopListView.showNoLinkError();
        } else {
            //When we have the URL, delegate to the V to launch the Web page
            mShopListView.launchWebLink(webUrl);
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
        mShopListView.launchMapLocation(location);
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
        mShopListView.launchShareLocation(location);
    }


}

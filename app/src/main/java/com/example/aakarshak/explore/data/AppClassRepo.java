package com.example.aakarshak.explore.data;

import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;

import com.example.aakarshak.explore.data.local.models.HotelsClass;
import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.data.local.models.RestaurantClass;
import com.example.aakarshak.explore.data.local.models.ShopClass;

import java.util.List;

public class AppClassRepo implements IResourceClassRepo {

    //Constant used for logs
    private static final String LOG_TAG = AppClassRepo.class.getSimpleName();

    //Singleton instance of AppClassRepo
    private static volatile AppClassRepo INSTANCE;

    //Instance of IResourceClassRepo to communicate with Resources
    private final IResourceClassRepo mResourceRepository;

    /**
     * Private Constructor of {@link AppClassRepo}
     *
     * @param resourceRepository Instance of {@link IResourceClassRepo} to communicate with {@link android.content.res.Resources}
     */
    private AppClassRepo(@NonNull IResourceClassRepo resourceRepository) {
        mResourceRepository = resourceRepository;
    }

    /**
     * Singleton Constructor that creates a single instance of {@link AppClassRepo}
     *
     * @param resourceRepository Instance of {@link IResourceClassRepo} to communicate with {@link android.content.res.Resources}
     * @return New or existing instance of {@link AppClassRepo}
     */
    public static AppClassRepo getInstance(@NonNull IResourceClassRepo resourceRepository) {
        if (INSTANCE == null) {
            //When instance is not available
            synchronized (AppClassRepo.class) {
                //Apply lock and check for the instance again
                if (INSTANCE == null) {
                    //When there is no instance, create a new one
                    INSTANCE = new AppClassRepo(resourceRepository);
                }
            }
        }
        //Returning the instance of AppClassRepo
        return INSTANCE;
    }

    /**
     * Method that retrieves the List of {@link PlaceClass} data for the PlaceClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         PlaceClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    @Override
    public void getPlaceListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<PlaceClass>> resourceCallback) {
        mResourceRepository.getPlaceListEntries(arrayResId, resourceCallback);
    }

    /**
     * Method that retrieves the List of {@link ParkClass} data for the ParkClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         ParkClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    @Override
    public void getParkListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ParkClass>> resourceCallback) {
        mResourceRepository.getParkListEntries(arrayResId, resourceCallback);
    }

    /**
     * Method that retrieves the List of {@link HotelsClass} data for the HotelsClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         HotelsClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    @Override
    public void getHotelListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<HotelsClass>> resourceCallback) {
        mResourceRepository.getHotelListEntries(arrayResId, resourceCallback);
    }

    /**
     * Method that retrieves the List of {@link RestaurantClass} data for the RestaurantClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         RestaurantClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    @Override
    public void getRestaurantListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<RestaurantClass>> resourceCallback) {
        mResourceRepository.getRestaurantListEntries(arrayResId, resourceCallback);
    }

    /**
     * Method that retrieves the List of {@link ShopClass} data for the ShopClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         ShopClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    @Override
    public void getShopListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ShopClass>> resourceCallback) {
        mResourceRepository.getShopListEntries(arrayResId, resourceCallback);
    }
}

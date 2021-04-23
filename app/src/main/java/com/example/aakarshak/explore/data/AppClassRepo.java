package com.example.aakarshak.explore.data;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;

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

    private AppClassRepo(@NonNull IResourceClassRepo resourceRepository) {
        mResourceRepository = resourceRepository;
    }

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

    @Override
    public void getPlaceListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<PlaceClass>> resourceCallback) {
        mResourceRepository.getPlaceListEntries(arrayResId, resourceCallback);
    }

    @Override
    public void getParkListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ParkClass>> resourceCallback) {
        mResourceRepository.getParkListEntries(arrayResId, resourceCallback);
    }

    @Override
    public void getHotelListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<HotelsClass>> resourceCallback) {
        mResourceRepository.getHotelListEntries(arrayResId, resourceCallback);
    }

    @Override
    public void getRestaurantListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<RestaurantClass>> resourceCallback) {
        mResourceRepository.getRestaurantListEntries(arrayResId, resourceCallback);
    }

    @Override
    public void getShopListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ShopClass>> resourceCallback) {
        mResourceRepository.getShopListEntries(arrayResId, resourceCallback);
    }
}

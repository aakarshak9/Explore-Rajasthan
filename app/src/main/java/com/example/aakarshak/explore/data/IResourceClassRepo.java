package com.example.aakarshak.explore.data;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.HotelsClass;
import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.data.local.models.RestaurantClass;
import com.example.aakarshak.explore.data.local.models.ShopClass;

import java.util.List;

public interface IResourceClassRepo {

    void getPlaceListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<PlaceClass>> resourceCallback);

    void getParkListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ParkClass>> resourceCallback);

    void getHotelListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<HotelsClass>> resourceCallback);

    void getRestaurantListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<RestaurantClass>> resourceCallback);

    void getShopListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ShopClass>> resourceCallback);

    interface GetResourceCallback<T> {
        void onResults(T results);

        void onFailure(@StringRes int messageId, @Nullable Object... args);
    }
}

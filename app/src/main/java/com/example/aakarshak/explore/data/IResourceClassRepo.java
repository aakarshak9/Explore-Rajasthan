package com.example.aakarshak.explore.data;

import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.example.aakarshak.explore.data.local.models.HotelsClass;
import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.data.local.models.RestaurantClass;
import com.example.aakarshak.explore.data.local.models.ShopClass;

import java.util.List;

public interface IResourceClassRepo {

    /**
     * Method that retrieves the List of {@link PlaceClass} data for the PlaceClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         PlaceClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    void getPlaceListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<PlaceClass>> resourceCallback);

    /**
     * Method that retrieves the List of {@link ParkClass} data for the ParkClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         ParkClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    void getParkListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ParkClass>> resourceCallback);

    /**
     * Method that retrieves the List of {@link HotelsClass} data for the HotelsClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         HotelsClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    void getHotelListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<HotelsClass>> resourceCallback);

    /**
     * Method that retrieves the List of {@link RestaurantClass} data for the RestaurantClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         RestaurantClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    void getRestaurantListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<RestaurantClass>> resourceCallback);

    /**
     * Method that retrieves the List of {@link ShopClass} data for the ShopClass list entries
     * tracked by the {@code arrayResId}.
     *
     * @param arrayResId       The Integer Id of the Array resource that tracks the
     *                         ShopClass list entries to be shown.
     * @param resourceCallback The Callback to be implemented by the caller to receive the result.
     */
    void getShopListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ShopClass>> resourceCallback);

    /**
     * Callback interface for Resource data requests.
     *
     * @param <T> The type of the results expected for the data requested.
     */
    interface GetResourceCallback<T> {
        /**
         * Method invoked when the {@code results} are obtained for the data requested.
         *
         * @param results The {@code results} in the generic type passed.
         */
        void onResults(T results);

        /**
         * Method invoked when the results could not be obtained for the data requested
         * due to some error.
         *
         * @param messageId The String resource of the error message
         * @param args      Variable number of arguments to replace the format specifiers
         *                  in the String resource if any
         */
        void onFailure(@StringRes int messageId, @Nullable Object... args);
    }
}

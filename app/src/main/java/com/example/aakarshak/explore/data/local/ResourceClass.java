package com.example.aakarshak.explore.data.local;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.data.IResourceClassRepo;
import com.example.aakarshak.explore.data.local.models.HotelsClass;
import com.example.aakarshak.explore.data.local.models.ParkClass;
import com.example.aakarshak.explore.data.local.models.PlaceClass;
import com.example.aakarshak.explore.data.local.models.RestaurantClass;
import com.example.aakarshak.explore.data.local.models.ShopClass;
import com.example.aakarshak.explore.utils.Execute;
import com.example.aakarshak.explore.utils.ImageUtility;

import java.util.ArrayList;
import java.util.List;

public class ResourceClass implements IResourceClassRepo {

    //Constant used for logs
    private static final String LOG_TAG = ResourceClass.class.getSimpleName();

    //Singleton instance of ResourceClass
    private static volatile ResourceClass INSTANCE;

    //The name of this application's package
    private final String mAppPackageName;

    //Resources Instance
    private final Resources mResources;

    //Execute instance for threading requests
    private final Execute mExecute;

    private ResourceClass(@NonNull String packageName, @NonNull Resources resources, @NonNull Execute execute) {
        mAppPackageName = packageName;
        mResources = resources;
        mExecute = execute;
    }

    public static ResourceClass getInstance(@NonNull String packageName, @NonNull Resources resources, @NonNull Execute execute) {
        if (INSTANCE == null) {
            //When instance is not available
            synchronized (ResourceClass.class) {
                //Apply lock and check for the instance again
                if (INSTANCE == null) {
                    //When there is no instance, create a new one
                    INSTANCE = new ResourceClass(packageName, resources, execute);
                }
            }
        }
        //Returning the instance of ResourceClass
        return INSTANCE;
    }

    @Override
    public void getPlaceListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<PlaceClass>> resourceCallback) {
        //Executing on the Disk Thread
        mExecute.getDiskIO().execute(() -> {
            //Stores an Array of Places to be read
            TypedArray typedArrayPlaces = null;
            try {
                //Obtaining the Typed Array of Places
                typedArrayPlaces = mResources.obtainTypedArray(arrayResId);
                //The Number of Places to load
                int noOfPlaces = typedArrayPlaces.length();
                if (noOfPlaces > 0) {
                    //When we have some Places to be loaded

                    //Initializing an ArrayList of PlaceClass data to store the PlaceClass information
                    //for all the Places read from the Typed Array
                    ArrayList<PlaceClass> placeClassList = new ArrayList<>();
                    //Iterating over the Typed Array to build the list
                    for (int index = 0; index < noOfPlaces; index++) {
                        //Getting the Resource ID of the PlaceClass Array resource
                        int placeResourceId = typedArrayPlaces.getResourceId(index, 0);
                        if (placeResourceId > 0) {
                            //When the resource read is valid

                            //Obtain the String Array for the resource which contains the data for the PlaceClass
                            String[] placeStringArray = mResources.getStringArray(placeResourceId);
                            //Read the Name
                            String name = placeStringArray[PlaceClass.Contract.NAME_INDEX].trim();
                            //Read the Rating
                            float rating = Float.parseFloat(placeStringArray[PlaceClass.Contract.RATING_INDEX]);
                            //Read the PlaceClass Types
                            String types = placeStringArray[PlaceClass.Contract.TYPES_INDEX].trim();
                            //Read the drawable resource Id for the PlaceClass Type
                            int typeImageResId = ImageUtility.findDrawableResourceId(
                                    placeStringArray[PlaceClass.Contract.TYPE_ICON_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Read the drawable resource Id for the PlaceClass Photo
                            int photoResId = ImageUtility.findDrawableResourceId(
                                    placeStringArray[PlaceClass.Contract.PHOTO_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Construct a Bitmap for the PlaceClass Photo and extract the Palette
                            Bitmap photoBitmap = BitmapFactory.decodeResource(mResources, photoResId);
                            Palette.Swatch photoSwatch = ImageUtility.extractVibrantSwatch(photoBitmap);
                            //Reading the Access Timings
                            String accessTimeInfo = placeStringArray[PlaceClass.Contract.ACCESS_TIME_INDEX].trim();
                            //Read the Entry Fee
                            String entryFeeInfo = placeStringArray[PlaceClass.Contract.ENTRY_FEE_INDEX].trim();
                            //Read the Location
                            String location = placeStringArray[PlaceClass.Contract.LOCATION_INDEX].trim();
                            //Read the Description
                            String description = placeStringArray[PlaceClass.Contract.DESCRIPTION_INDEX];
                            //Read the Website
                            String website = placeStringArray[PlaceClass.Contract.WEBSITE_INDEX].trim();

                            //Construct the PlaceClass data with the data read
                            PlaceClass placeClass = new PlaceClass.Builder()
                                    .setId(placeResourceId)
                                    .setName(name)
                                    .setRating(rating)
                                    .setTypes(types)
                                    .setTypeImageResId(typeImageResId)
                                    .setPhotoResId(photoResId)
                                    .setAccessTimeInfo(accessTimeInfo)
                                    .setEntryFeeInfo(entryFeeInfo)
                                    .setLocation(location)
                                    .setDescription(description)
                                    .setWebsite(website)
                                    .setSwatchGenerated(photoSwatch != null)
                                    .setSwatchBodyTextColor(photoSwatch != null ? photoSwatch.getBodyTextColor() : 0)
                                    .setSwatchTitleTextColor(photoSwatch != null ? photoSwatch.getTitleTextColor() : 0)
                                    .setSwatchRgbColor(photoSwatch != null ? photoSwatch.getRgb() : 0)
                                    .createPlace();

                            //Add to the list of PlaceClass data constructed
                            placeClassList.add(placeClass);
                        }
                    }

                    if (placeClassList.size() > 0) {
                        //When we have places data to be loaded, pass the result to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the list of Places to the callback
                            resourceCallback.onResults(placeClassList);
                        });
                    } else {
                        //When we have no places to be loaded, pass an error message to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the error message to the callback
                            resourceCallback.onFailure(R.string.place_load_error);
                        });
                    }
                } else {
                    //When we have no places to be loaded, pass an error message to the callback
                    //Executing on the Main Thread
                    mExecute.getMainThread().execute(() -> {
                        //Pass the error message to the callback
                        resourceCallback.onFailure(R.string.place_load_error);
                    });
                }
            } finally {
                //Finally, release the TypedArray resource if held
                if (typedArrayPlaces != null) {
                    typedArrayPlaces.recycle();
                }
            }
        });
    }

    @Override
    public void getParkListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ParkClass>> resourceCallback) {
        //Executing on the Disk Thread
        mExecute.getDiskIO().execute(() -> {
            //Stores an Array of Parks to be read
            TypedArray typedArrayParks = null;
            try {
                //Obtaining the Typed Array of Parks
                typedArrayParks = mResources.obtainTypedArray(arrayResId);
                //The Number of Parks to load
                int noOfParks = typedArrayParks.length();
                if (noOfParks > 0) {
                    //When we have some Parks to be loaded

                    //Initializing an ArrayList of ParkClass data to store the ParkClass information
                    //for all the Parks read from the Typed Array
                    ArrayList<ParkClass> parkClassList = new ArrayList<>();
                    //Iterating over the Typed Array to build the list
                    for (int index = 0; index < noOfParks; index++) {
                        //Getting the Resource ID of the ParkClass Array resource
                        int parkResourceId = typedArrayParks.getResourceId(index, 0);
                        if (parkResourceId > 0) {
                            //When the resource read is valid

                            //Obtain the String Array for the resource which contains the data for the ParkClass
                            String[] parkStringArray = mResources.getStringArray(parkResourceId);
                            //Read the Name
                            String name = parkStringArray[ParkClass.Contract.NAME_INDEX].trim();
                            //Read the Rating
                            float rating = Float.parseFloat(parkStringArray[ParkClass.Contract.RATING_INDEX]);
                            //Read the drawable resource Id for the ParkClass Photo
                            int photoResId = ImageUtility.findDrawableResourceId(
                                    parkStringArray[ParkClass.Contract.PHOTO_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Construct a Bitmap for the ParkClass Photo and extract the Palette
                            Bitmap photoBitmap = BitmapFactory.decodeResource(mResources, photoResId);
                            Palette.Swatch photoSwatch = ImageUtility.extractVibrantSwatch(photoBitmap);
                            //Read the Access Timings
                            String accessTimeInfo = parkStringArray[ParkClass.Contract.ACCESS_TIME_INDEX].trim();
                            //Read the Entry Fee
                            String entryFeeInfo = parkStringArray[ParkClass.Contract.ENTRY_FEE_INDEX].trim();
                            //Read the Location
                            String location = parkStringArray[ParkClass.Contract.LOCATION_INDEX].trim();
                            //Read the Description
                            String description = parkStringArray[ParkClass.Contract.DESCRIPTION_INDEX];
                            //Read the Website
                            String website = parkStringArray[ParkClass.Contract.WEBSITE_INDEX].trim();

                            //Construct the ParkClass data with the data read
                            ParkClass parkClass = new ParkClass.Builder()
                                    .setId(parkResourceId)
                                    .setName(name)
                                    .setRating(rating)
                                    .setPhotoResId(photoResId)
                                    .setAccessTimeInfo(accessTimeInfo)
                                    .setEntryFeeInfo(entryFeeInfo)
                                    .setLocation(location)
                                    .setDescription(description)
                                    .setWebsite(website)
                                    .setSwatchGenerated(photoSwatch != null)
                                    .setSwatchBodyTextColor(photoSwatch != null ? photoSwatch.getBodyTextColor() : 0)
                                    .setSwatchTitleTextColor(photoSwatch != null ? photoSwatch.getTitleTextColor() : 0)
                                    .setSwatchRgbColor(photoSwatch != null ? photoSwatch.getRgb() : 0)
                                    .createPark();

                            //Add to the list of ParkClass data constructed
                            parkClassList.add(parkClass);
                        }
                    }

                    if (parkClassList.size() > 0) {
                        //When we have parks data to be loaded, pass the result to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the list of Parks to the callback
                            resourceCallback.onResults(parkClassList);
                        });
                    } else {
                        //When we have no parks to be loaded, pass an error message to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the error message to the callback
                            resourceCallback.onFailure(R.string.park_load_error);
                        });
                    }
                } else {
                    //When we have no parks to be loaded, pass an error message to the callback

                    //Executing on the Main Thread
                    mExecute.getMainThread().execute(() -> {
                        //Pass the error message to the callback
                        resourceCallback.onFailure(R.string.park_load_error);
                    });
                }
            } finally {
                //Finally, release the TypedArray resource if held
                if (typedArrayParks != null) {
                    typedArrayParks.recycle();
                }
            }
        });
    }

    @Override
    public void getHotelListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<HotelsClass>> resourceCallback) {
        //Executing on the Disk Thread
        mExecute.getDiskIO().execute(() -> {
            //Stores an Array of Hotels to be read
            TypedArray typedArrayHotels = null;
            try {
                //Obtaining the Typed Array of Hotels
                typedArrayHotels = mResources.obtainTypedArray(arrayResId);
                //The Number of Hotels to load
                int noOfHotels = typedArrayHotels.length();
                if (noOfHotels > 0) {
                    //When we have some Hotels to be loaded

                    //Initializing an ArrayList of HotelsClass data to store the HotelsClass information
                    //for all the Hotels read from the Typed Array
                    ArrayList<HotelsClass> hotelsClassList = new ArrayList<>();
                    //Iterating over the Typed Array to build the list
                    for (int index = 0; index < noOfHotels; index++) {
                        //Getting the Resource ID of the HotelsClass Array resource
                        int hotelResourceId = typedArrayHotels.getResourceId(index, 0);
                        if (hotelResourceId > 0) {
                            //When the resource read is valid

                            //Obtain the String Array for the resource which contains the data for the HotelsClass
                            String[] hotelStringArray = mResources.getStringArray(hotelResourceId);
                            //Read the Name
                            String name = hotelStringArray[HotelsClass.Contract.NAME_INDEX].trim();
                            //Read the Drawable Resource Id for the Star Type Rating
                            int starTypeResId = ImageUtility.findDrawableResourceId(
                                    hotelStringArray[HotelsClass.Contract.STAR_TYPE_ICON_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Read the Rating
                            float rating = Float.parseFloat(hotelStringArray[HotelsClass.Contract.RATING_INDEX]);
                            //Read the Drawable Resource Id for the HotelsClass Photo
                            int photoResId = ImageUtility.findDrawableResourceId(
                                    hotelStringArray[HotelsClass.Contract.PHOTO_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Construct a Bitmap for the HotelsClass Photo and extract the Palette
                            Bitmap photoBitmap = BitmapFactory.decodeResource(mResources, photoResId);
                            Palette.Swatch photoSwatch = ImageUtility.extractVibrantSwatch(photoBitmap);
                            //Read the Cost per Night
                            String costOfStay = hotelStringArray[HotelsClass.Contract.COST_INDEX].trim();
                            //Read the Location
                            String location = hotelStringArray[HotelsClass.Contract.LOCATION_INDEX].trim();
                            //Read the Contact Number
                            String contactNumber = hotelStringArray[HotelsClass.Contract.CONTACT_INDEX].trim();
                            //Read the Website
                            String website = hotelStringArray[HotelsClass.Contract.WEBSITE_INDEX].trim();

                            //Construct the HotelsClass data with the data read
                            HotelsClass hotelsClass = new HotelsClass.Builder()
                                    .setId(hotelResourceId)
                                    .setName(name)
                                    .setStarTypeResId(starTypeResId)
                                    .setRating(rating)
                                    .setPhotoResId(photoResId)
                                    .setCostOfStay(costOfStay)
                                    .setLocation(location)
                                    .setContactNumber(contactNumber)
                                    .setWebsite(website)
                                    .setSwatchGenerated(photoSwatch != null)
                                    .setSwatchBodyTextColor(photoSwatch != null ? photoSwatch.getBodyTextColor() : 0)
                                    .setSwatchTitleTextColor(photoSwatch != null ? photoSwatch.getTitleTextColor() : 0)
                                    .setSwatchRgbColor(photoSwatch != null ? photoSwatch.getRgb() : 0)
                                    .createHotel();

                            //Add to the list of HotelsClass data constructed
                            hotelsClassList.add(hotelsClass);
                        }
                    }

                    if (hotelsClassList.size() > 0) {
                        //When we have hotels data to be loaded, pass the result to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the list of Hotels to the callback
                            resourceCallback.onResults(hotelsClassList);
                        });
                    } else {
                        //When we have no hotels to be loaded, pass an error message to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the error message to the callback
                            resourceCallback.onFailure(R.string.hotel_load_error);
                        });
                    }

                } else {
                    //When we have no hotels to be loaded, pass an error message to the callback

                    //Executing on the Main Thread
                    mExecute.getMainThread().execute(() -> {
                        //Pass the error message to the callback
                        resourceCallback.onFailure(R.string.hotel_load_error);
                    });
                }
            } finally {
                //Finally, release the TypedArray resource if held
                if (typedArrayHotels != null) {
                    typedArrayHotels.recycle();
                }
            }
        });
    }

    @Override
    public void getRestaurantListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<RestaurantClass>> resourceCallback) {
        //Executing on the Disk Thread
        mExecute.getDiskIO().execute(() -> {
            //Stores an Array of Restaurants to be read
            TypedArray typedArrayRestaurants = null;
            try {
                //Obtaining the Typed Array of Restaurants
                typedArrayRestaurants = mResources.obtainTypedArray(arrayResId);
                //The Number of Restaurants to load
                int noOfRestaurants = typedArrayRestaurants.length();
                if (noOfRestaurants > 0) {
                    //When we have some Restaurants to be loaded

                    //Initializing an ArrayList of RestaurantClass data to store the RestaurantClass information
                    //for all the Restaurants read from the Typed Array
                    ArrayList<RestaurantClass> restaurantClassList = new ArrayList<>();
                    //Iterating over the Typed Array to build the list
                    for (int index = 0; index < noOfRestaurants; index++) {
                        //Getting the Resource ID of the RestaurantClass Array resource
                        int restaurantResourceId = typedArrayRestaurants.getResourceId(index, 0);
                        if (restaurantResourceId > 0) {
                            //When the resource read is valid

                            //Obtain the String Array for the resource which contains the data for the RestaurantClass
                            String[] restaurantStringArray = mResources.getStringArray(restaurantResourceId);
                            //Read the Name
                            String name = restaurantStringArray[RestaurantClass.Contract.NAME_INDEX].trim();
                            //Read the RestaurantClass Rating
                            float rating = Float.parseFloat(restaurantStringArray[RestaurantClass.Contract.RATING_INDEX]);
                            //Read the Cuisine Types
                            String cuisineTypes = restaurantStringArray[RestaurantClass.Contract.CUISINE_TYPES_INDEX].trim();
                            //Read the drawable resource Id for the RestaurantClass Photo
                            int photoResId = ImageUtility.findDrawableResourceId(
                                    restaurantStringArray[RestaurantClass.Contract.PHOTO_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Construct a Bitmap for the RestaurantClass Photo and extract the Palette
                            Bitmap photoBitmap = BitmapFactory.decodeResource(mResources, photoResId);
                            Palette.Swatch photoSwatch = ImageUtility.extractVibrantSwatch(photoBitmap);
                            //Read the Timings
                            String timings = restaurantStringArray[RestaurantClass.Contract.TIMINGS_INDEX].trim();
                            //Read the Average Cost
                            String averageCost = restaurantStringArray[RestaurantClass.Contract.COST_INDEX];
                            //Read the Location
                            String location = restaurantStringArray[RestaurantClass.Contract.LOCATION_INDEX].trim();
                            //Read the Contact Number
                            String contactNumber = restaurantStringArray[RestaurantClass.Contract.CONTACT_INDEX].trim();
                            //Read the Website
                            String website = restaurantStringArray[RestaurantClass.Contract.WEBSITE_INDEX].trim();

                            //Construct the RestaurantClass data with the data read
                            RestaurantClass restaurantClass = new RestaurantClass.Builder()
                                    .setName(name)
                                    .setRating(rating)
                                    .setCuisineTypes(cuisineTypes)
                                    .setPhotoResId(photoResId)
                                    .setTimings(timings)
                                    .setAverageCost(averageCost)
                                    .setLocation(location)
                                    .setContactNumber(contactNumber)
                                    .setWebsite(website)
                                    .setSwatchGenerated(photoSwatch != null)
                                    .setSwatchBodyTextColor(photoSwatch != null ? photoSwatch.getBodyTextColor() : 0)
                                    .setSwatchTitleTextColor(photoSwatch != null ? photoSwatch.getTitleTextColor() : 0)
                                    .setSwatchRgbColor(photoSwatch != null ? photoSwatch.getRgb() : 0)
                                    .createRestaurant();

                            //Add to the list of RestaurantClass data constructed
                            restaurantClassList.add(restaurantClass);
                        }
                    }

                    if (restaurantClassList.size() > 0) {
                        //When we have restaurants data to be loaded, pass the result to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the list of Restaurants to the callback
                            resourceCallback.onResults(restaurantClassList);
                        });
                    } else {
                        //When we have no restaurants to be loaded, pass an error message to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the error message to the callback
                            resourceCallback.onFailure(R.string.resoto_load_error);
                        });
                    }

                } else {
                    //When we have no restaurants to be loaded, pass an error message to the callback

                    //Executing on the Main Thread
                    mExecute.getMainThread().execute(() -> {
                        //Pass the error message to the callback
                        resourceCallback.onFailure(R.string.resoto_load_error);
                    });
                }
            } finally {
                //Finally, release the TypedArray resource if held
                if (typedArrayRestaurants != null) {
                    typedArrayRestaurants.recycle();
                }
            }
        });
    }

    @Override
    public void getShopListEntries(@ArrayRes int arrayResId, @NonNull GetResourceCallback<List<ShopClass>> resourceCallback) {
        //Executing on the Disk Thread
        mExecute.getDiskIO().execute(() -> {
            //Stores an Array of Shops to be read
            TypedArray typedArrayShops = null;
            try {
                //Obtaining the Typed Array of Shops
                typedArrayShops = mResources.obtainTypedArray(R.array.shop_list_entries);
                //The Number of Shops to load
                int noOfShops = typedArrayShops.length();
                if (noOfShops > 0) {
                    //When we have some Shops to be loaded

                    //Initializing an ArrayList of ShopClass data to store the ShopClass information
                    //for all the Shops read from the Typed Array
                    ArrayList<ShopClass> shopClassList = new ArrayList<>();
                    //Iterating over the Typed Array to build the list
                    for (int index = 0; index < noOfShops; index++) {
                        //Getting the Resource ID of the ShopClass Array resource
                        int shopResourceId = typedArrayShops.getResourceId(index, 0);
                        if (shopResourceId > 0) {
                            //When the resource read is valid

                            //Obtain the String Array for the resource which contains the data for the ShopClass
                            String[] shopStringArray = mResources.getStringArray(shopResourceId);
                            //Read the Name
                            String name = shopStringArray[ShopClass.Contract.NAME_INDEX].trim();
                            //Read the Rating
                            float rating = Float.parseFloat(shopStringArray[ShopClass.Contract.RATING_INDEX]);
                            //Read the ShopClass Types
                            String shopTypes = shopStringArray[ShopClass.Contract.SHOP_TYPES_INDEX].trim();
                            //Read the drawable resource Id for the ShopClass Photo
                            int photoResId = ImageUtility.findDrawableResourceId(
                                    shopStringArray[ShopClass.Contract.PHOTO_INDEX],
                                    mAppPackageName,
                                    mResources
                            );
                            //Construct a Bitmap for the ShopClass Photo and extract the Palette
                            Bitmap photoBitmap = BitmapFactory.decodeResource(mResources, photoResId);
                            Palette.Swatch photoSwatch = ImageUtility.extractVibrantSwatch(photoBitmap);
                            //Read the Timings
                            String timings = shopStringArray[ShopClass.Contract.TIMINGS_INDEX].trim();
                            //Read the Location
                            String location = shopStringArray[ShopClass.Contract.LOCATION_INDEX].trim();
                            //Read the Website
                            String website = shopStringArray[ShopClass.Contract.WEBSITE_INDEX].trim();

                            //Construct the ShopClass data with the data read
                            ShopClass shopClass = new ShopClass.Builder()
                                    .setName(name)
                                    .setRating(rating)
                                    .setShopTypes(shopTypes)
                                    .setPhotoResId(photoResId)
                                    .setTimings(timings)
                                    .setLocation(location)
                                    .setWebsite(website)
                                    .setSwatchGenerated(photoSwatch != null)
                                    .setSwatchBodyTextColor(photoSwatch != null ? photoSwatch.getBodyTextColor() : 0)
                                    .setSwatchTitleTextColor(photoSwatch != null ? photoSwatch.getTitleTextColor() : 0)
                                    .setSwatchRgbColor(photoSwatch != null ? photoSwatch.getRgb() : 0)
                                    .createShop();

                            //Add to the list of ShopClass data constructed
                            shopClassList.add(shopClass);
                        }
                    }

                    if (shopClassList.size() > 0) {
                        //When we have shops data to be loaded, pass the result to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the list of Shops to the callback
                            resourceCallback.onResults(shopClassList);
                        });
                    } else {
                        //When we have no shops to be loaded, pass an error message to the callback

                        //Executing on the Main Thread
                        mExecute.getMainThread().execute(() -> {
                            //Pass the error message to the callback
                            resourceCallback.onFailure(R.string.shop_load_error);
                        });
                    }

                } else {
                    //When we have no shops to be loaded, pass an error message to the callback

                    //Executing on the Main Thread
                    mExecute.getMainThread().execute(() -> {
                        //Pass the error message to the callback
                        resourceCallback.onFailure(R.string.shop_load_error);
                    });
                }
            } finally {
                //Finally, release the TypedArray resource if held
                if (typedArrayShops != null) {
                    typedArrayShops.recycle();
                }
            }
        });
    }

}
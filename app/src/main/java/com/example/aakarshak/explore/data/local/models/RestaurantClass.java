package com.example.aakarshak.explore.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class RestaurantClass implements Parcelable {

    public static final Creator<RestaurantClass> CREATOR = new Creator<RestaurantClass>() {

        @Override
        public RestaurantClass createFromParcel(Parcel in) {
            return new RestaurantClass(in);
        }

        @Override
        public RestaurantClass[] newArray(int size) {
            return new RestaurantClass[size];
        }
    };
    //The Unique Id of the RestaurantClass
    private final int mId;
    //The Name of the RestaurantClass
    private final String mName;
    //Foodie's Rating for the RestaurantClass
    private final float mRating;
    //Cuisine Types served by the RestaurantClass
    private final String mCuisineTypes;
    //Photo Resource Id of the RestaurantClass
    private final int mPhotoResId;
    //Timings of the RestaurantClass
    private final String mTimings;
    //Average Cost of dining at the RestaurantClass
    private final String mAverageCost;
    //Location address of the RestaurantClass
    private final String mLocation;
    //Contact Number of the RestaurantClass
    private final String mContactNumber;
    //Website of the RestaurantClass
    private final String mWebsite;
    //Boolean that indicates if a Palette Swatch was generated for the RestaurantClass Photo
    private final boolean mSwatchGenerated;
    //The Swatch Text Color to be used for the Body Text
    private final int mSwatchBodyTextColor;
    //The Swatch Text Color to be used for the Title Text
    private final int mSwatchTitleTextColor;
    //The Swatch Color
    private final int mSwatchRgbColor;

    private RestaurantClass(int id, String name, float rating, String cuisineTypes,
                            @DrawableRes int photoResId, String timings, String averageCost,
                            String location, String contactNumber, String website,
                            boolean swatchGenerated, int swatchBodyTextColor,
                            int swatchTitleTextColor, int swatchRgbColor) {
        mId = id;
        mName = name;
        mRating = rating;
        mCuisineTypes = cuisineTypes;
        mPhotoResId = photoResId;
        mTimings = timings;
        mAverageCost = averageCost;
        mLocation = location;
        mContactNumber = contactNumber;
        mWebsite = website;
        mSwatchGenerated = swatchGenerated;
        mSwatchBodyTextColor = swatchBodyTextColor;
        mSwatchTitleTextColor = swatchTitleTextColor;
        mSwatchRgbColor = swatchRgbColor;
    }

    protected RestaurantClass(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mRating = in.readFloat();
        mCuisineTypes = in.readString();
        mPhotoResId = in.readInt();
        mTimings = in.readString();
        mAverageCost = in.readString();
        mLocation = in.readString();
        mContactNumber = in.readString();
        mWebsite = in.readString();
        mSwatchGenerated = in.readByte() != 0;
        mSwatchBodyTextColor = in.readInt();
        mSwatchTitleTextColor = in.readInt();
        mSwatchRgbColor = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeFloat(mRating);
        dest.writeString(mCuisineTypes);
        dest.writeInt(mPhotoResId);
        dest.writeString(mTimings);
        dest.writeString(mAverageCost);
        dest.writeString(mLocation);
        dest.writeString(mContactNumber);
        dest.writeString(mWebsite);
        dest.writeByte((byte) (mSwatchGenerated ? 1 : 0));
        dest.writeInt(mSwatchBodyTextColor);
        dest.writeInt(mSwatchTitleTextColor);
        dest.writeInt(mSwatchRgbColor);
    }

    @Override
    public int describeContents() {
        return 0; //Indicating with no mask
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public float getRating() {
        return mRating;
    }

    public String getCuisineTypes() {
        return mCuisineTypes;
    }

    public int getPhotoResId() {
        return mPhotoResId;
    }

    public String getTimings() {
        return mTimings;
    }

    public String getAverageCost() {
        return mAverageCost;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getContactNumber() {
        return mContactNumber;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public boolean isSwatchGenerated() {
        return mSwatchGenerated;
    }

    public int getSwatchBodyTextColor() {
        return mSwatchBodyTextColor;
    }

    public int getSwatchTitleTextColor() {
        return mSwatchTitleTextColor;
    }

    public int getSwatchRgbColor() {
        return mSwatchRgbColor;
    }

    public interface Contract {
        int NAME_INDEX = 0;
        int CUISINE_TYPES_INDEX = 1;
        int RATING_INDEX = 2;
        int PHOTO_INDEX = 3;
        int TIMINGS_INDEX = 4;
        int COST_INDEX = 5;
        int LOCATION_INDEX = 6;
        int CONTACT_INDEX = 7;
        int WEBSITE_INDEX = 8;
    }

    public static class Builder {

        private int mId;
        private String mName;
        private float mRating;
        private String mCuisineTypes;
        private int mPhotoResId;
        private String mTimings;
        private String mAverageCost;
        private String mLocation;
        private String mContactNumber;
        private String mWebsite;
        private boolean mSwatchGenerated;
        private int mSwatchBodyTextColor;
        private int mSwatchTitleTextColor;
        private int mSwatchRgbColor;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setRating(float rating) {
            mRating = rating;
            return this;
        }

        public Builder setCuisineTypes(String cuisineTypes) {
            mCuisineTypes = cuisineTypes;
            return this;
        }

        public Builder setPhotoResId(@DrawableRes int photoResId) {
            mPhotoResId = photoResId;
            return this;
        }

        public Builder setTimings(String timings) {
            mTimings = timings;
            return this;
        }

        public Builder setAverageCost(String averageCost) {
            mAverageCost = averageCost;
            return this;
        }

        public Builder setLocation(String location) {
            mLocation = location;
            return this;
        }

        public Builder setContactNumber(String contactNumber) {
            mContactNumber = contactNumber;
            return this;
        }

        public Builder setWebsite(String website) {
            mWebsite = website;
            return this;
        }

        public Builder setSwatchGenerated(boolean swatchGenerated) {
            mSwatchGenerated = swatchGenerated;
            return this;
        }

        public Builder setSwatchBodyTextColor(int swatchBodyTextColor) {
            mSwatchBodyTextColor = swatchBodyTextColor;
            return this;
        }

        public Builder setSwatchTitleTextColor(int swatchTitleTextColor) {
            mSwatchTitleTextColor = swatchTitleTextColor;
            return this;
        }

        public Builder setSwatchRgbColor(int swatchRgbColor) {
            mSwatchRgbColor = swatchRgbColor;
            return this;
        }

        public RestaurantClass createRestaurant() {
            return new RestaurantClass(mId, mName, mRating, mCuisineTypes, mPhotoResId,
                    mTimings, mAverageCost, mLocation, mContactNumber, mWebsite,
                    mSwatchGenerated, mSwatchBodyTextColor, mSwatchTitleTextColor, mSwatchRgbColor);
        }
    }
}

package com.example.aakarshak.explore.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class ShopClass implements Parcelable {

    public static final Creator<ShopClass> CREATOR = new Creator<ShopClass>() {

        @Override
        public ShopClass createFromParcel(Parcel in) {
            return new ShopClass(in);
        }

        @Override
        public ShopClass[] newArray(int size) {
            return new ShopClass[size];
        }
    };
    //The Unique Id of the ShopClass
    private final int mId;
    //The Name of the ShopClass
    private final String mName;
    //User Rating of the ShopClass
    private final float mRating;
    //Photo Resource Id of the ShopClass
    private final int mPhotoResId;
    //Types/keywords describing the ShopClass
    private final String mShopTypes;
    //Timings of the ShopClass
    private final String mTimings;
    //Location address of the ShopClass
    private final String mLocation;
    //Website of the ShopClass
    private final String mWebsite;
    //Boolean that indicates if a Palette Swatch was generated for the ShopClass Photo
    private final boolean mSwatchGenerated;
    //The Swatch Text Color to be used for the Body Text
    private final int mSwatchBodyTextColor;
    //The Swatch Text Color to be used for the Title Text
    private final int mSwatchTitleTextColor;
    //The Swatch Color
    private final int mSwatchRgbColor;

    private ShopClass(int id, String name, float rating, @DrawableRes int photoResId,
                      String shopTypes, String timings, String location, String website,
                      boolean swatchGenerated, int swatchBodyTextColor,
                      int swatchTitleTextColor, int swatchRgbColor) {
        mId = id;
        mName = name;
        mRating = rating;
        mPhotoResId = photoResId;
        mShopTypes = shopTypes;
        mTimings = timings;
        mLocation = location;
        mWebsite = website;
        mSwatchGenerated = swatchGenerated;
        mSwatchBodyTextColor = swatchBodyTextColor;
        mSwatchTitleTextColor = swatchTitleTextColor;
        mSwatchRgbColor = swatchRgbColor;
    }

    protected ShopClass(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mRating = in.readFloat();
        mPhotoResId = in.readInt();
        mShopTypes = in.readString();
        mTimings = in.readString();
        mLocation = in.readString();
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
        dest.writeInt(mPhotoResId);
        dest.writeString(mShopTypes);
        dest.writeString(mTimings);
        dest.writeString(mLocation);
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

    public int getPhotoResId() {
        return mPhotoResId;
    }

    public String getShopTypes() {
        return mShopTypes;
    }

    public String getTimings() {
        return mTimings;
    }

    public String getLocation() {
        return mLocation;
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
        int SHOP_TYPES_INDEX = 1;
        int RATING_INDEX = 2;
        int PHOTO_INDEX = 3;
        int TIMINGS_INDEX = 4;
        int LOCATION_INDEX = 5;
        int WEBSITE_INDEX = 6;
    }

    public static class Builder {

        private int mId;
        private String mName;
        private float mRating;
        private int mPhotoResId;
        private String mShopTypes;
        private String mTimings;
        private String mLocation;
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

        public Builder setPhotoResId(@DrawableRes int photoResId) {
            mPhotoResId = photoResId;
            return this;
        }

        public Builder setShopTypes(String shopTypes) {
            mShopTypes = shopTypes;
            return this;
        }

        public Builder setTimings(String timings) {
            mTimings = timings;
            return this;
        }

        public Builder setLocation(String location) {
            mLocation = location;
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

        public ShopClass createShop() {
            return new ShopClass(mId, mName, mRating, mPhotoResId, mShopTypes, mTimings,
                    mLocation, mWebsite, mSwatchGenerated, mSwatchBodyTextColor,
                    mSwatchTitleTextColor, mSwatchRgbColor);
        }
    }
}

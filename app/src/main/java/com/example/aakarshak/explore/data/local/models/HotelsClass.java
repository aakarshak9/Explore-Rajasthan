package com.example.aakarshak.explore.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class HotelsClass implements Parcelable {


    public static final Creator<HotelsClass> CREATOR = new Creator<HotelsClass>() {
        @Override
        public HotelsClass createFromParcel(Parcel in) {
            return new HotelsClass(in);
        }

        @Override
        public HotelsClass[] newArray(int size) {
            return new HotelsClass[size];
        }
    };
    //The Unique Id of the HotelsClass
    private final int mId;
    //The Name of the HotelsClass
    private final String mName;
    //Icon Resource Id of the HotelsClass Star Rating
    private final int mStarTypeResId;
    //Traveller's Rating for the HotelsClass
    private final float mRating;
    //Photo Resource Id of the HotelsClass
    private final int mPhotoResId;
    //Cost of Stay in the HotelsClass
    private final String mCostOfStay;
    //Location address of the HotelsClass
    private final String mLocation;
    //Contact Number of the HotelsClass
    private final String mContactNumber;
    //Website of the HotelsClass
    private final String mWebsite;
    //Boolean that indicates if a Palette Swatch was generated for the HotelsClass Photo
    private final boolean mSwatchGenerated;
    //The Swatch Text Color to be used for the Body Text
    private final int mSwatchBodyTextColor;
    //The Swatch Text Color to be used for the Title Text
    private final int mSwatchTitleTextColor;
    //The Swatch Color
    private final int mSwatchRgbColor;

    private HotelsClass(int id, String name, @DrawableRes int starTypeResId,
                        float rating, @DrawableRes int photoResId, String costOfStay,
                        String location, String contactNumber, String website,
                        boolean swatchGenerated, int swatchBodyTextColor,
                        int swatchTitleTextColor, int swatchRgbColor) {
        mId = id;
        mName = name;
        mStarTypeResId = starTypeResId;
        mRating = rating;
        mPhotoResId = photoResId;
        mCostOfStay = costOfStay;
        mLocation = location;
        mContactNumber = contactNumber;
        mWebsite = website;
        mSwatchGenerated = swatchGenerated;
        mSwatchBodyTextColor = swatchBodyTextColor;
        mSwatchTitleTextColor = swatchTitleTextColor;
        mSwatchRgbColor = swatchRgbColor;
    }

    protected HotelsClass(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mStarTypeResId = in.readInt();
        mRating = in.readFloat();
        mPhotoResId = in.readInt();
        mCostOfStay = in.readString();
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
        dest.writeInt(mStarTypeResId);
        dest.writeFloat(mRating);
        dest.writeInt(mPhotoResId);
        dest.writeString(mCostOfStay);
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

    public int getStarTypeResId() {
        return mStarTypeResId;
    }


    public float getRating() {
        return mRating;
    }


    public int getPhotoResId() {
        return mPhotoResId;
    }


    public String getCostOfStay() {
        return mCostOfStay;
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
        int STAR_TYPE_ICON_INDEX = 1;
        int RATING_INDEX = 2;
        int PHOTO_INDEX = 3;
        int COST_INDEX = 4;
        int LOCATION_INDEX = 5;
        int CONTACT_INDEX = 6;
        int WEBSITE_INDEX = 7;
    }


    public static class Builder {

        private int mId;
        private String mName;
        private int mStarTypeResId;
        private float mRating;
        private int mPhotoResId;
        private String mCostOfStay;
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

        public Builder setStarTypeResId(@DrawableRes int starTypeResId) {
            mStarTypeResId = starTypeResId;
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

        public Builder setCostOfStay(String costOfStay) {
            mCostOfStay = costOfStay;
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

        public HotelsClass createHotel() {
            return new HotelsClass(mId, mName, mStarTypeResId, mRating, mPhotoResId, mCostOfStay,
                    mLocation, mContactNumber, mWebsite, mSwatchGenerated,
                    mSwatchBodyTextColor, mSwatchTitleTextColor, mSwatchRgbColor);
        }
    }
}

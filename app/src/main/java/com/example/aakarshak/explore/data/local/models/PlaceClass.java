package com.example.aakarshak.explore.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

public class PlaceClass implements Parcelable {

    public static final Creator<PlaceClass> CREATOR = new Creator<PlaceClass>() {

        @Override
        public PlaceClass createFromParcel(Parcel in) {
            return new PlaceClass(in);
        }

        @Override
        public PlaceClass[] newArray(int size) {
            return new PlaceClass[size];
        }
    };
    //The Unique Id of the PlaceClass
    private final int mId;
    //The Name of the PlaceClass
    private final String mName;
    //User Rating of the PlaceClass
    private final float mRating;
    //The Types/Keywords describing the PlaceClass
    private final String mTypes;
    //Icon Resource Id for the PlaceClass Type
    private final int mTypeImageResId;
    //Photo Resource Id of the PlaceClass
    private final int mPhotoResId;
    //Access Timings of the PlaceClass
    private final String mAccessTimeInfo;
    //The Entry Fee for the PlaceClass
    private final String mEntryFeeInfo;
    //Description of the PlaceClass
    private final String mDescription;
    //The Location Address of the PlaceClass
    private final String mLocation;
    //Website of the PlaceClass
    private final String mWebsite;
    //Boolean that indicates if a Palette Swatch was generated for the PlaceClass Photo
    private final boolean mSwatchGenerated;
    //The Swatch Text Color to be used for the Body Text
    private final int mSwatchBodyTextColor;
    //The Swatch Text Color to be used for the Title Text
    private final int mSwatchTitleTextColor;
    //The Swatch Color
    private final int mSwatchRgbColor;

    private PlaceClass(int id, String name, float rating, String types,
                       @DrawableRes int typeImageResId, @DrawableRes int photoResId,
                       String accessTimeInfo, String entryFeeInfo, String description, String location,
                       String website, boolean swatchGenerated,
                       int swatchBodyTextColor, int swatchTitleTextColor, int swatchRgbColor) {
        mId = id;
        mName = name;
        mRating = rating;
        mTypes = types;
        mTypeImageResId = typeImageResId;
        mPhotoResId = photoResId;
        mAccessTimeInfo = accessTimeInfo;
        mEntryFeeInfo = entryFeeInfo;
        mDescription = description;
        mLocation = location;
        mWebsite = website;
        mSwatchGenerated = swatchGenerated;
        mSwatchBodyTextColor = swatchBodyTextColor;
        mSwatchTitleTextColor = swatchTitleTextColor;
        mSwatchRgbColor = swatchRgbColor;
    }

    protected PlaceClass(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mRating = in.readFloat();
        mTypes = in.readString();
        mTypeImageResId = in.readInt();
        mPhotoResId = in.readInt();
        mAccessTimeInfo = in.readString();
        mEntryFeeInfo = in.readString();
        mDescription = in.readString();
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
        dest.writeString(mTypes);
        dest.writeInt(mTypeImageResId);
        dest.writeInt(mPhotoResId);
        dest.writeString(mAccessTimeInfo);
        dest.writeString(mEntryFeeInfo);
        dest.writeString(mDescription);
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

    public String getTypes() {
        return mTypes;
    }

    public int getTypeImageResId() {
        return mTypeImageResId;
    }

    public int getPhotoResId() {
        return mPhotoResId;
    }

    public String getAccessTimeInfo() {
        return mAccessTimeInfo;
    }

    public String getEntryFeeInfo() {
        return mEntryFeeInfo;
    }

    public String getDescription() {
        return mDescription;
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
        int RATING_INDEX = 1;
        int TYPES_INDEX = 2;
        int TYPE_ICON_INDEX = 3;
        int PHOTO_INDEX = 4;
        int ACCESS_TIME_INDEX = 5;
        int ENTRY_FEE_INDEX = 6;
        int LOCATION_INDEX = 7;
        int DESCRIPTION_INDEX = 8;
        int WEBSITE_INDEX = 9;
    }

    public static class Builder {

        private int mId;
        private String mName;
        private float mRating;
        private String mTypes;
        private int mTypeImageResId;
        private int mPhotoResId;
        private String mAccessTimeInfo;
        private String mEntryFeeInfo;
        private String mDescription;
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

        public Builder setTypes(String types) {
            mTypes = types;
            return this;
        }

        public Builder setTypeImageResId(@DrawableRes int typeImageResId) {
            mTypeImageResId = typeImageResId;
            return this;
        }

        public Builder setPhotoResId(@DrawableRes int photoResId) {
            mPhotoResId = photoResId;
            return this;
        }

        public Builder setAccessTimeInfo(String accessTimeInfo) {
            mAccessTimeInfo = accessTimeInfo;
            return this;
        }

        public Builder setEntryFeeInfo(String entryFeeInfo) {
            mEntryFeeInfo = entryFeeInfo;
            return this;
        }

        public Builder setDescription(String description) {
            mDescription = description;
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

        public PlaceClass createPlace() {
            return new PlaceClass(mId, mName, mRating, mTypes, mTypeImageResId, mPhotoResId, mAccessTimeInfo,
                    mEntryFeeInfo, mDescription, mLocation, mWebsite, mSwatchGenerated,
                    mSwatchBodyTextColor, mSwatchTitleTextColor, mSwatchRgbColor);
        }
    }
}

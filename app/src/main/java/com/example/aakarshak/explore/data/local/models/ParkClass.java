package com.example.aakarshak.explore.data.local.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

public class ParkClass implements Parcelable {

    /**
     * Implementation of {@link android.os.Parcelable.Creator} interface
     * to generate instances of this Parcelable class {@link ParkClass} from a {@link Parcel}
     */
    public static final Creator<ParkClass> CREATOR = new Creator<ParkClass>() {
        /**
         * Creates an instance of this Parcelable class {@link ParkClass} from
         * a given Parcel whose data had been previously written by #writeToParcel() method
         *
         * @param in The Parcel to read the object's data from.
         * @return Returns a new instance of this Parcelable class {@link ParkClass}
         */
        @Override
        public ParkClass createFromParcel(Parcel in) {
            return new ParkClass(in);
        }

        /**
         * Creates a new array of this Parcelable class {@link ParkClass}
         *
         * @param size Size of the array
         * @return Returns an array of this Parcelable class {@link ParkClass}, with every
         * entry initialized to null
         */
        @Override
        public ParkClass[] newArray(int size) {
            return new ParkClass[size];
        }
    };
    //The Unique Id of the ParkClass
    private final int mId;
    //The Name of the ParkClass
    private final String mName;
    //User Rating of the ParkClass
    private final float mRating;
    //Photo Resource Id of the ParkClass
    private final int mPhotoResId;
    //Access Timings of the ParkClass
    private final String mAccessTimeInfo;
    //The Entry Fee for the ParkClass
    private final String mEntryFeeInfo;
    //Description of the ParkClass
    private final String mDescription;
    //The Location Address of the ParkClass
    private final String mLocation;
    //Website of the ParkClass
    private final String mWebsite;
    //Boolean that indicates if a Palette Swatch was generated for the ParkClass Photo
    private final boolean mSwatchGenerated;
    //The Swatch Text Color to be used for the Body Text
    private final int mSwatchBodyTextColor;
    //The Swatch Text Color to be used for the Title Text
    private final int mSwatchTitleTextColor;
    //The Swatch Color
    private final int mSwatchRgbColor;

    /**
     * Private Constructor of {@link ParkClass}
     *
     * @param id                   The Unique Id of the ParkClass
     * @param name                 The Name of the ParkClass
     * @param rating               User Rating of the ParkClass
     * @param photoResId           Photo Resource Id of the ParkClass
     * @param accessTimeInfo       Access Timings of the ParkClass
     * @param entryFeeInfo         The Entry Fee for the ParkClass
     * @param description          Description of the ParkClass
     * @param location             The Location Address of the ParkClass
     * @param website              Website of the ParkClass
     * @param swatchGenerated      Boolean that indicates if a Palette Swatch was generated for the ParkClass Photo
     * @param swatchBodyTextColor  The Swatch Text Color to be used for the Body Text
     * @param swatchTitleTextColor The Swatch Text Color to be used for the Title Text
     * @param swatchRgbColor       The Swatch Color
     */
    private ParkClass(int id, String name, float rating, @DrawableRes int photoResId,
                      String accessTimeInfo, String entryFeeInfo, String description,
                      String location, String website, boolean swatchGenerated,
                      int swatchBodyTextColor, int swatchTitleTextColor, int swatchRgbColor) {
        mId = id;
        mName = name;
        mRating = rating;
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

    /**
     * Parcelable constructor that de-serializes the data from a Parcel {@code in} passed
     *
     * @param in The Instance of the Parcel class containing the serialized data
     */
    protected ParkClass(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mRating = in.readFloat();
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

    /**
     * Flattens/Serializes the object of {@link ParkClass} into a Parcel
     *
     * @param dest  The Parcel in which the object should be written
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeFloat(mRating);
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

    /**
     * Describes the kinds of special objects contained in this Parcelable
     * instance's marshaled representation.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0; //Indicating with no mask
    }

    /**
     * Getter Method for the Unique Id of the ParkClass
     *
     * @return The Unique Integer Id of the ParkClass
     */
    public int getId() {
        return mId;
    }

    /**
     * Getter Method for the Name of the ParkClass
     *
     * @return The Name of the ParkClass
     */
    public String getName() {
        return mName;
    }

    /**
     * Getter Method for the User Rating of the ParkClass
     *
     * @return A Float representing the User Rating of the ParkClass
     */
    public float getRating() {
        return mRating;
    }

    /**
     * Getter Method for the Photo Resource Id of the ParkClass
     *
     * @return An Integer representing the Photo Resource Id of the ParkClass
     */
    public int getPhotoResId() {
        return mPhotoResId;
    }

    /**
     * Getter Method for the Access Timings of the ParkClass
     *
     * @return The Access Timings of the ParkClass
     */
    public String getAccessTimeInfo() {
        return mAccessTimeInfo;
    }

    /**
     * Getter Method for the Entry Fee for the ParkClass
     *
     * @return The Entry Fee for the ParkClass
     */
    public String getEntryFeeInfo() {
        return mEntryFeeInfo;
    }

    /**
     * Getter Method for the Description of the ParkClass
     *
     * @return The Description of the ParkClass
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Getter Method for the Location Address of the ParkClass
     *
     * @return The Location Address of the ParkClass
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Getter Method for the Website of the ParkClass
     *
     * @return The Website of the ParkClass
     */
    public String getWebsite() {
        return mWebsite;
    }

    /**
     * Getter Method for the Boolean that indicates if a Palette Swatch was generated for the ParkClass Photo
     *
     * @return Boolean that indicates if a Palette Swatch was generated for the ParkClass Photo
     */
    public boolean isSwatchGenerated() {
        return mSwatchGenerated;
    }

    /**
     * Getter Method for the Swatch Text Color to be used for the Body Text
     *
     * @return Integer representing the Swatch Text Color to be used for the Body Text
     */
    public int getSwatchBodyTextColor() {
        return mSwatchBodyTextColor;
    }

    /**
     * Getter Method for the Swatch Text Color to be used for the Title Text
     *
     * @return Integer representing the Swatch Text Color to be used for the Title Text
     */
    public int getSwatchTitleTextColor() {
        return mSwatchTitleTextColor;
    }

    /**
     * Getter Method for the Swatch Color
     *
     * @return Integer representing the Swatch Color
     */
    public int getSwatchRgbColor() {
        return mSwatchRgbColor;
    }

    /**
     * Contract Interface for the data index of the ParkClass arrays stored in resources
     */
    public interface Contract {
        int NAME_INDEX = 0;
        int RATING_INDEX = 1;
        int PHOTO_INDEX = 2;
        int ACCESS_TIME_INDEX = 3;
        int ENTRY_FEE_INDEX = 4;
        int LOCATION_INDEX = 5;
        int DESCRIPTION_INDEX = 6;
        int WEBSITE_INDEX = 7;
    }

    /**
     * Static Builder class that constructs {@link ParkClass}
     */
    public static class Builder {

        private int mId;
        private String mName;
        private float mRating;
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

        /**
         * Setter for the Integer Unique Id of the ParkClass
         *
         * @param id Integer Unique Id of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setId(int id) {
            mId = id;
            return this;
        }

        /**
         * Setter for the Name of the ParkClass
         *
         * @param name The Name of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setName(String name) {
            mName = name;
            return this;
        }

        /**
         * Setter for the User Rating of the ParkClass
         *
         * @param rating Float value of the User Rating of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setRating(float rating) {
            mRating = rating;
            return this;
        }

        /**
         * Setter for the Photo Resource Id of the ParkClass
         *
         * @param photoResId Integer representing the Photo Resource Id of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setPhotoResId(@DrawableRes int photoResId) {
            mPhotoResId = photoResId;
            return this;
        }

        /**
         * Setter for the Access Timings of the ParkClass
         *
         * @param accessTimeInfo The Access Timings of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setAccessTimeInfo(String accessTimeInfo) {
            mAccessTimeInfo = accessTimeInfo;
            return this;
        }

        /**
         * Setter for the Entry Fee for the ParkClass
         *
         * @param entryFeeInfo The Entry Fee for the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setEntryFeeInfo(String entryFeeInfo) {
            mEntryFeeInfo = entryFeeInfo;
            return this;
        }

        /**
         * Setter for the Description of the ParkClass
         *
         * @param description The Description of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        /**
         * Setter for the Location Address of the ParkClass
         *
         * @param location The Location Address of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setLocation(String location) {
            mLocation = location;
            return this;
        }

        /**
         * Setter for the Website of the ParkClass
         *
         * @param website The Website of the ParkClass
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setWebsite(String website) {
            mWebsite = website;
            return this;
        }

        /**
         * Setter for the Boolean that indicates if a Palette Swatch was generated for the ParkClass Photo
         *
         * @param swatchGenerated Boolean that indicates if a Palette Swatch was generated for the ParkClass Photo
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setSwatchGenerated(boolean swatchGenerated) {
            mSwatchGenerated = swatchGenerated;
            return this;
        }

        /**
         * Setter for the Swatch Text Color to be used for the Body Text
         *
         * @param swatchBodyTextColor The Swatch Text Color to be used for the Body Text
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setSwatchBodyTextColor(int swatchBodyTextColor) {
            mSwatchBodyTextColor = swatchBodyTextColor;
            return this;
        }

        /**
         * Setter for the Swatch Text Color to be used for the Title Text
         *
         * @param swatchTitleTextColor The Swatch Text Color to be used for the Title Text
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setSwatchTitleTextColor(int swatchTitleTextColor) {
            mSwatchTitleTextColor = swatchTitleTextColor;
            return this;
        }

        /**
         * Setter for the Swatch Color
         *
         * @param swatchRgbColor The Swatch Color
         * @return Instance of {@link Builder} for chaining method calls.
         */
        public Builder setSwatchRgbColor(int swatchRgbColor) {
            mSwatchRgbColor = swatchRgbColor;
            return this;
        }

        /**
         * Terminal Method that constructs the {@link ParkClass}
         *
         * @return Instance of {@link ParkClass} built
         */
        public ParkClass createPark() {
            return new ParkClass(mId, mName, mRating, mPhotoResId, mAccessTimeInfo, mEntryFeeInfo,
                    mDescription, mLocation, mWebsite, mSwatchGenerated,
                    mSwatchBodyTextColor, mSwatchTitleTextColor, mSwatchRgbColor);
        }
    }
}

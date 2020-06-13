package com.example.aakarshak.explore.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;

/**
 * Utility class that deals with the Common Intents used in the App.
 *
 * @author Kaushik N Sanji
 */
public class UtilityIntent {

    //Constants used for generating the Geo Location URI for Map Intents
    private static final String MAP_URI_SCHEME = "geo";
    private static final String MAP_PATH_DEFAULT_COORDINATES = "0,0";
    private static final String MAP_QUERY_PARAMETER = "q";

    //Text Data Type Constants
    private static final String TEXT_TYPE_PLAIN = "text/plain";

    //URI Scheme Constants
    private static final String URI_STR_TELEPHONE_SCHEME = "tel:";

    /**
     * Private constructor to avoid instantiating {@link UtilityIntent}
     */
    private UtilityIntent() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

    /**
     * Method that opens a webpage for the URL passed
     *
     * @param context A {@link Context} of the Calling Activity/Fragment
     * @param webUrl  String containing the URL of the Web Page to be launched
     */
    public static void openLink(Context context, String webUrl) {
        //Parsing the URL
        Uri webPageUri = Uri.parse(webUrl);
        //Creating an ACTION_VIEW Intent with the URI
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webPageUri);
        //Checking if there is an Activity that accepts the Intent
        if (webIntent.resolveActivity(context.getPackageManager()) != null) {
            //Launching the corresponding Activity and passing it the Intent
            context.startActivity(webIntent);
        }
    }

    /**
     * Method that opens a Map application for the location address {@code locationAddress} passed.
     *
     * @param context         A {@link Context} of the Calling Activity/Fragment
     * @param locationAddress String containing the specific address or location query
     */
    public static void openMap(Context context, String locationAddress) {
        //Building the Geo Location URI using the URI builder for the location address passed
        Uri.Builder locationUriBuilder = new Uri.Builder()
                .scheme(MAP_URI_SCHEME)
                .path(MAP_PATH_DEFAULT_COORDINATES)
                .appendQueryParameter(MAP_QUERY_PARAMETER, locationAddress);
        //Creating an ACTION_VIEW Intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        //Setting the Location URI generated on the Intent Data
        mapIntent.setData(locationUriBuilder.build());
        //Checking if there is an Activity that accepts the Intent
        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
            //Launching the corresponding Activity and passing it the Intent
            context.startActivity(mapIntent);
        }
    }

    /**
     * Method that opens up an app chooser for sharing the text content passed.
     *
     * @param launchingActivity The {@link FragmentActivity} instance initiating this.
     * @param textToShare       The text String to be shared
     * @param chooserTitle      The Title text to be shown for the chooser dialog
     */
    public static void shareText(FragmentActivity launchingActivity, String textToShare, String chooserTitle) {
        //Building and launching the share intent, to share the text content
        ShareCompat.IntentBuilder
                .from(launchingActivity)
                .setType(TEXT_TYPE_PLAIN)
                .setText(textToShare)
                .setChooserTitle(chooserTitle)
                .startChooser();
    }

    /**
     * Method that creates an Intent to the Phone Dialer to initiate a Phone Call.
     *
     * @param launchingActivity The {@link FragmentActivity} instance initiating this.
     * @param phoneNumber       The Phone Number to dial
     */
    public static void dialPhoneNumber(FragmentActivity launchingActivity, String phoneNumber) {
        //Creating a Phone Dialer Intent
        Intent intent = new Intent(Intent.ACTION_DIAL);
        //Setting the Phone number Uri
        intent.setData(Uri.parse(URI_STR_TELEPHONE_SCHEME + phoneNumber));
        //Checking for an Activity that can handle this Intent
        if (intent.resolveActivity(launchingActivity.getPackageManager()) != null) {
            //Starting the activity that handles the given Intent
            launchingActivity.startActivity(intent);
        }
    }

}

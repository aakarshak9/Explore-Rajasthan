package com.example.aakarshak.explore.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.FragmentActivity;

public class UtilityIntent {

    //Constants used for generating the Geo Location URI for Map Intents
    private static final String MAP_URI_SCHEME = "geo";
    private static final String MAP_PATH_DEFAULT_COORDINATES = "0,0";
    private static final String MAP_QUERY_PARAMETER = "q";

    //Text Data Type Constants
    private static final String TEXT_TYPE_PLAIN = "text/plain";

    //URI Scheme Constants
    private static final String URI_STR_TELEPHONE_SCHEME = "tel:";

    private UtilityIntent() {
        //Suppressing with an error to enforce noninstantiability
        throw new AssertionError("No " + this.getClass().getCanonicalName() + " instances for you!");
    }

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

    public static void shareText(FragmentActivity launchingActivity, String textToShare, String chooserTitle) {
        //Building and launching the share intent, to share the text content
        ShareCompat.IntentBuilder
                .from(launchingActivity)
                .setType(TEXT_TYPE_PLAIN)
                .setText(textToShare)
                .setChooserTitle(chooserTitle)
                .startChooser();
    }

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

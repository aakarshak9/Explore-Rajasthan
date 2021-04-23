package com.example.aakarshak.explore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.cache.Image_bitmap;
import com.example.aakarshak.explore.data.AppClassRepo;
import com.example.aakarshak.explore.ui.about.AboutClassAct;
import com.example.aakarshak.explore.ui.hotels.HotelFragmentl;
import com.example.aakarshak.explore.ui.hotels.HotelPresenterl;
import com.example.aakarshak.explore.ui.parks.ParkFragmentl;
import com.example.aakarshak.explore.ui.parks.ParkPresenterl;
import com.example.aakarshak.explore.ui.places.PlaceFragmentl;
import com.example.aakarshak.explore.ui.places.PlacePresenterl;
import com.example.aakarshak.explore.ui.restaurants.RestoFragmentl;
import com.example.aakarshak.explore.ui.restaurants.RestoPresenterl;
import com.example.aakarshak.explore.ui.shops.ShopFragmentl;
import com.example.aakarshak.explore.ui.shops.ShopPresenterl;
import com.example.aakarshak.explore.utils.UtilityInjector;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    //Bundle Constant for persisting the last viewed BottomNavigationView fragment menu item resource id
    private static final String BUNDLE_LAST_VIEWED_NAV_ITEM_INT_KEY = "Main.LastViewedNavMenuItemResId";

    //The Common PresenterBase Interface implemented by the BottomNavigationView Fragments
    private NaviPresenterBase mNavigationPresenter;
    //For the BottomNavigationView
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Inflating the Activity's UI
        setContentView(R.layout.activity_main);

        //Initialize Toolbar
        setupToolbar();

        //Initialize BottomNavigationView
        setupBottomNavigationView();

        if (savedInstanceState == null) {
            //On Initial Launch, loading the first BottomNavigationView Menu Item - "Places"
            mBottomNavigationView.setSelectedItemId(R.id.navi_places);
        } else {
            //On Subsequent Launch, loading the last viewed BottomNavigationView Menu Item
            mBottomNavigationView.setSelectedItemId(savedInstanceState.getInt(BUNDLE_LAST_VIEWED_NAV_ITEM_INT_KEY));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saving the last viewed BottomNavigationView Menu Item Resource Id
        outState.putInt(BUNDLE_LAST_VIEWED_NAV_ITEM_INT_KEY, mBottomNavigationView.getSelectedItemId());
    }

    private void setupToolbar() {
        //Finding the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        //Setting it as the Action Bar
        setSupportActionBar(toolbar);
    }

    private void setupBottomNavigationView() {
        //Finding the BottomNavigationView
        mBottomNavigationView = findViewById(R.id.navi_main);
        //Registering the Item Selection Listener
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflating the Menu options from 'R.menu.menu_main'
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Returning true for the Menu to be displayed
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handling based on the Menu item selected
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //On Click of Refresh Menu

                if (mNavigationPresenter != null) {
                    //When PresenterBase is initialized, invoke the Refresh method
                    mNavigationPresenter.onRefreshMenuClicked();
                }
                return true;
            case R.id.action_about:
                //On Click of About

                //Launch the About Activity
                launchAboutActivity();
                return true;
            default:
                //On other cases, do the default menu handling
                return super.onOptionsItemSelected(item);
        }
    }

    private void launchAboutActivity() {
        //Creating an Intent to launch AboutClassAct
        Intent aboutIntent = new Intent(this, AboutClassAct.class);
        //Starting the Activity
        startActivity(aboutIntent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Retrieving the App Repository Instance
        AppClassRepo appClassRepo = UtilityInjector.provideAppRepository(getApplicationContext());

        //Taking action based on the Id of the Menu item clicked
        switch (item.getItemId()) {
            case R.id.navi_places:
                //For Places

                //Load the PlaceFragmentl and its PresenterBase
                mNavigationPresenter = new PlacePresenterl(appClassRepo,
                        (PlaceFragmentl) switchNavigationFragment(PlaceFragmentl.newInstance()));
                return true;

            case R.id.navi_parks:
                //For Parks

                //Load the ParkFragmentl and its PresenterBase
                mNavigationPresenter = new ParkPresenterl(appClassRepo,
                        (ParkFragmentl) switchNavigationFragment(ParkFragmentl.newInstance()));
                return true;

            case R.id.navigation_hotels:
                //For Hotels

                //Load the HotelFragmentl and its PresenterBase
                mNavigationPresenter = new HotelPresenterl(appClassRepo,
                        (HotelFragmentl) switchNavigationFragment(HotelFragmentl.newInstance()));
                return true;

            case R.id.navi_resturant:
                //For Restaurants

                //Load the RestoFragmentl and its PresenterBase
                mNavigationPresenter = new RestoPresenterl(appClassRepo,
                        (RestoFragmentl) switchNavigationFragment(RestoFragmentl.newInstance()));
                return true;

            case R.id.navi_shops:
                //For Shops

                //Load the ShopFragmentl and its PresenterBase
                mNavigationPresenter = new ShopPresenterl(appClassRepo,
                        (ShopFragmentl) switchNavigationFragment(ShopFragmentl.newInstance()));
                return true;
        }
        //On all else, returning False
        return false;
    }

    private Fragment switchNavigationFragment(Fragment fragment) {
        //Retrieving the Fragment Manager
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        //Retrieving the current Fragment at the FrameLayout 'R.id.content_main'
        Fragment currentFragment = supportFragmentManager.findFragmentById(R.id.content_main);
        //Checking if any Fragment exists at the FrameLayout 'R.id.content_main'
        if (currentFragment == null || !fragment.getClass().isInstance(currentFragment)) {
            //When there is NO Fragment or the Fragment is different from the fragment to be loaded

            //Replace the Current Fragment with the given Fragment at the FrameLayout 'R.id.content_main'
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
        } else {
            //When there is a Fragment already and is same as the given Fragment,
            //then return the Fragment found
            return currentFragment;
        }
        //Return the New Fragment loaded
        return fragment;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) {
            //Clearing the Bitmap Cache when the activity is finishing
            Image_bitmap.clearCache();
        }
    }

}

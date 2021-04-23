package com.example.aakarshak.explore.ui.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.aakarshak.explore.R;
import com.example.aakarshak.explore.utils.UtilityIntent;
import com.example.aakarshak.explore.utils.UtilityText;

public class AboutClassAct extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_act);

        //Initializing the Toolbar
        setupToolbar();

        //Initializing the Content Title Text
        initContentTitleText();

        //Initializing the Content Intro Text
        initContentIntroText();

        //Initializing the Content Description Text
        initContentDescriptionText();

        //Initializing the ImageViews and registering the click listeners
        initImageViews();
    }

    private void setupToolbar() {
        //Finding the Custom Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_about);

        //Setting the toolbar as the ActionBar
        setSupportActionBar(toolbar);

        //Enable the Up button navigation
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            //Enabling the home button for Up Action
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            //Changing the Up button icon to close
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_close_about);
        }
    }

    private void initContentTitleText() {
        //Finding the TextView
        TextView textViewContentTitle = findViewById(R.id.text_about_content_title);
        //Setting the Typeface
        textViewContentTitle.setTypeface(ResourcesCompat.getFont(this, R.font.lobster));
    }

    private void initContentIntroText() {
        //Finding the TextView
        TextView textViewContentIntro = findViewById(R.id.text_about_content_intro);
        //Setting the Html Content
        UtilityText.setHtmlText(textViewContentIntro, getString(R.string.content_intro));
        //Setting the Typeface
        textViewContentIntro.setTypeface(ResourcesCompat.getFont(this, R.font.noticia_text));
    }

    private void initContentDescriptionText() {
        //Finding the TextView
        TextView textViewContentDescription = findViewById(R.id.text_about_content_desc);
        //Setting the Html Content
        UtilityText.setHtmlText(textViewContentDescription, getString(R.string.content_desc));
        //Making the embedded links clickable
        textViewContentDescription.setMovementMethod(LinkMovementMethod.getInstance());
        //Setting the Typeface
        textViewContentDescription.setTypeface(ResourcesCompat.getFont(this, R.font.noticia_text));
    }

    private void initImageViews() {
        //Finding the ImageViews
        ImageView imageViewUdacity = findViewById(R.id.image_about_udacity);
        ImageView imageViewGithub = findViewById(R.id.image_about_github);
        ImageView imageViewLinkedIn = findViewById(R.id.image_about_linkedin);

        //Registering Click Listener on these Views
        imageViewUdacity.setOnClickListener(this);
        imageViewGithub.setOnClickListener(this);
        imageViewLinkedIn.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handling the Menu Item selected based on their Id
        switch (item.getItemId()) {
            case android.R.id.home:
                //Handling the action bar's home/up button
                finish(); //Finishing the Activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        //Executing action based on V's id
        switch (view.getId()) {
            case R.id.image_about_udacity:
                //For the Udacity ImageView

                //Opens the webpage for the course provided by Udacity
                UtilityIntent.openLink(this, getString(R.string.udacity_link));
                break;
            case R.id.image_about_github:
                //For the GitHub ImageView

                //Opens my Github profile
                UtilityIntent.openLink(this, getString(R.string.github_link));
                break;
            case R.id.image_about_linkedin:
                //For the LinkedIn ImageView

                //Opens my LinkedIn profile
                UtilityIntent.openLink(this, getString(R.string.linkedin_link));
                break;
        }
    }
}

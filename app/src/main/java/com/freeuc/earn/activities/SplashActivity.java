package com.freeuc.earn.activities;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;

import com.freeuc.earn.R;

import com.parse.ParseUser;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AwesomeSplash {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    public void initSplash(ConfigSplash configSplash) {

        /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(200); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.RotateIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash(getString(R.string.app_name)+" ");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(30f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("fonts/streatwear.otf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        getCurrentUser();

    }
    public void getCurrentUser() {
        // After login, Parse will cache it on disk, so
        // we don't need to login every time we open this
        // application
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this,MainActivity.class));
        } else {
            Intent intent = new Intent(this,AuthActivity.class);
            startActivity(intent);
        }
    }
}

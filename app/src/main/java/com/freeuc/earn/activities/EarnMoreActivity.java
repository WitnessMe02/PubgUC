package com.freeuc.earn.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.freeuc.earn.R;
import com.parse.ParseConfig;
import com.parse.ParseUser;

public class EarnMoreActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earn_more);
        if(!user.getBoolean("rate_us")){
            findViewById(R.id.rate_us).setOnClickListener(this);
            findViewById(R.id.rate_us).setEnabled(true);
        }
        if(!user.getBoolean("sub_us")){
            findViewById(R.id.subs_us).setOnClickListener(this);
            findViewById(R.id.subs_us).setEnabled(true);
        }
        if(!user.getBoolean("watch_video")){
            findViewById(R.id.watch_video).setOnClickListener(this);
            findViewById(R.id.watch_video).setEnabled(true);
        }


    }

    @Override
    public void onClick(View view) {
        String url = "";
        switch (view.getId()){
            case R.id.rate_us:
                url = ParseConfig.getCurrentConfig().getString("RateUsURL");
                ParseUser.getCurrentUser().put("rate_us",true);
                break;
            case R.id.subs_us:
                url = ParseConfig.getCurrentConfig().getString("SubscribeUsURL");
                ParseUser.getCurrentUser().put("sub_us",true);
                break;
            case R.id.watch_video:
                url = ParseConfig.getCurrentConfig().getString("WatchVideo");
                ParseUser.getCurrentUser().put("watch_video",true);
                break;
        }
        ParseUser.getCurrentUser().saveInBackground();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        addWinning(10);

        view.setEnabled(false);
    }
}

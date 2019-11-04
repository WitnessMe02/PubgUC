package com.freeuc.earn;

import com.parse.Parse;
import android.app.Application;

public class PubgUC extends Application {

  // Initializes Parse SDK as soon as the application is created
  @Override
  public void onCreate() {
    super.onCreate();

    Parse.initialize(new Parse.Configuration.Builder(this)
      .applicationId("gP5sAvSS69pHSHVmrEyvHZugc6Ary3PdnpbLbsDj")
      .clientKey("0j6eJw3eHCHj8eKSgH53UHI7feqyQWz3P47vxa6z")
      .server("https://parseapi.back4app.com")
      .build()
    );
    StaticData.refreshConfig();
  }
}
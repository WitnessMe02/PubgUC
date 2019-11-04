package com.freeuc.earn;

import com.parse.ConfigCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;

public class StaticData {
    public static int minimumWithAmount;
    public static String how_it_works;

    StaticData(){
        refreshConfig();
    }
    public static void refreshConfig() {
        ParseConfig.getInBackground();
    }




}

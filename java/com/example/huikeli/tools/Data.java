package com.example.huikeli.tools;

import android.app.Application;

/**
 * Created by huikeli on 2017/4/23.
 */

public class Data extends Application {
    private String url;

    public String getUrl(){
        return this.url;
    }
    public void setUrl(String c){
        this.url= c;
    }
    @Override
    public void onCreate(){
        url = "http://hw80608-env.us-west-2.elasticbeanstalk.com?";
        super.onCreate();
    }
}

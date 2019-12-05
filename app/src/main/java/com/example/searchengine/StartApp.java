package com.example.searchengine;

import android.app.Application;

import com.parse.Parse;

public class StartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("477a5aa66c09b3d8613a089f20f8a7b995438894")
                .clientKey("48227949571c2199edfe123f61bb4aecc8e25c5c")
                .server("http://34.211.8.224:80/parse/")
                .build()
        );

    }
}

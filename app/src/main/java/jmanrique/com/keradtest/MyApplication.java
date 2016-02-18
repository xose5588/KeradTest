package jmanrique.com.keradtest;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Jose on 18/02/2016.
 */
public class MyApplication extends Application {

    public static final String CONSUMER_KEY = "YcNrAKYevb7vpLdYMI0JAOR3Z";
    public static final String CONSUMER_SECRET = "ZrtXDEzcpg3EuRoyhFGRLcAJpsW8S5p883RQBoKfM2QkQxZcUC";

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
package jmanrique.com.keradtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    public static final String TWITTER_KEY = "YcNrAKYevb7vpLdYMI0JAOR3Z";
    public static final String TWITTER_SECRET = "ZrtXDEzcpg3EuRoyhFGRLcAJpsW8S5p883RQBoKfM2QkQxZcUC";
    private static final long tweetId = 631879971628183552L;
    private TwitterLoginButton loginButton;
    private TwitterSession session;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = result.data;
                Toast.makeText(getApplicationContext(), "Logged: " + session.getUserName(), Toast.LENGTH_LONG).show();
                loadTweet(tweetId);
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void loadTweet(long tweetId){
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {


                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                // Can also use Twitter directly: Twitter.getApiClient()
/*                StatusesService statusesService = twitterApiClient.getStatusesService();
                statusesService.show(524971209851543553L, null, null, null, new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        Log.d("dfdas", "afads");
                        //Do something with result, which provides a Tweet inside of result.data
                    }

                    public void failure(TwitterException exception) {
                        Log.d("dfdas", "afads");
                        //Do something on failure
                    }
                });
*/
                SearchService searchService = twitterApiClient.getSearchService();
                searchService.tweets("It's 10:18 and", null, null, null, null, 10, null, null, null, null, new Callback<Search>() {
                    @Override
                    public void success(Result<Search> result) {
                        Log.d("dfdas", "afads");
                        //Do something with result, which provides a Tweet inside of result.data
                    }

                    public void failure(TwitterException exception) {
                        Log.d("dfdas", "afads");
                        //Do something on failure
                    }
                });


//                TweetView tweetView = new TweetView(MainActivity.this, result.data);
//                addTweetToView(tweetView);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });
    }

    private void addTweetToView(TweetView tweetView){
        final ViewGroup parentView = (ViewGroup) getWindow().getDecorView().getRootView();
        parentView.addView(tweetView);
    }
}

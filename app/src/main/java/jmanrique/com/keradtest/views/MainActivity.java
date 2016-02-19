package jmanrique.com.keradtest.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import jmanrique.com.keradtest.R;
import jmanrique.com.keradtest.entities.TweetComparable;

public class MainActivity extends Activity {

    private TwitterLoginButton loginButton;
    private Button tryAgainButton;
    private RelativeLayout contentLayout;
    private TwitterSession session;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        contentLayout = (RelativeLayout)findViewById(R.id.content_view);
        tryAgainButton = (Button) findViewById(R.id.try_again_btn);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Tweet> tweets;

                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                String formatted = format1.format(Calendar.getInstance().getTime());

                searchTweets("\"It's " + formatted + " and\"");
            }
        });

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = result.data;
                Toast.makeText(getApplicationContext(), "Logged: " + session.getUserName(), Toast.LENGTH_LONG).show();

                tryAgainButton.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);

                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
                String formatted = format1.format(Calendar.getInstance().getTime());

                searchTweets("\"It's " + formatted + " and\"");
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

    private void searchTweets(String text){

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        SearchService searchService = twitterApiClient.getSearchService();
        final ArrayList<Tweet> res = new ArrayList<>();
        searchService.tweets(text, null, null, null, null, null, null, null, null, null, new Callback<Search>() {
            @Override
            public void success(Result<Search> result) {
                if (result.data.tweets.isEmpty()) {
                    tryAgainButton.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "No tweets found", Toast.LENGTH_SHORT).show();
                } else {
                    tryAgainButton.setVisibility(View.GONE);
                    List<TweetComparable> resultSearch = new ArrayList<>();
                    for (Tweet t : result.data.tweets) {
                        resultSearch.add(new TweetComparable(t));
                    }
                    Collections.sort(resultSearch, TweetComparable.sortByRetweetCount());
                    Collections.sort(resultSearch, TweetComparable.sortByNewer());

                    loadTweet(result.data.tweets.get(0).id);

                }
            }

            public void failure(TwitterException exception) {
                Toast.makeText(context, "Failure searching tweets", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTweet(long tweetId){
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                TweetView tweetView = new TweetView(MainActivity.this, result.data);
                addTweetToView(tweetView);
            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(context, "Failure loading tweet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTweetToView(TweetView tweetView){
        contentLayout.addView(tweetView);
    }
}

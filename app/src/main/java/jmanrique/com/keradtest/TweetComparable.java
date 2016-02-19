package jmanrique.com.keradtest;

/**
 * Created by Jose on 19/02/2016.
 */

import android.annotation.TargetApi;
import android.os.Build;

import com.twitter.sdk.android.core.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

public class TweetComparable extends Tweet {


    public TweetComparable(Tweet t){
        super(t.coordinates, t.createdAt, t.currentUserRetweet, t.entities, t.extendedEtities, t.favoriteCount, t.favorited, t.filterLevel, t.id, t.idStr, t.inReplyToScreenName, t.inReplyToStatusId, t.inReplyToStatusIdStr, t.inReplyToUserId, t.inReplyToUserIdStr, t.lang, t.place, t.possiblySensitive, t.scopes, t.retweetCount, t.retweeted, t.retweetedStatus, t.source, t.text, t.truncated, t.user, t.withheldCopyright, t.withheldInCountries, t.withheldScope);
    }

    static Comparator<TweetComparable> sortByRetweetCount() {
        return new Comparator<TweetComparable>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public int compare(TweetComparable one, TweetComparable two) {
                return Integer.compare(one.retweetCount, two.retweetCount);
            }
        };
    }

    static Comparator<TweetComparable> sortByNewer() {
        return new Comparator<TweetComparable>() {
            @Override
            public int compare(TweetComparable one, TweetComparable two) {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US);
                try {
                    cal1.setTime(sdf.parse(one.createdAt));
                    cal2.setTime(sdf.parse(two.createdAt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return cal1.compareTo(cal2);
            }
        };
    }

}
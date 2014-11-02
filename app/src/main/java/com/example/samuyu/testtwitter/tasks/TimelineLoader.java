package com.example.samuyu.testtwitter.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.Collections;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by toyamaosamuyu on 2014/11/02.
 */
public class TimelineLoader extends AsyncTaskLoader<List<Status>>{

    private static final String TAG = TimelineLoader.class.getSimpleName();
    private Twitter mTwitter;

    public TimelineLoader(Context context, Twitter twitter) {
        super(context);
        mTwitter = twitter;
    }

    @Override
    public List<Status> loadInBackground() {

        List<Status> timelineList;

        try {
            timelineList = mTwitter.getHomeTimeline();
        } catch (TwitterException e) {
            timelineList = Collections.emptyList();
            e.printStackTrace();
        }

        return timelineList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad(); //これがないと動かなかった
    }

}

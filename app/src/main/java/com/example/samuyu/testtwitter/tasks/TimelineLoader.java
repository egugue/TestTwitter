package com.example.samuyu.testtwitter.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.samuyu.testtwitter.entities.Tweet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by toyamaosamuyu on 2014/11/02.
 */
public class TimelineLoader extends AsyncTaskLoader<List<Tweet>>{

    private static final String TAG = TimelineLoader.class.getSimpleName();
    private Twitter mTwitter;
    private int paging = 1;

    public TimelineLoader(Context context, Twitter twitter, int paging) {
        super(context);
        mTwitter = twitter;
        this.paging = paging;
    }

    @Override
    public List<Tweet> loadInBackground() {

        List<Tweet> timelineList = new ArrayList<Tweet>();

        try {

            List<Status> statusList;

            if (paging == 1) {
                statusList = mTwitter.getHomeTimeline();
            } else {
                Paging p = new Paging();
                p.setPage(paging);
                statusList = mTwitter.getHomeTimeline(p);
            }

            for (Status status : statusList) {
                timelineList.add(Tweet.fromStatus(status));
            }

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return timelineList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad(); //これがないと動かなかった
    }


}

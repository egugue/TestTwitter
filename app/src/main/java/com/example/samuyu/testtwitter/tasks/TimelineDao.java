package com.example.samuyu.testtwitter.tasks;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.events.BusProvider;
import com.example.samuyu.testtwitter.events.LoadTweetTimelineCompleteEvent;
import com.example.samuyu.testtwitter.utils.TwitterUtil;

import java.util.List;

import twitter4j.Twitter;

/**
 * Created by toyamaosamuyu on 2014/11/25.
 */
public class TimelineDao {
    private static final String TAG = TimelineDao.class.getSimpleName();

    private LoaderManager mLoaderManager;
    private Context mContext;
    private int LOADER_ID_LOADING_TIMELINE = 0;
    private int page = 1;
    private Twitter mTwitter;

    private TimelineDao() {
        // no instances
    }

    public TimelineDao(Fragment fragment) {
        mLoaderManager = fragment.getLoaderManager();
        mContext = fragment.getActivity().getApplicationContext();
    }

    public void requestTimeline() {

        page++;

        mLoaderManager.restartLoader(LOADER_ID_LOADING_TIMELINE, null, new LoaderManager.LoaderCallbacks<List<Tweet>>() {
            @Override
            public Loader<List<Tweet>> onCreateLoader(int id, Bundle args) {
                Log.d("HOGE", "onCreateLoader");

                if (mTwitter == null) {
                    mTwitter = TwitterUtil.getTwitterInstance(mContext);
                }

                Log.d("HOGE", "paging = " + page + "");
                return new TimelineLoader(mContext, mTwitter, page);
            }

            @Override
            public void onLoadFinished(Loader<List<Tweet>> loader, List<Tweet> data) {

                BusProvider.getInstance().post(new LoadTweetTimelineCompleteEvent(data));
                Log.d("HOGE", "onLoadFinishedaa");
                mLoaderManager.destroyLoader(LOADER_ID_LOADING_TIMELINE);
            }

            @Override
            public void onLoaderReset(Loader<List<Tweet>> loader) {
                //nothing to do
            }

        });
    }
}

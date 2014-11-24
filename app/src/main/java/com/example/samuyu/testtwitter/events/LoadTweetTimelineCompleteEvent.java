package com.example.samuyu.testtwitter.events;

import com.example.samuyu.testtwitter.entities.Tweet;

import java.util.List;

/**
 * Created by toyamaosamuyu on 2014/11/25.
 */
public class LoadTweetTimelineCompleteEvent {
    private static final String TAG = LoadTweetTimelineCompleteEvent.class.getSimpleName();
    private List<Tweet> mTimelineList;

    public LoadTweetTimelineCompleteEvent(List<Tweet> timelineList) {
        mTimelineList = timelineList;
    }

    public List<Tweet> getTimelineList() {
        return mTimelineList;
    }

}

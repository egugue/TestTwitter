package com.example.samuyu.testtwitter.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.adapters.TimelineArrayAdapter;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.tasks.TimelineLoader;
import com.example.samuyu.testtwitter.utils.TwitterUtil;

import java.util.List;

public class TimelineFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Tweet>> {

    private static int LOADER_ID_LOADING_TIMELINE = 0;

    private OnTimelineSelectedListener mCallback;
    private Context mContext;
    private TimelineArrayAdapter mAdapter;

    public interface OnTimelineSelectedListener {
        public void onTweetSelected(Tweet tweet);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTimelineSelectedListener) activity;
        } catch (ClassCastException e) {

            throw new ClassCastException(
                    activity.toString() + "にOnHeadlineSelectedListenerが実装されていません。必ず実装してください。"
            );

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        mAdapter = new TimelineArrayAdapter(mContext);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID_LOADING_TIMELINE,null, this);

        return view;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onTweetSelected(mAdapter.getItem(position));
    }

    @Override
    public Loader<List<Tweet>> onCreateLoader(int id, Bundle args) {
        return new TimelineLoader(mContext, TwitterUtil.getTwitterInstance(mContext), -1);
    }

    @Override
    public void onLoadFinished(Loader<List<Tweet>> loader, List<Tweet> data) {
        mAdapter.clear();
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Tweet>> loader) {
    }
}

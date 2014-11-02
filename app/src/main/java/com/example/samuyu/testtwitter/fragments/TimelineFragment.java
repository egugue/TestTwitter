package com.example.samuyu.testtwitter.fragments;



import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.adapters.TimelineAdapter;
import com.example.samuyu.testtwitter.tasks.TimelineLoader;
import com.example.samuyu.testtwitter.utils.TwitterUtil;
import com.example.samuyu.testtwitter.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;

public class TimelineFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Status>>{

    private static int LOADER_ID_LOADING_TIMELINE = 0;

    private RecyclerView mTimelineView;
    private TimelineAdapter mAdapter;
    private List<Status> mTimelineList;
    private Twitter mTwitter;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        mTimelineList = new ArrayList<Status>();
        mAdapter = new TimelineAdapter(mTimelineList);

        mTimelineView = (RecyclerView)view.findViewById(R.id.tweet_timeline_list);
        mTimelineView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mTimelineView.setAdapter(mAdapter);

        mDividerItemDecoration = new DividerItemDecoration(getActivity().getApplicationContext());

        getLoaderManager().initLoader(LOADER_ID_LOADING_TIMELINE, null, this);

        Log.d("HOGE", "onCreateView");

        return view;
    }


    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super(savedInstanceState);
    }
    */

    @Override
    public Loader<List<Status>> onCreateLoader(int id, Bundle args) {

        Log.d("HOGE", "onCreateLoader");

        Context context = getActivity().getApplicationContext();

        if (mTwitter == null) {
            mTwitter = TwitterUtil.getTwitterInstance(context);
        }

        return new TimelineLoader(getActivity().getApplicationContext(), mTwitter);
    }

    @Override
    public void onLoadFinished(Loader<List<Status>> loader, List<Status> data) {

        for (Status status : data) {
//            Log.d("HOGE", status.toString());
            mAdapter.addToList(status, 0);
            mTimelineView.addItemDecoration(mDividerItemDecoration);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Status>> loader) {

    }

}

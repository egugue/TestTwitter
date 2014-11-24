package com.example.samuyu.testtwitter.fragments;



import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.activities.TweetDetailActivity;
import com.example.samuyu.testtwitter.adapters.TimelineAdapter;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.events.BusProvider;
import com.example.samuyu.testtwitter.events.LoadTweetTimelineCompleteEvent;
import com.example.samuyu.testtwitter.listeners.MoreLoadRecycleListener;
import com.example.samuyu.testtwitter.tasks.TimelineDao;
import com.example.samuyu.testtwitter.tasks.TimelineLoader;
import com.example.samuyu.testtwitter.utils.TwitterUtil;
import com.example.samuyu.testtwitter.views.DividerItemDecoration;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import twitter4j.Status;
import twitter4j.Twitter;

public class TimelineRecycleFragment extends Fragment{

    private static int LOADER_ID_LOADING_TIMELINE = 0;

    @InjectView(R.id.tweet_timeline_list)
    RecyclerView mTimelineRecyclerView;
    @InjectView(R.id.footer_menu)
    RelativeLayout mfooterMenuLayout;

    private TimelineAdapter mAdapter;
    private List<Status> mTimelineList;
    private Twitter mTwitter;
    private DividerItemDecoration mDividerItemDecoration;
    private Context mContext;
    private OnRecycleViewClickListener mRecycleViewClickListener;
    private int paging = 1;
    private TimelineDao mTimelineDao;

    public interface OnRecycleViewClickListener {
        public void onRescycleViewClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //BusProvider.getInstance().register(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.inject(this, view);

        mContext = getActivity().getApplicationContext();


        final Animation inAnim  = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        final Animation outAnim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);

        mTimelineList = new ArrayList<Status>();
        mAdapter = new TimelineAdapter(mContext, new TimelineAdapter.OnItemClickListener() {
            @Override
            public void onClick(Tweet tweet) {
                Intent intent = TweetDetailActivity.createIntent(mContext, tweet);
                startActivity(intent);
        }
        });

        mTimelineRecyclerView.setHasFixedSize(true);
        mTimelineRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mTimelineRecyclerView.setAdapter(mAdapter);
        mTimelineRecyclerView.setItemAnimator(null);

        mDividerItemDecoration = new DividerItemDecoration(mContext);
//        mTimelineDao = new TimelineDao(this);
//        mTimelineDao.requestTimeline();
        requestTweetTimeline();

        mTimelineRecyclerView.setOnScrollListener(new MoreLoadRecycleListener(mTimelineRecyclerView, null) {
            @Override
            public void onLoadMore() {
                Log.d("aiu", "onLoadMore");
                requestTweetTimeline();
                //mTimelineDao.requestTimeline();
            }

            @Override
            public void onUpScroll() {
                if (mfooterMenuLayout.getVisibility() == View.GONE) {
                    mfooterMenuLayout.startAnimation(inAnim);
                    mfooterMenuLayout.setVisibility(View.VISIBLE);
                    return;
                }
            }

            @Override
            public void onDownScroll() {
                if (mfooterMenuLayout.getVisibility() == View.VISIBLE) {
                    mfooterMenuLayout.startAnimation(outAnim);
                    mfooterMenuLayout.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
//        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }

    @Subscribe
    public void onLoadTweetTimelineComplete(LoadTweetTimelineCompleteEvent event) {

        List<Tweet> timeline = event.getTimelineList();
        Log.d("onLoadFinished", timeline.get( timeline.size()-1 ).text);

        mTimelineRecyclerView.removeItemDecoration(mDividerItemDecoration);
        mTimelineRecyclerView.addItemDecoration(mDividerItemDecoration);
        mAdapter.addAll(timeline);
        getLoaderManager().destroyLoader(LOADER_ID_LOADING_TIMELINE);
    }


    private void requestTweetTimeline() {
        getLoaderManager().restartLoader(LOADER_ID_LOADING_TIMELINE, null, new LoaderManager.LoaderCallbacks<List<Tweet>>() {
            @Override
            public Loader<List<Tweet>> onCreateLoader(int id, Bundle args) {
                Log.d("HOGE", "onCreateLoader");

                if (mTwitter == null) {
                    mTwitter = TwitterUtil.getTwitterInstance(mContext);
                }

                Log.d("HOGE", "paging = "+paging+"");
                return new TimelineLoader(getActivity().getApplicationContext(), mTwitter, paging);
            }

            @Override
            public void onLoadFinished(Loader<List<Tweet>> loader, List<Tweet> data) {
                Log.d("HOGE", "onLoadFinished");

                mTimelineRecyclerView.removeItemDecoration(mDividerItemDecoration);
                mTimelineRecyclerView.addItemDecoration(mDividerItemDecoration);
                mAdapter.addAll(data);
                getLoaderManager().destroyLoader(LOADER_ID_LOADING_TIMELINE);
                Log.d("onLoadFinished", data.get( data.size()-1 ).text);
                paging++;
            }

            @Override
            public void onLoaderReset(Loader<List<Tweet>> loader) {
                //nothing to do
            }

        });
    }
}

package com.example.samuyu.testtwitter.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.tasks.TimelineLoader;

import java.util.ArrayList;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;

/**
 * Created by toyamaosamuyu on 2014/11/02.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>{

    private static final String TAG = TimelineAdapter.class.getSimpleName();

    private List<Status> mTimelineList;

    public TimelineAdapter(List<Status> timelineList) {
        mTimelineList = timelineList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_item_tweet_no_image, null);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        Status status = mTimelineList.get(i);

        viewHolder.nickname.setText( status.getUser().getName() );
        viewHolder.username.setText( status.getUser().getScreenName() );
        viewHolder.tweetText.setText( status.getText() );

    }

    @Override
    public int getItemCount() {
        return mTimelineList.size();
    }


    public void addToList(Status status, int index) {

        mTimelineList.add(index, status);
        notifyItemInserted(index);
    }

    public void removeFromList(Status status, int index) {

        mTimelineList.remove(index);
        notifyItemRemoved(index);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView nickname;
        public TextView created;
        public TextView tweetText;
        public ImageView profileIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            username = (TextView)itemView.findViewById(R.id.username);
            nickname = (TextView)itemView.findViewById(R.id.nickname);
            created = (TextView)itemView.findViewById(R.id.tweet_created);
            tweetText = (TextView)itemView.findViewById(R.id.tweet_text);
            profileIcon = (ImageView)itemView.findViewById(R.id.user_profile_icon);
        }
    }
}

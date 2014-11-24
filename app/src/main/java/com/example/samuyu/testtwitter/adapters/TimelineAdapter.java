package com.example.samuyu.testtwitter.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.views.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by toyamaosamuyu on 2014/11/02.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private static final String TAG = TimelineAdapter.class.getSimpleName();
    private static final CircleTransformation CIRCLE_TRANSFORMATION = new CircleTransformation();

    private List<Tweet> mTimelineList = new ArrayList<Tweet>();
    private Context mContext;
    private OnItemClickListener mItemClickListener;

    public static interface OnItemClickListener {
        public void onClick(Tweet tweet);
    }

    public TimelineAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        int layout = R.layout.list_item_tweet_like_voice;
        //int layout = R.layout.list_item_tweet_no_image;
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(layout, null);

        ViewHolder holder = new ViewHolder(view, mContext, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int location) {
        Tweet tweet = mTimelineList.get(location);
        viewHolder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mTimelineList.size();
    }

    public Tweet getItem(int index) {
       return mTimelineList.get(index);
    }

    public void setTweetList(List<Tweet> list) {
        mTimelineList = list;
    }

    public void addAll(List<Tweet> list) {

        int positionStart = mTimelineList.size();
        int itemCount = list.size();

        for(Tweet tweet : list) {
           mTimelineList.add(tweet);
        }

        //notifyDataSetChanged();
        notifyItemRangeRemoved(positionStart, itemCount);
    }

    public void addToList(Tweet tweet, int index) {
        mTimelineList.add(index, tweet);
        notifyItemInserted(index);
    }

    public void removeFromList(Tweet tweet, int index) {
        mTimelineList.remove(index);
        notifyItemRemoved(index);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView nickname;
        public TextView created;
        public TextView tweetText;
        public ImageView profileIcon;

        private View view;
        private OnItemClickListener onItemClickListener;
        private Context context;

        public ViewHolder(View itemView, Context context, OnItemClickListener onItemClickListener) {
            super(itemView);

            username = (TextView) itemView.findViewById(R.id.username);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            created = (TextView) itemView.findViewById(R.id.tweet_created);
            tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
            profileIcon = (ImageView) itemView.findViewById(R.id.user_profile_icon);

            this.view = itemView;
            this.onItemClickListener = onItemClickListener;
            this.context = context;
        }

        public void bind(final Tweet tweet) {

            nickname.setText(tweet.nickname);
            username.setText(tweet.nameId);
            tweetText.setText(tweet.text);

            Picasso.with(context)
                    .load(tweet.profileImageUrl)
                    .transform(CIRCLE_TRANSFORMATION)
                    .into(profileIcon);

            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(tweet);
                }
            });
        }

    }


}

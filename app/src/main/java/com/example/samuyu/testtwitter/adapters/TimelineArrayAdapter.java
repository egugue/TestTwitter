package com.example.samuyu.testtwitter.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.views.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by toyamaosamuyu on 2014/11/08.
 */
public class TimelineArrayAdapter extends ArrayAdapter<Tweet>{

    private static final String TAG = TimelineArrayAdapter.class.getSimpleName();
    private static final CircleTransformation CIRCLE_TRANSFORMATION = new CircleTransformation();


    public TimelineArrayAdapter(Context context) {
        super(context, -1, new ArrayList<Tweet>());
    }

    public TimelineArrayAdapter(Context context, ArrayList<Tweet> list) {
        super(context, -1, list);
    }


    private static class ViewHolder {
        TextView nameId;
        TextView nickname;
        TextView created;
        TextView tweetText;
        ImageView profileIcon;

        public ViewHolder(View view) {
            this.nameId = (TextView)view.findViewById(R.id.username);
            this.nickname = (TextView)view.findViewById(R.id.nickname);
            this.created = (TextView)view.findViewById(R.id.tweet_created);
            this.tweetText = (TextView)view.findViewById(R.id.tweet_text);
            this.profileIcon = (ImageView)view.findViewById(R.id.user_profile_icon);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_item_tweet_like_voice, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        Tweet tweet = getItem(position);

        holder.nameId.setText(tweet.nameId);
        holder.nickname.setText(tweet.nickname);
        holder.tweetText.setText(tweet.text);
        //holder.created.setText(tweet.created);

        Picasso.with(getContext())
                .load(tweet.profileImageUrl)
                .transform(CIRCLE_TRANSFORMATION)
                .into(holder.profileIcon);

        return convertView;
    }
}

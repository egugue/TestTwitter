package com.example.samuyu.testtwitter.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.views.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TweetDetailFragment extends Fragment {

    private Context mContext;

    @InjectView(R.id.thumbnail_image)
    ImageView mThumbnailImageView;
    @InjectView(R.id.user_profile_icon)
    ImageView mProfileIconImageView;
    @InjectView(R.id.tweet_text)
    TextView mTweetTextView;
    @InjectView(R.id.nickname)
    TextView mNicnameTextView;
    @InjectView(R.id.username)
    TextView mUserNameTextView;

    public static TweetDetailFragment openFragment(Tweet tweet) {
        TweetDetailFragment fragment = new TweetDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("tweetData", tweet);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_tweet_detail, container, false);
        Bundle bundle = getArguments();
        if (bundle == null  ) {
            Toast.makeText(mContext, "error1", Toast.LENGTH_SHORT).show();
            if ( bundle.get("tweetData") == null) {
                Toast.makeText(mContext, "error2", Toast.LENGTH_SHORT).show();
            }
            return view;
        }

        ButterKnife.inject(this, view);
        Tweet tweet = (Tweet) ( bundle.get("tweetData"));
        setupViews(tweet);

        return view;
    }


    private void setupViews(Tweet tweet) {

        mUserNameTextView.setText(tweet.nameId);
        mTweetTextView.setText(tweet.text);
        mNicnameTextView.setText(tweet.nickname);

        Picasso.with(mContext)
                .load(tweet.profileImageUrl)
                .transform(new CircleTransformation())
                .into(mProfileIconImageView);

        if ( ! tweet.imageThumbnailUrlList.isEmpty() ) {

            Picasso.with(mContext)
                    .load(tweet.imageThumbnailUrlList.get(0))
                    .into(mThumbnailImageView);

            mThumbnailImageView.setVisibility(View.VISIBLE);
        }

    }

}

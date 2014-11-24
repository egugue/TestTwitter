package com.example.samuyu.testtwitter.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.fragments.TimelineFragment;
import com.example.samuyu.testtwitter.fragments.TweetDetailFragment;
import com.example.samuyu.testtwitter.utils.TwitterUtil;
import com.example.samuyu.testtwitter.views.CircleTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;


public class MyActivity extends ActionBarActivity implements TimelineFragment.OnTimelineSelectedListener{

    private static final String TAG = MyActivity.class.getSimpleName();
    private static final int LAYOUT_ONE_COLUMN = 1;
    private static final int LAYOUT_TWO_COLUMN = 2;

    private int currentLayout = LAYOUT_ONE_COLUMN;
    private ActionBarDrawerToggle mDrawerToggle;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //ログイン済みじゃない場合は、ログイン画面に遷移させる。
        if (! TwitterUtil.hasAccessToken(this)) {
            Intent intent = OAuthActivity.createIntent(getApplicationContext());
            startActivity(intent);
            finish();
        }

        currentLayout = getLayoutType();

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle); //これでタップしたときに、三が←にかわる

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //これがないと、そもそも三がでない
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        setupDrawer();
    }


    private void setupDrawer() {

        Twitter twitter = TwitterUtil.getTwitterInstance(this);
        final ImageView profileImageView = (ImageView)findViewById(R.id.user_profile_icon);
        final TextView userIdTextView = (TextView)findViewById(R.id.username);
        final TextView nicknameTextView = (TextView)findViewById(R.id.nickname);
        final RelativeLayout profileLayout = (RelativeLayout)findViewById(R.id.my_profile);

        AsyncTask<Void, Void, User> task = new AsyncTask<Void, Void, User>() {

            @Override
            protected User doInBackground(Void... params) {

                try {
                    User user = TwitterUtil.getTwitterInstance(MyActivity.this).verifyCredentials();
                    return user;
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(User user) {

                if (user != null) {
                    userIdTextView.setText("@"+user.getScreenName());
                    nicknameTextView.setText(user.getName());
                    Picasso.with(MyActivity.this)
                            .load(user.getProfileImageURL())
                            .transform(new CircleTransformation())
                            .into(profileImageView);

                    Picasso.with(MyActivity.this)
                            .load(user.getProfileBannerURL())
                            .into(
                                new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                                    profileLayout.setBackground(drawable);
                                  }

                                  @Override
                                  public void onBitmapFailed(Drawable errorDrawable) {

                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {

                                  }
                              }

                            );
                }

            }
        };
        task.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //これがないとドロわーが開かない
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void openTweetDetailActivity(Tweet tweet) {
        Intent intent = TweetDetailActivity.createIntent(getApplicationContext(), tweet);
        startActivity(intent);
    }

    private void replaceRightFragment(int id, Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */

    @Override
    public void onTweetSelected(Tweet tweet) {

        if (currentLayout == LAYOUT_ONE_COLUMN) {
            openTweetDetailActivity(tweet);

        } else {
            replaceRightFragment(R.id.tweet_detail_container,
                    TweetDetailFragment.openFragment(tweet));

        }

    }

    private int getLayoutType() {

        View tweetDetailContainer = findViewById(R.id.tweet_detail_container);

        if (tweetDetailContainer != null) {
            return LAYOUT_TWO_COLUMN;
        }
        return LAYOUT_ONE_COLUMN;

    }
}

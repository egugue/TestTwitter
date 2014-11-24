package com.example.samuyu.testtwitter.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.entities.Tweet;
import com.example.samuyu.testtwitter.fragments.TweetDetailFragment;

public class TweetDetailActivity extends Activity {


    public static Intent createIntent(Context context, Tweet tweet) {
        Intent intent = new Intent(context, TweetDetailActivity.class);
        intent.putExtra("tweetData", tweet);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        Bundle extaras = getIntent().getExtras();
        if (extaras == null) {
            Log.d("HOGE", "インテントにExtarsが渡されていないので、終了します。");
            finish();
        }

        Tweet tweet = (Tweet)extaras.get("tweetData");

        //Toast.makeText(this, tweet.text, Toast.LENGTH_SHORT).show();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.detail_fragment_container, TweetDetailFragment.openFragment(tweet));
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tweet_detail, menu);
        return true;
    }

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
}

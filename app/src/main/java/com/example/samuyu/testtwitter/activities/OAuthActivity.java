package com.example.samuyu.testtwitter.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.utils.TwitterUtil;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class OAuthActivity extends Activity {

    private Button mOAuthButton;
    private Twitter mTwitter;
    private RequestToken mRequestToken;
    private String mCallbackUrl;

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, OAuthActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        mCallbackUrl = getString(R.string.twitter_callback_url);
        mTwitter = TwitterUtil.getTwitterInstance(this);

        mOAuthButton = (Button)findViewById(R.id.oauth_button);
        mOAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuthorize();
            }
        });

    }

    private void startAuthorize() {

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                    mRequestToken = mTwitter.getOAuthRequestToken(mCallbackUrl);
                    return mRequestToken.getAuthenticationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {

                if (url != null) {
                   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); //StingからUriはこうparseする
                    startActivity(intent);
                } else {
                    Toast.makeText(OAuthActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute();

    }

    @Override
    public void onNewIntent(Intent intent) {

        Log.d("HOGE", "onNewIntent");
        if (intent == null
            || intent.getData() == null
            || ! intent.getData().toString().startsWith(mCallbackUrl)
        ) {
          return ;
        }

        Log.d("HOGE", intent.getData().toString());
        String verifer = intent.getData().getQueryParameter("oauth_verifier");

        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {

            @Override
            protected AccessToken doInBackground(String... params) {

                try {
                    return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(AccessToken accessToken) {

                if (accessToken != null) {
                    Toast.makeText(getApplicationContext(), "認証成功", Toast.LENGTH_SHORT).show();
                    successOAuth(accessToken);
                }
            }
        };

        task.execute(verifer);
    }

    private void successOAuth(AccessToken accessToken) {

        TwitterUtil.storeAccessToken(this, accessToken);

        Intent intent = MyActivity.createIntent(getApplicationContext());
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.oauth, menu);
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

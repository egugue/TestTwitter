package com.example.samuyu.testtwitter.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.samuyu.testtwitter.R;
import com.example.samuyu.testtwitter.utils.TwitterUtil;


public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MyActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if (! TwitterUtil.hasAccessToken(this)) {
            Intent intent = OAuthActivity.createIntent(getApplicationContext());
            startActivity(intent);
            finish();
        }

        Log.d(TAG, "HOGE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

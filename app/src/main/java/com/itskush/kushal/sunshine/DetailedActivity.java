package com.itskush.kushal.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    private static final String APP_SHARE_HASHTAG = " #SunshineApp #MadeBy #Kushal";
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        TextView tv = (TextView) super.findViewById(R.id.tv);
        data = intent.getStringExtra("com.itskush.kushal.sunshine.ListItemText");
        tv.setText(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detailed_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentSettings = new Intent(DetailedActivity.this, SettingsActivity.class);
            startActivity(intentSettings);
            return true;
        }

        if (id == R.id.share) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, data + APP_SHARE_HASHTAG);
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

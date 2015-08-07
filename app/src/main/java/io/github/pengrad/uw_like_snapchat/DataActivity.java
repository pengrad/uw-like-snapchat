package io.github.pengrad.uw_like_snapchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * stas
 * 8/7/15
 */
public class DataActivity extends AppCompatActivity {

    public static final String EXTRA_PIXELS_COUNT = "pixels_count";

    public static Intent newIntent(Context context, int count) {
        Intent intent = new Intent(context, DataActivity.class);
        intent.putExtra(EXTRA_PIXELS_COUNT, count);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        setTitle("Result activity");

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        int count = getIntent().getIntExtra(EXTRA_PIXELS_COUNT, 0);

        TextView textView = (TextView) findViewById(R.id.textNumber);
        textView.setText(String.valueOf(count));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

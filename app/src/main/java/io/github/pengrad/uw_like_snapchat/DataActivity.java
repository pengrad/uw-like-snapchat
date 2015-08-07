package io.github.pengrad.uw_like_snapchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * stas
 * 8/7/15
 */
public class DataActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO_DATA = "photo_data";

    public static Intent newIntent(Context context, byte[] data) {
        Intent intent = new Intent(context, DataActivity.class);
        intent.putExtra(EXTRA_PHOTO_DATA, data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        byte[] data = getIntent().getByteArrayExtra(EXTRA_PHOTO_DATA);

        int count = 0;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        for (int w = 0; w < bitmap.getWidth(); w++) {
            for (int h = 0; h < bitmap.getHeight(); h++) {
                int color = bitmap.getPixel(w, h);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                if (red > 100 && green > 100 && blue > 100) {
                    count++;
                }
            }
        }

        TextView textView = new TextView(this);
        textView.setText("Number of pixels higher than RGB(100,100,100) =  " + count);


        setContentView(textView);
    }
}

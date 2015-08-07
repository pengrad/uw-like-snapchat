package io.github.pengrad.uw_like_snapchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * stas
 * 8/7/15
 */
public class PixelCalculator {

    public static int calcPixelsCount(byte[] data) {
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
        return count;
    }
}

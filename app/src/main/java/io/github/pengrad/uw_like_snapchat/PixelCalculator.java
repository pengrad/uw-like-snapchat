package io.github.pengrad.uw_like_snapchat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * stas
 * 8/7/15
 */
public class PixelCalculator {

    private int colorCondition;

    public PixelCalculator(int colorCondition) {
        this.colorCondition = colorCondition;
    }

    public BitmapColorInfo getBitmapColorInfo(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        int pixelsCount = bitmap.getWidth() * bitmap.getHeight();
        int pixelsConditionCount = 0;
        long reds = 0, greens = 0, blues = 0;

//        for (int w = 0; w < bitmap.getWidth(); w++) {
//            for (int h = 0; h < bitmap.getHeight(); h++) {
        for (int w = 0; w < 100; w++) {
            for (int h = 0; h < 100; h++) {
                int color = bitmap.getPixel(w, h);

                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);

                if (red > colorCondition && green > colorCondition && blue > colorCondition) {
                    pixelsConditionCount++;
                }
                reds += red;
                greens += green;
                blues += blue;
            }
        }
        int avgRed = (int) (reds / pixelsCount);
        int avgGreen = (int) (greens / pixelsCount);
        int avgBlue = (int) (blues / pixelsCount);
        int avgColor = Color.rgb(avgRed, avgGreen, avgBlue);

        return new BitmapColorInfo(pixelsCount, pixelsConditionCount, avgColor);
    }
}

package io.github.pengrad.uw_like_snapchat;

import java.io.Serializable;

/**
 * stas
 * 8/8/15
 */
public class BitmapColorInfo implements Serializable {

    private int pixelsCount;
    private int pixelsConditionCount;
    private int avgColor;

    public BitmapColorInfo(int pixelsCount, int pixelsConditionCount, int avgColor) {
        this.pixelsCount = pixelsCount;
        this.pixelsConditionCount = pixelsConditionCount;
        this.avgColor = avgColor;
    }

    public int getPixelsConditionCount() {
        return pixelsConditionCount;
    }

    public int getAvgColor() {
        return avgColor;
    }
}

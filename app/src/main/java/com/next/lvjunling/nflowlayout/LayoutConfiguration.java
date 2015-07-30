package com.next.lvjunling.nflowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Created by lvjunling on 15/7/29.
 */
public class LayoutConfiguration {

    private int lines;
    private int horizontalSpacing;
    private int verticalSpacing;

    public LayoutConfiguration(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.NFlowLayout);
        try {
            setLines(a.getInteger(R.styleable.NFlowLayout_lines, Integer.MAX_VALUE));
            setHorizontalSpacing(a.getDimensionPixelSize(R.styleable.NFlowLayout_horizontalSpacing, 0));
            setVerticalSpacing(a.getDimensionPixelSize(R.styleable.NFlowLayout_verticalSpacing, 0));
        } finally {
            a.recycle();
        }
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }
}

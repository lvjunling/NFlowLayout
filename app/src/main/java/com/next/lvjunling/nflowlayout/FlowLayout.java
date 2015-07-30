package com.next.lvjunling.nflowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvjunling on 15/7/30.
 */
public class FlowLayout extends ViewGroup{

    private List<List<View>> childs = new ArrayList<>();
    private List<Integer> lineHeights = new ArrayList();
    private LayoutConfiguration configuration;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        configuration = new LayoutConfiguration(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        List<View> curLines = null;
        View childView = null;
        int childLeft = getPaddingLeft(), childTop = getPaddingTop();
        for (int i = 0; i < childs.size(); i++) {
            curLines = childs.get(i);
            int lb = 0, tb = 0, rb = 0, bb = 0;
            for (int j = 0; j < curLines.size(); j++) {
                childView = curLines.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                lb = childLeft + lp.leftMargin;
                tb = childTop + lp.topMargin;
                rb = lb + childView.getMeasuredWidth();
                bb = tb + childView.getMeasuredHeight();
                childView.layout(lb, tb, rb, bb);
                childLeft += childView.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin + configuration.getHorizontalSpacing();
            }
            childLeft = getPaddingLeft();
            childTop += lineHeights.get(i);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        childs.clear();
        lineHeights.clear();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int endHeight = 0, lineWidth = 0, lineHeight = 0;
        int childCount = getChildCount();
        List<View> lineViews = new ArrayList();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //水平间隔
            if (lineViews.size() != 0) {
                childWidth += configuration.getHorizontalSpacing();
            }
            if (lineWidth + childWidth > widthSize) {
                addToList(lineViews, addVerticalSpacing(lineHeight));
                endHeight += lineHeight;
                lineHeight = childHeight;
                lineWidth = childWidth;
                lineViews = new ArrayList();
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            lineViews.add(child);
        }
        addToList(lineViews, addVerticalSpacing(lineHeight));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 与当前ViewGroup对应的LayoutParams,子view通过inflate(R.layout.xx, root, false)导入时;
     * 根据源码可知，inflate时候，Root不为空时才会调用generateLayoutParams获取xml设置的参数
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private void addToList(List<View> lineViews, int lineHeight) {
        if (childs.size() < configuration.getLines()) {
            childs.add(lineViews);
            lineHeights.add(lineHeight);
        }
    }

    private int addVerticalSpacing(int lineHeight){
        //有垂直间隔
        if (configuration.getVerticalSpacing() > 0) {
            lineHeight += configuration.getVerticalSpacing();
        }
        return lineHeight;
    }

}

package com.zsr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zsr.starratingview.R;

import java.util.ArrayList;
import java.util.List;

public class StarRatingView extends LinearLayout {
    private boolean stepInt = true;
    private int star_num = 5;
    private Drawable on, off, half;   //星星三种图片
    private boolean ratable;    //是否点击滑动
    private float padding;  //星星之间间距
    private List<ImageView> list;
    private float starWidth;    //设置的星星图片宽度
    private float halfStarWidth;    //半个星星宽度
    private int paddingLeft;       //控件左padding
    private float itemWidth; //一个星+padding的宽度。
    private OnRateChangeListener onRateChangeListener;
    private float select_star = 0;

    public StarRatingView(Context context) {
        super(context);
    }

    public StarRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StarRatingView);
        on = array.getDrawable(R.styleable.StarRatingView_stat_on);
        off = array.getDrawable(R.styleable.StarRatingView_stat_off);
        half = array.getDrawable(R.styleable.StarRatingView_stat_half);
        ratable = array.getBoolean(R.styleable.StarRatingView_ratable, false);
        stepInt = array.getBoolean(R.styleable.StarRatingView_star_stepInt, true);
        star_num = array.getInteger(R.styleable.StarRatingView_star_num, star_num);
        halfStarWidth = on.getIntrinsicWidth() / 2;
        starWidth = on.getIntrinsicWidth();
        padding = array.getDimension(R.styleable.StarRatingView_star_padding, starWidth / 3);
        itemWidth = starWidth + padding;
        paddingLeft = getPaddingLeft();
        list = new ArrayList<>();
        ImageView imageView;
        //初始化五个星星，并通过points数组把分数与坐标对应起来
        for (int i = 0; i < star_num; i++) {
            imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins((int) padding / 2, 0, (int) padding / 2, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageDrawable(off);
            list.add(imageView);
            addView(list.get(i));
        }
        setOrientation(LinearLayout.HORIZONTAL);
        array.recycle();
    }

    /**
     * 根据分数显示星星
     *
     * @param rate
     */
    public void setRate(float rate) {
        if (rate == select_star) {
            return;
        }
        select_star = rate;
        removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            if (i < rate) {
                list.get(i).setImageDrawable(on);
            } else {
                list.get(i).setImageDrawable(off);
            }
            if (i + 0.5f == rate) {
                if (half != null) {
                    list.get(i).setImageDrawable(half);
                } else {
                    list.get(i).setImageDrawable(on);
                }
            }
            addView(list.get(i));
        }
        if (ratable && onRateChangeListener != null) {
            onRateChangeListener.onRateChange(rate);
        }
    }

    public void setOnRateChangeListener(OnRateChangeListener l) {
        onRateChangeListener = l;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //设置不可以滑动则不处理事件
        if (!ratable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                setRate(calculateStar(event.getX()));
                return true;
            default:
                //当不是以上三个action时，如 为 CANCLE事件时，getX()的值就不是点击位置的相对坐标了。我们之采集以上三种事件下的坐标更新UI。
//                Log.e("zsrzsr", "未知 ： " + event.getAction());
        }

        return super.onTouchEvent(event);
    }

    /**
     * 根据坐标计算分数
     *
     * @param x
     * @return
     */
    private float calculateStar(float x) {
        if (x <= 0) {
            return 0;
        }
        //减去控件左padding
        float position = x - paddingLeft;
        float again = position / itemWidth;
        float num = 0;
        if (stepInt) {
            num = (int) Math.ceil(again);
        } else {//需要选择半颗星星的情况
            int high = (int) again;
            float low = again % 1;
            if (low <= 0.5f) {
                num = high + 0.5f;
            } else {
                num = high + 1;
            }
        }
        if (num >= star_num) {
            num = star_num;
        }
        return num;
    }

    /**
     * 评分改变的回调
     */
    public interface OnRateChangeListener {
        void onRateChange(float rate);
    }
}

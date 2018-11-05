package com.jiudu.newdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DemoView extends View {
    private int PaintColor = 1;
    private int TextColor = 0;
    public DemoView(Context context) {
        super(context);
        init(context);
        setWillNotDraw(false);
    }


    public DemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UserMessge);
        PaintColor = a.getColor(R.styleable.UserMessge_PaintColor,0);
        TextColor = a.getInteger(R.styleable.UserMessge_TextSize,0);
        init(context);
        setWillNotDraw(false);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UserMessge);
        init(context);
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置颜色
     * @param color
     */
    public void setPaintColor(int color){
        this.PaintColor = color;
        Log.i("amp","9985566");
        postInvalidate();
    }

    public void setTextColor(int size){
        this.TextColor = size;
        postInvalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("amp","99999");
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setColor(PaintColor);
        mPaint.setTextSize(TextColor);
        canvas.drawLine(20,20,120,120,mPaint);
        Log.i("amp","3366");
    }

    private void init(Context context){


    }
}

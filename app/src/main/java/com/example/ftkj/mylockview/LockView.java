package com.example.ftkj.mylockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



import java.util.ArrayList;

/**
 * Created by FTKJ on 2017/8/3.
 */

public class LockView extends View {
    private Context mContext;
    //九宫格的实际宽高
    private int mWidth;
    private int mHeight;
    //九宫格的默认宽高
    private int mViewWidth;
    private int mViewHeight;
    //点的颜色
    private int mPointColor;
    private Paint mPaint;
    //点的半径
    private int mPointRadius;
    //点信息的集合
    private ArrayList<Point> mPointList;

    //线头的位置
    private float mLineX;
    private float mLineY;
    //记录轨迹
    ArrayList<Integer> recardLine = null;
    private Paint mLinePaint;

    private StringBuilder mPassword = new StringBuilder();
    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(context, attrs, defStyleAttr);
        initPaint();

    }

  
    private void initPoint() {
        mPointList = new ArrayList<>();
        int distance = mWidth/2-mPointRadius;
        for (int i = 0; i < 9; i++) {
            if (i >= 0 && i <= 2) {
                mPointList.add(new Point(i * distance + mPointRadius, mPointRadius, mPointRadius, false));
            } else if (i > 2 && i <= 5) {
                mPointList.add(new Point((i-3)*distance+mPointRadius,distance+mPointRadius, mPointRadius,false));
            } else {
                mPointList.add(new Point((i-6)*distance+mPointRadius,2*distance+mPointRadius,mPointRadius,false));
            }
        }

    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LockView, defStyleAttr, 0);
        mViewWidth = typedArray.getDimensionPixelSize(R.styleable.LockView_lViewWidth, DensityUtil.dip2px(context, 150));
        mViewHeight = typedArray.getDimensionPixelSize(R.styleable.LockView_lViewHeight, DensityUtil.dip2px(context, 150));
        mPointColor = typedArray.getColor(R.styleable.LockView_pointColor, Color.BLACK);
        mPointRadius = typedArray.getDimensionPixelSize(R.styleable.LockView_pointRadius, DensityUtil.dip2px(context, 20));
        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mPointColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);



        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(10);
        mLinePaint.setColor(Color.GREEN);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = mViewWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = mViewHeight;
        }

        setMeasuredDimension(mWidth, mHeight);

        initPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 9; i++) {
            Point point = mPointList.get(i);
            if (point.isLight()){
                mPaint.setColor(Color.GREEN);
            }else {
                mPaint.setColor(mPointColor);
            }
            canvas.drawCircle(point.getCx(),point.getCy(),point.getRadius(),mPaint);

        }


        if (recardLine!=null&&!recardLine.isEmpty()) {
            Path path = new Path();
            path.moveTo(mPointList.get(recardLine.get(0)).getCx(),mPointList.get(recardLine.get(0)).getCy());
            for (int i = 1; i <recardLine.size() ; i++) {
                path.lineTo(mPointList.get(recardLine.get(i)).getCx(),mPointList.get(recardLine.get(i)).getCy());
            }
            canvas.drawPath(path,mLinePaint);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        float x = event.getX();
        float y = event.getY();

        boolean ret=false;
        switch (eventAction){
            case MotionEvent.ACTION_DOWN:
                 recardLine = new ArrayList<>();
                for (int i = 0; i < 9; i++) {
                    Point point = mPointList.get(i);
                    if (x>=point.getCx()-mPointRadius&&x<=point.getCx()+mPointRadius&&y>=point.getCy()-mPointRadius&&y<=point.getCy()+mPointRadius){
                        point.setLight(true);
                        recardLine.add(i);
                        invalidate();
                    }
                }
                ret = true;
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < 9; i++) {
                    Point point = mPointList.get(i);
                    if (x>=point.getCx()-mPointRadius&&x<=point.getCx()+mPointRadius&&y>=point.getCy()-mPointRadius&&y<=point.getCy()+mPointRadius){
                        point.setLight(true);
                        if (!recardLine.contains(i)) {
                            recardLine.add(i);
                        }
                        invalidate();
                    }
                }
                mLineX = x;
                mLineY = y;
                break;
            case MotionEvent.ACTION_UP:
                mPassword.delete(0,mPassword.length());
                for (int i = 0; i < 9; i++) {
                    mPointList.get(i).setLight(false);
                    for (Integer integer : recardLine) {
                        mPassword.append(integer);
                    }
                    recardLine.clear();
                    invalidate();
                }
                break;
        }
        return ret;
    }

    public String getPassword(){
        return mPassword.toString();
    }
}

package com.zhang.pvz.plantsvszombies.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhang.pvz.plantsvszombies.MainActivity;

/**
 * Created by Administrator on 2017/8/31 0031.
 */

public class ProcessView extends SurfaceView implements SurfaceHolder.Callback {
    private Bitmap background;
    private MainActivity activity;
    private RefreshThread mRefreshThread;
    private Paint mPaint;

    public static int getProcessPercent() {
        return processPercent_;
    }

    public static void setProcessPercent(int processPercent_) {
        ProcessView.processPercent_ = processPercent_;
    }

    private static int processPercent_ = 0;

    public ProcessView(MainActivity activity, Bitmap background) {
        super(activity);
        this.activity = activity;
        this.background = background;
        getHolder().addCallback(this);
        mPaint = new Paint();
        mRefreshThread = new RefreshThread(getHolder(), this);
        initBitmap();
    }

    private void initBitmap() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, 0, 0, mPaint);
        canvas.drawText("加载中。。。。", 420, 300, mPaint);
        int rectWidth = 400;
        int rectHeight = 30;
        int rectLeft = 7;
        int rectTop = 280;
        int rectRight = (rectLeft + rectWidth);
        int rectBottom = (rectTop + rectHeight);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mPaint);
        mPaint.setColor(0xFFFFAA00);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectLeft, rectTop, rectLeft + getProcessPercent() * 4, rectBottom, mPaint);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        mRefreshThread.setFlag(true);
        mRefreshThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        mRefreshThread.setFlag(false);
        while (retry) {
            try {
                mRefreshThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }


    public class RefreshThread extends Thread {
        // 睡眠的毫秒数
        int span = 50;
        SurfaceHolder surfaceHolder_ = null;
        ProcessView processView_ = null;
        boolean flag_ = false;

        public RefreshThread(SurfaceHolder surfaceHolder, ProcessView processView) {
            this.surfaceHolder_ = surfaceHolder;
            this.processView_ = processView;
        }

        public void setFlag(boolean flag) {
            this.flag_ = flag;
        }

        public void run() {
            Canvas canvas;

            while (this.flag_) {
                canvas = null;

                try {
                    // 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
                    canvas = this.surfaceHolder_.lockCanvas(null);
                    synchronized (ProcessView.class) {
                        processView_.draw(canvas);

                    }
                } finally {
                    if (canvas != null)
                        this.surfaceHolder_.unlockCanvasAndPost(canvas);
                }

                if (getProcessPercent() >= 100) {
                    processView_.activity.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu);
                    setProcessPercent(0);
                }

                try {
                    Thread.sleep(span);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }    // RefreshThread
}

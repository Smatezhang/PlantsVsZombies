package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class PlantWinView extends SurfaceView
        implements SurfaceHolder.Callback {
    Bitmap plant_win_Image_ = null;
    MainActivity activity_ = null;
    RefreshThread refreshThread_ = null;
    Paint paint_ = new Paint();
    Handler handler_ = null;

    private MIDIPlayer plant_win_view_mMIDIPlayer = null;

    public PlantWinView(MainActivity activity) {
        super(activity);

        activity_ = activity;

        initBitmap();

        getHolder().addCallback(this);

        this.refreshThread_ = new RefreshThread(getHolder(), this);

        if (this.activity_.hasBackgroundMusic()) {
            plant_win_view_mMIDIPlayer = this.activity_.musicFactory_.getMusicPlayer(
                    MainActivity.MusicFactory.Background_Plant_WinView);
            plant_win_view_mMIDIPlayer.SetPlayerLoop(false);

            plant_win_view_mMIDIPlayer.PlayMusic();
        }
    }

    private void initBitmap() {
        plant_win_Image_ = activity_.otherFactory_.getImage(MainActivity.OtherFactory.Background_Plant_Win);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(this.plant_win_Image_, 0, 0, paint_);
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.refreshThread_.setFlag(true);
        this.refreshThread_.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        refreshThread_.setFlag(false);
        while (retry) {
            try {
                refreshThread_.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (null != plant_win_view_mMIDIPlayer && plant_win_view_mMIDIPlayer.playerMusic.isPlaying())
                plant_win_view_mMIDIPlayer.FreeMusic();
            activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu);
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////
    class RefreshThread extends Thread {
        SurfaceHolder surfaceHolder_ = null;
        PlantWinView plant_win_View_ = null;
        boolean flag_ = false;

        public RefreshThread(SurfaceHolder surfaceHolder, PlantWinView plantWinView) {
            this.surfaceHolder_ = surfaceHolder;
            this.plant_win_View_ = plantWinView;
        }

        public void setFlag(boolean flag) {
            this.flag_ = flag;
        }

        public void run() {
            Canvas canvas;

            while (this.flag_) {
                canvas = null;

                try {
                    // ��������������ڴ�Ҫ��Ƚϸߵ�����£��������ҪΪnull
                    canvas = this.surfaceHolder_.lockCanvas(null);
                    synchronized (this.surfaceHolder_) {
                        plant_win_View_.draw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        this.surfaceHolder_.unlockCanvasAndPost(canvas);
                }
            }
        }
    }    // RefreshThread
}

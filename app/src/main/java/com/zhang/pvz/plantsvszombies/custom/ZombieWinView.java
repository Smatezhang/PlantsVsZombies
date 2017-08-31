package com.zhang.pvz.plantsvszombies.custom;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class ZombieWinView extends SurfaceView
        implements SurfaceHolder.Callback {
    Bitmap zombie_win_Image_ = null;
    MainActivity activity_ = null;
    RefreshThread refreshThread_ = null;
    Paint paint_ = new Paint();
    Handler handler_ = null;

    private MIDIPlayer zombie_win_view_mMIDIPlayer = null;
    private MIDIPlayer zombie_win_view_mMIDIPlayer1 = null;
    private boolean dead_sound_have_paly = false;

    public ZombieWinView(MainActivity activity) {
        super(activity);

        activity_ = activity;

        initBitmap();

        getHolder().addCallback(this);

        this.refreshThread_ = new RefreshThread(getHolder(), this);

        if (this.activity_.hasBackgroundMusic()) {
            zombie_win_view_mMIDIPlayer1 = this.activity_.musicFactory_.getMusicPlayer(
                    MainActivity.MusicFactory.People_Dead);
            zombie_win_view_mMIDIPlayer1.SetPlayerLoop(false);

            zombie_win_view_mMIDIPlayer = this.activity_.musicFactory_.getMusicPlayer(
                    MainActivity.MusicFactory.Background_Zombie_WinView);
            zombie_win_view_mMIDIPlayer.SetPlayerLoop(false);

            zombie_win_view_mMIDIPlayer.PlayMusic();
        }
    }

    private void initBitmap() {
        zombie_win_Image_ = activity_.otherFactory_.getImage(MainActivity.OtherFactory.Background_Zombie_Win);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(this.zombie_win_Image_, 0, 0, paint_);
        if (null != zombie_win_view_mMIDIPlayer && !zombie_win_view_mMIDIPlayer.playerMusic.isPlaying()) {
            zombie_win_view_mMIDIPlayer.FreeMusic();
            if (null != zombie_win_view_mMIDIPlayer1 && !dead_sound_have_paly) {
                zombie_win_view_mMIDIPlayer1.PlayMusic();
                dead_sound_have_paly = true;
            }
        }
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
            if (null != zombie_win_view_mMIDIPlayer1)
                zombie_win_view_mMIDIPlayer1.FreeMusic();
            if (null != zombie_win_view_mMIDIPlayer && zombie_win_view_mMIDIPlayer.playerMusic.isPlaying())
                zombie_win_view_mMIDIPlayer.FreeMusic();
            activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu);
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////
    class RefreshThread extends Thread {
        SurfaceHolder surfaceHolder_ = null;
        ZombieWinView zombie_win_View_ = null;
        boolean flag_ = false;

        public RefreshThread(SurfaceHolder surfaceHolder, ZombieWinView zombiewinView) {
            this.surfaceHolder_ = surfaceHolder;
            this.zombie_win_View_ = zombiewinView;
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
                        zombie_win_View_.draw(canvas);
                    }
                } finally {
                    if (canvas != null)
                        this.surfaceHolder_.unlockCanvasAndPost(canvas);
                }
            }
        }
    }    // RefreshThread
}

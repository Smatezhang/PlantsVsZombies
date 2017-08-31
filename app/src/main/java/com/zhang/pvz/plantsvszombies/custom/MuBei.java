
package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Canvas;

import com.zhang.pvz.plantsvszombies.MainActivity;


public class MuBei extends Plant {
    MainActivity activity_ = null;
    private int mubei_timer_ = 150;

    public MuBei(int x, int y, DoublePlayerModeAView activity) {
        super(x, y, 1);
        this.life_ = 5;
        this.activity_ = activity.activity_;
        this.doublePlayerModeAView_ = activity;
        loadImage();
    }

    protected void loadImage() {
        frameImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_MuBei);
        this.type_ = MainActivity.PlantImageFactory.Plant_MuBei;
    }

    public void draw(Canvas canvas) {
        if (--this.mubei_timer_ == 0) {
            this.mubei_timer_ = 150;
            this.doublePlayerModeAView_.sunshines_.add(new Zombie_Sunshine(xDraw_ + 40, yDraw_ + 35, doublePlayerModeAView_));
        }

        canvas.drawBitmap(frameImage_[frameIndex_], xDraw_, yDraw_, paint_);

        frameIndex_ = (frameIndex_ + 1) % frameAmount_;
    }

    public void attack() {
        ;
    }

}

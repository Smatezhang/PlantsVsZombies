package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Canvas;

import com.zhang.pvz.plantsvszombies.MainActivity;



public class XiangRiKui extends Plant {
    MainActivity activity_ = null;
    private int sunshine_timer_ = 150;

    public XiangRiKui(int x, int y, DoublePlayerModeAView activity) {
        super(x, y, 18);

        this.life_ = 10;
        this.activity_ = activity.activity_;
        this.doublePlayerModeAView_ = activity;
        loadImage();
    }

    protected void loadImage() {
        frameImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiangRiKui);
        this.type_ = MainActivity.PlantImageFactory.Plant_XiangRiKui;
    }

    public void draw(Canvas canvas) {
        if (--this.sunshine_timer_ == 0) {
            this.sunshine_timer_ = 150;
            this.doublePlayerModeAView_.sunshines_.add(new Plant_Sunshine(xDraw_ + 40, yDraw_ + 35, doublePlayerModeAView_));
        }

        canvas.drawBitmap(frameImage_[frameIndex_], xDraw_, yDraw_, paint_);

        frameIndex_ = (frameIndex_ + 1) % frameAmount_;
    }

    public void attack() {
        ;
    }
}

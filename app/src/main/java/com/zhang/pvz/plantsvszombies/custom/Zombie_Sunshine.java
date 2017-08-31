package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.zhang.pvz.plantsvszombies.MainActivity;


public class Zombie_Sunshine extends Sunshine {
    MainActivity activity_ = null;
    private Bitmap zombie_sunshine_frame_ = null;
    private int luodijuli_ = 0;
    private int[][] direct_ = {{0, 0}, {2, -2}, {4, -4}, {6, -6}, {8, -8}, {10, -6}, {12, -4}, {14, -2}, {16, 0},
            {16, 5}, {16, 10}, {16, 15}, {16, 20}, {16, 25}};
    private float gradient_ = 0;
    private float tx, ty;
    private int dx, dy;

    public Zombie_Sunshine(int x, int y, DoublePlayerModeAView activity) {
        super(x, y);

        ty = y;
        tx = x - 20;
        this.gradient_ = ty / (tx - 440);
        this.activity_ = activity.activity_;
        this.doublePlayerModeAView_ = activity;
        loadImage();
    }

    protected void loadImage() {
        this.zombie_sunshine_frame_ = activity_.sunImageFactory_.getImage(MainActivity.SunImageFactory.Sun_Zombie);
    }

    public void move() {
        if (luodijuli_ < 13) {
            dx = xDraw_ + direct_[luodijuli_][0];
            dy = yDraw_ + direct_[luodijuli_][1];
            luodijuli_++;
        } else {
            if (dx <= 440 && dy >= 30) {
                dx += 10;
                dy = (int) ((dx - 430) * this.gradient_);
            }
        }

        if (dx > 440 || dy < 30) {
            if (!this.isCollected) {
                this.isCollected = true;
                this.doublePlayerModeAView_.sZ_sun += 25;
                this.doublePlayerModeAView_.zombieSunAmount_ += 25;
                doublePlayerModeAView_.moveThread_.deleteSunshines_.add(this);
            }
        }
    }

    public void draw(Canvas canvas) {
        if (luodijuli_ < 13)
            canvas.drawBitmap(this.zombie_sunshine_frame_, dx, dy, paint_);
        else if (!(dx > 440 || dy < 30))
            canvas.drawBitmap(this.zombie_sunshine_frame_, dx, dy, paint_);
    }
}

package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhang.pvz.plantsvszombies.MainActivity;

public abstract class Bullet {
    protected MainActivity activity_ = null;
    protected DoublePlayerModeAView doublePlayerModeAView_ = null;

    // 子弹图片底边中点坐标
    protected int x_ = 0;
    protected int y_ = 0;

    // 贴图的坐标
    protected int xDraw_ = 0;
    protected int yDraw_ = 0;

    protected Bitmap buttleImage_ = null;
    protected Bitmap buttleHitImage_ = null;

    protected boolean isHited_ = false;

    // 子弹的威力
    protected int bulletPower_ = 1;

    protected int speed_ = 5;
    // 用于在画布上绘图
    Paint paint_ = new Paint();

    protected int count_ = 0;

    public Bullet(int x, int y) {
        this.x_ = x;
        this.y_ = y;
        this.xDraw_ = x - 14;
        this.yDraw_ = y - 37;
    }

    protected void loadImage() {
    }

    public void draw(Canvas canvas) {
        if (this.x_ >= 470)
            return;

        if (this.isHited()) {
            ++count_;
            canvas.drawBitmap(buttleHitImage_, xDraw_, yDraw_, paint_);
        } else {
            canvas.drawBitmap(buttleImage_, xDraw_, yDraw_, paint_);
        }

    }

    public void move() {
        if (this.isHited())
            return;

        this.x_ += speed_;
        this.xDraw_ += speed_;
    }

    public void checkHit() {

    }

    //-------------------------------------------------------------------------
    // Setters and Getters
    //-------------------------------------------------------------------------
    public int getX() {
        return this.x_;
    }

    public int getY() {
        return this.y_;
    }

    public int getLeftX() {
        return this.x_;
    }

    public int getRightX() {
        return this.x_;
    }

    public int getPower() {
        return bulletPower_;
    }

    public void setHited() {
        this.isHited_ = true;
    }

    public boolean isHited() {
        return this.isHited_;
    }

    public boolean isRemoveable() {
        return this.count_ >= 3;
    }
}

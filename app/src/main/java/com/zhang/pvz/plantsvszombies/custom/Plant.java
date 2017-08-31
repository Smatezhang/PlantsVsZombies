package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhang.pvz.plantsvszombies.MainActivity;

public abstract class Plant {
    protected MainActivity activity_ = null;
    protected DoublePlayerModeAView doublePlayerModeAView_ = null;

    // 植物图片底边中点坐标
    protected int x_ = 0;
    protected int y_ = 0;

    // 贴图的坐标
    protected int xDraw_ = 0;
    protected int yDraw_ = 0;

    // 植物的生命
    protected int life_ = 10;
    // 播放植物动画的帧序列
    protected int frameIndex_ = 0;
    // 植物动画的帧数
    protected Bitmap[] frameImage_ = null;
    // 植物动画的帧总数
    protected final int frameAmount_;
    // 用于在画布上绘图
    protected Paint paint_ = new Paint();

    public int type_ = 0;

    protected int status_ = 0;

    public Plant(int x, int y, int frameAmount) {
        this.x_ = x;
        this.y_ = y;
        this.xDraw_ = x - 40;
        this.yDraw_ = y - 40;
        this.frameAmount_ = frameAmount;
    }

    protected void loadImage() {
        // TODO	子类必须重写此函数，用于加载植物类及动画图片
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(frameImage_[frameIndex_], xDraw_, yDraw_, paint_);

        frameIndex_ = (frameIndex_ + 1) % frameAmount_;
    }

    public void attack() {

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
        return this.x_ - this.frameImage_[0].getWidth() / 2;
    }

    public int getRightX() {
        return this.x_ + this.frameImage_[0].getWidth() / 2;
    }

    public int getLife() {
        return this.life_;
    }

    public void setLife(int life) {
        this.life_ = life;
    }

    public boolean isDead() {
        return this.life_ <= 0;
    }

    public void checkHit() {

    }

    public int gettype() {
        return type_;
    }
}

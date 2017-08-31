package com.zhang.pvz.plantsvszombies.custom;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhang.pvz.plantsvszombies.MainActivity;

public abstract class Zombie {
    public static final int State_Normal = 0;
    public static final int State_Attack = 1;
    public static final int State_Normal_Dead = 2;
    public static final int State_Attack_Dead = 3;
    public static final int State_Dead = 4;

    MainActivity activity_ = null;
    public int state_ = State_Normal;

    public int type_ = 0;

    // 图片底边中点坐标
    protected int x_ = 0;
    protected int y_ = 0;

    // 贴图的坐标
    protected int xDraw_ = 0;
    protected int yDraw_ = 0;

    // 僵尸的生命
    protected int life_ = 10;

    protected int speed_ = -2;

    protected Paint paint_ = new Paint();

    protected boolean isRemoveable_ = false;


    protected int frameNormalAmount_;
    protected int frameAttackAmount_;
    protected Bitmap[] frameNormalImage_;
    protected Bitmap[] frameAttackImage_;

    public Zombie(int x, int y, DoublePlayerModeAView activity) {
        this.x_ = x;
        this.y_ = y;
        this.xDraw_ = x - 90;
        this.yDraw_ = y - 80;
        this.activity_ = activity.activity_;
    }

    protected void loadImage() {
        // TODO	子类必须重写此函数
    }

    public void draw(Canvas canvas) {
    }

    public void move() {

    }

    public void updateState() {
        if (this.life_ <= 0) {
            switch (this.state_) {
                case State_Normal:
                    state_ = State_Normal_Dead;
                    break;
                case State_Normal_Dead:
                    break;
                case State_Attack:
                    state_ = State_Attack_Dead;
                    break;
                case State_Attack_Dead:
                    break;
                case State_Dead:
                    break;
            }
        }
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

    public int getLife() {
        return this.life_;
    }

    public void setLife(int life) {
        this.life_ = life;
    }

    public void setState(int state) {
        this.state_ = state;
    }

    public int getState() {
        return this.state_;
    }

    public boolean isRemoveable() {
        return this.isRemoveable_;
    }

    public void change() {
        frameNormalAmount_ = 22;
        frameAttackAmount_ = 21;
        frameNormalImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi);
        frameAttackImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi_Attack);
    }
}

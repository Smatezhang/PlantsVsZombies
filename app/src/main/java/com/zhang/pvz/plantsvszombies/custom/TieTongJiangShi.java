package com.zhang.pvz.plantsvszombies.custom;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class TieTongJiangShi extends Zombie {
    //	PvZ_OnlineActivity 						activity_					= null;
    DoublePlayerModeAView doublePlayerModeAView_ = null;

    // 行走状态
    int frameNormalIndex_ = 0;
    //Bitmap[]									frameNormalImage_			= null;
    // int									frameNormalAmount_		= 15;

    // 行走状态死亡
    int frameNormalDeadIndex_ = 0;
    Bitmap[] frameNormalDeadImage_ = null;
    final int frameNormalDeadAmount_ = 10;

    // 攻击状态
    int frameAttackIndex_ = 0;
    //Bitmap[]									frameAttackImage_			= null;
    // int									frameAttackAmount_		= 11;

    // 攻击状态死亡
    int frameAttackDeadIndex_ = 0;
    Bitmap[] frameAttackDeadImage_ = null;
    final int frameAttackDeadAmount_ = 11;

    // 死亡倒地
    int frameDeadIndex_ = 0;
    Bitmap[] frameDeadImage_ = null;
    final int frameDeadAmount_ = 10;

    // 死亡掉落的头部
    int frameHeadIndex_ = 0;
    Bitmap[] frameHeadImage_ = null;
    final int frameHeadAmount_ = 12;

    int attackCount_ = 0;

    public TieTongJiangShi(int x, int y, DoublePlayerModeAView activity) {
        super(x, y, activity);

        this.life_ = 30;
        this.speed_ = -1;
        //this.activity_ 			= activity.activity_;
        doublePlayerModeAView_ = activity;
        frameNormalAmount_ = 15;
        frameAttackAmount_ = 11;

        loadImage();
    }

    protected void loadImage() {
        frameNormalImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_TieTongJiangShi);
        frameNormalDeadImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi_Losthead);
        frameAttackImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_TieTongJiangshi_Attack);
        frameAttackDeadImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi_Attack_Losthead);
        frameDeadImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi_Die);
        frameHeadImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi_Head);
    }

    public void draw(Canvas canvas) {
        switch (state_) {
            case State_Normal:
                canvas.drawBitmap(frameNormalImage_[frameNormalIndex_], xDraw_, yDraw_, paint_);
                frameNormalIndex_ = (frameNormalIndex_ + 1) % frameNormalAmount_;
                break;
            case State_Attack:
                canvas.drawBitmap(frameAttackImage_[frameAttackIndex_], xDraw_, yDraw_, paint_);
                frameAttackIndex_ = (frameAttackIndex_ + 1) % frameAttackAmount_;
                break;
            case State_Normal_Dead:
                canvas.drawBitmap(frameNormalDeadImage_[frameNormalDeadIndex_], xDraw_, yDraw_, paint_);
                ++frameNormalDeadIndex_;
                ;
                canvas.drawBitmap(frameHeadImage_[frameHeadIndex_], xDraw_ + 45, yDraw_, paint_);
                frameHeadIndex_ = (frameHeadIndex_ + 1) % frameHeadAmount_;
                if (frameNormalDeadIndex_ == frameNormalDeadAmount_)
                    state_ = State_Dead;
                break;
            case State_Attack_Dead:
                canvas.drawBitmap(frameAttackDeadImage_[frameAttackDeadIndex_], xDraw_, yDraw_, paint_);
                ++frameAttackDeadIndex_;
                canvas.drawBitmap(frameHeadImage_[frameHeadIndex_], xDraw_ + 45, yDraw_, paint_);
                frameHeadIndex_ = (frameHeadIndex_ + 1) % frameHeadAmount_;
                if (frameAttackDeadIndex_ == frameAttackDeadAmount_)
                    state_ = State_Dead;
                break;
            case State_Dead:
                canvas.drawBitmap(frameDeadImage_[frameDeadIndex_], xDraw_, yDraw_, paint_);
                ++frameDeadIndex_;
                if (frameDeadIndex_ == frameDeadAmount_)
                    this.isRemoveable_ = true;
                break;
        }
    }

    public void checkHit() {
        boolean isAttack = false;

        for (Plant plant : doublePlayerModeAView_.plants_) {
            if (plant.getY() == this.getY()) {
                if (plant.getX() <= this.getX()) {
                    if (plant.type_ == MainActivity.PlantImageFactory.Plant_MuBei)
                        continue;

                    if (State_Dead != state_ && State_Normal_Dead != state_ && State_Attack_Dead != state_) {
                        if (plant.getRightX() >= this.getX()) {
                            isAttack = true;
                            this.state_ = State_Attack;
                            if (0 == attackCount_)
                                plant.setLife(plant.getLife() - 1);
                            attackCount_ = (attackCount_ + 1) % 10;

                        }
                    }
                }
            }
        }

        if (State_Dead != state_ && State_Normal_Dead != state_ && State_Attack_Dead != state_)
            if (!isAttack)
                state_ = State_Normal;
    }

    public void move() {
        switch (state_) {
            case State_Normal:
                this.x_ += speed_;
                this.xDraw_ += speed_;
                break;
            case State_Attack:
                break;
            case State_Normal_Dead:
                this.x_ += speed_;
                this.xDraw_ += speed_;
                break;
            case State_Attack_Dead:
                break;
            case State_Dead:
                break;
        }
    }

    public int getLeftX() {
        return this.x_ - frameNormalImage_[0].getWidth() / 2;
    }

    public int getRightX() {
        return this.x_ + frameNormalImage_[0].getWidth() / 2;
    }

}



package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.zhang.pvz.plantsvszombies.MainActivity;


public class TuDouLei extends Plant {

    MainActivity activity_ = null;
    private Bitmap status_ready_ = null;
    private Bitmap status_bomb_ = null;
    private Bitmap status_text_ = null;
    private int timer_ = 185;
    private int bomb_timer_ = 5;

    public TuDouLei(int x, int y, DoublePlayerModeAView activity) {
        super(x, y, 8);

        this.activity_ = activity.activity_;
        this.life_ = 5;
        this.status_ = 0;//0:ready 1:ready end 2:bomb&text
        this.doublePlayerModeAView_ = activity;
        loadImage();
    }

    public void checkHit() {
        boolean have_ = false;
        for (Zombie zombie : doublePlayerModeAView_.zombies_) {
            if (zombie.getY() == this.getY()) {
                if (zombie.getX() >= this.getX()) {
                    if (this.getX() + 10 >= zombie.getLeftX() && this.status_ == 1) {
                        String str = "";
                        str += xDraw_ + "," + yDraw_;
                        //Log.e("tudoulei_jiancedaojiangshi", str);
                        if (!have_) have_ = true;
                        break;
                    }
                }
            }
        }
        if (have_) {
            for (Zombie zombie : doublePlayerModeAView_.zombies_) {
                if (zombie.getY() == this.getY()) {
                    if (zombie.getX() >= this.getX()) {
                        if (this.getX() + 50 >= zombie.getLeftX() && this.status_ == 1) {
                            zombie.setLife(0);
                            zombie.updateState();
                        }
                    }
                }
            }
            this.status_ = 2;
        }
    }

    protected void loadImage() {
        this.frameImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_TuDouLei);
        this.status_ready_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_TuDouLei_Ready)[0];
        this.status_bomb_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_TuDouLei_Bomb)[0];
        this.status_text_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_TUDouLei_Text)[0];
        this.type_ = MainActivity.PlantImageFactory.Plant_TuDouLei;
    }

    public void draw(Canvas canvas) {
        // ��canvas����ͼ�񣬻���ʹ��this.paint_
        switch (this.status_) {
            case 0:
                if (--timer_ == 0)
                    this.status_ = 1;
                canvas.drawBitmap(this.status_ready_, xDraw_, yDraw_, paint_);
                break;
            case 1:
                this.life_ = 1;
                canvas.drawBitmap(frameImage_[frameIndex_], xDraw_, yDraw_, paint_);
                frameIndex_ = (frameIndex_ + 1) % frameAmount_;
                break;
            case 2:
                Log.e("baozhatupian", "yes");
                canvas.drawBitmap(this.status_bomb_, xDraw_, yDraw_, paint_);
                canvas.drawBitmap(this.status_text_, xDraw_, yDraw_, paint_);
                if (--this.bomb_timer_ == 0) {
                    doublePlayerModeAView_.moveThread_.deletePlant_.add(this);
                    doublePlayerModeAView_.plants_.removeAll(doublePlayerModeAView_.moveThread_.deletePlant_);
                    doublePlayerModeAView_.moveThread_.deletePlant_.clear();
                }
                break;
        }
    }

}

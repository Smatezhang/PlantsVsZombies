package com.zhang.pvz.plantsvszombies.custom;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.zhang.pvz.plantsvszombies.MainActivity;



public class XiaoJianGuoQiang extends Plant {

    MainActivity activity_ = null;
    private Bitmap[] status_strong_ = null;
    private Bitmap[] status_breakdown_ = null;
    private Bitmap[] status_todie_ = null;

    private int strong_frameAmount_ = 16;
    private int breakdown_frameAmount_ = 11;
    private int todie_frameAmount_ = 15;

    private int strong_frameIndex_ = 0;
    private int breakdown_frameIndex_ = 0;
    private int todie_frameIndex_ = 0;

    public XiaoJianGuoQiang(int x, int y, DoublePlayerModeAView activity) {
        super(x, y, 16);

        this.life_ = 29;
        this.activity_ = activity.activity_;
        this.status_ = 0;//0:����20--30 1:����10--20 2:����1--0
        this.doublePlayerModeAView_ = activity;
        loadImage();
    }

    protected void loadImage() {
        this.frameImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiaoJianGuoQiang);
        this.status_strong_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiaoJianGuoQiang);
        this.status_breakdown_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiaoJianGuoQiang_DamageLess);
        this.status_todie_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiaoJianGuoQiang_DamageMore);
        this.type_ = MainActivity.PlantImageFactory.Plant_XiaoJianGuoQiang;
    }

    public void draw(Canvas canvas) {
        // ��canvas����ͼ�񣬻���ʹ��this.paint_
        switch (this.life_ / 10) {
            case 2:
                canvas.drawBitmap(this.status_strong_[this.strong_frameIndex_], xDraw_, yDraw_, paint_);
                this.strong_frameIndex_ = (this.strong_frameIndex_ + 1) % this.strong_frameAmount_;
                break;
            case 1:
                canvas.drawBitmap(this.status_breakdown_[this.breakdown_frameIndex_], xDraw_, yDraw_, paint_);
                this.breakdown_frameIndex_ = (this.breakdown_frameIndex_ + 1) % this.breakdown_frameAmount_;
                break;
            case 0:
                canvas.drawBitmap(this.status_todie_[this.todie_frameIndex_], xDraw_, yDraw_, paint_);
                this.todie_frameIndex_ = (this.todie_frameIndex_ + 1) % this.todie_frameAmount_;
                break;
        }
    }

    public void attack() {
        ;
    }
}

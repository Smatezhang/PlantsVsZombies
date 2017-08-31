package com.zhang.pvz.plantsvszombies.custom;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class MainZombie  extends Zombie
{
	
	DoublePlayerModeAView					doublePlayerModeAView_	= null;
	
	// 行走状态
	int											frameNormalIndex_ 		= 0;
	Bitmap[]									frameNormalImage_			= null;
	final int									frameNormalAmount_		= 16;
	
	public MainZombie(int x, int y, DoublePlayerModeAView activity)
	{
		super(x, y,activity);
		
		this.life_					= 10;
		this.speed_				= 0;
		
		this.type_					= 1;
	
		doublePlayerModeAView_	= activity;
		
		loadImage();
	}
	
	protected void loadImage()
	{
		this.frameNormalImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_Main);
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(frameNormalImage_[frameNormalIndex_], xDraw_, yDraw_, paint_);
		frameNormalIndex_ = (frameNormalIndex_ + 1) % frameNormalAmount_;
	}

	public void checkHit()
	{
	}
	
	public void move()
	{
	}
	
	public int getLeftX()
	{
		return this.x_ - frameNormalImage_[0].getWidth() / 2;
	}
	public int getRightX()
	{
		return this.x_ + frameNormalImage_[0].getWidth() / 2;
	}
	public boolean isRemoveable()
	{
		if (this.life_ <= 0)
			this.isRemoveable_ = true;
		
		return this.isRemoveable_; 
	}
}

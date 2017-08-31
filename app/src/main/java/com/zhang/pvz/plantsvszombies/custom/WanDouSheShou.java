package com.zhang.pvz.plantsvszombies.custom;


import com.zhang.pvz.plantsvszombies.MainActivity;

public class WanDouSheShou extends Plant
{
	public WanDouSheShou(int x, int y, DoublePlayerModeAView activity)
	{
		super(x, y, 13);
		
		this.life_								= 10;
		this.activity_ 						= activity.activity_;
		this.doublePlayerModeAView_			= activity;
		
		loadImage();
	}

	protected void loadImage()
	{
		frameImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_WanDouSheShou);
	}
	
	public void attack()
	{
		doublePlayerModeAView_.bullets_.add(new PuTongWanDou(x_, y_, doublePlayerModeAView_));
	}
}

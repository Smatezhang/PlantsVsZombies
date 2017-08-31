package com.zhang.pvz.plantsvszombies.custom;

import com.zhang.pvz.plantsvszombies.MainActivity;


public class ShuangChongSheShou extends Plant
{
	MainActivity activity_				= null;
	
	public ShuangChongSheShou(int x, int y, DoublePlayerModeAView activity)
	{
		super(x, y, 15);
		
		this.life_ = 10;
		this.activity_ = activity.activity_;
		this.doublePlayerModeAView_			= activity;
		loadImage();
	}

	protected void loadImage()
	{
		frameImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_ShuangChongSheShou);
		this.type_ = MainActivity.PlantImageFactory.Plant_ShuangChongSheShou;
	}
	
	public void attack()
	{
		boolean have_ = false;
		for (Zombie zombie : doublePlayerModeAView_.zombies_)
		{			
			if (zombie.getY() == this.getY())
			{				
				if (zombie.getX() - this.getX() <= 30)
				{
					have_ = true;
					break;
				}
			}
		}
		if(have_)
		{
			doublePlayerModeAView_.bullets_.add(new PuTongWanDou(x_, y_, doublePlayerModeAView_));
			doublePlayerModeAView_.bullets_.add(new PuTongWanDou(x_, y_, doublePlayerModeAView_));
		}
		else
		{
			doublePlayerModeAView_.bullets_.add(new PuTongWanDou(x_+25, y_, doublePlayerModeAView_));
			doublePlayerModeAView_.bullets_.add(new PuTongWanDou(x_, y_, doublePlayerModeAView_));
		}
	}
}

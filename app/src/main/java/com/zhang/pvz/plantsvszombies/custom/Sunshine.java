package com.zhang.pvz.plantsvszombies.custom;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class Sunshine
{
	protected MainActivity activity_					= null;
	protected DoublePlayerModeAView				doublePlayerModeAView_	= null;
	
	// 植物图片底边中点坐标
	protected	int				x_ 				= 0;		
	protected	int				y_ 				= 0;		
	
	// 贴图的坐标
	protected	int				xDraw_			= 0;		
	protected	int				yDraw_			= 0;

	// 用于在画布上绘图
	protected	Paint 			paint_			= new Paint();
	
	protected	boolean isCollected 						= false;
	
	public Sunshine(int x, int y)
	{
		this.x_ 			= x;
		this.y_ 			= y;
		this.xDraw_		= x - 40;
		this.yDraw_		= y - 40;	
	}
	
	protected void loadImage()
	{
	}
	
	public void draw(Canvas canvas)
	{	
	}

	public void move()
	{
	}
	
}

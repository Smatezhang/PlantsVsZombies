package com.zhang.pvz.plantsvszombies.custom;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class MenuView 	extends 		SurfaceView
							implements 	SurfaceHolder.Callback 
{
	Bitmap						menuImage_			= null;
	MainActivity activity_			= null;
	RefreshThread				refreshThread_	= null;
	Paint						paint_				= new Paint();
	Handler					handler_			= null;
	
	public static final int			MenuItem_SinglePlayer			= 0;
	public static final int			MenuItem_DoublePlayer			= 1;
	public static final int			MenuItem_About					= 2;
	public static final int			MenuItem_Option					= 3;
	public static final int			MenuItem_Help						= 4;
	public static final int			MenuItem_Exit						= 5;
	
	public static final int[][][]	menuItemRect		= new int[][][]
			{
				{{250, 35 }, 	{425, 120}},		// 单人模式
				{{255, 42 }, 	{405, 215}},		// 双人模式
				{{200, 230}, 	{245, 280}},		// 关于
				{{330, 230}, 	{380, 280}},		// 选项
				{{385, 235}, 	{425, 295}},		// 帮助
				{{430, 240}, 	{475, 290}},		// 退出
			};
	
	private MIDIPlayer	                menu_view_mMIDIPlayer	                    = null;
	
	public MenuView(MainActivity activity)
	{
		super(activity);
	
		activity_ 		= activity;
		
		initBitmap();

		getHolder().addCallback(this);
		
		this.refreshThread_	= new RefreshThread(getHolder(), this);
		
		if(this.activity_.hasBackgroundMusic())
		{
			menu_view_mMIDIPlayer = this.activity_.musicFactory_.getMusicPlayer(
					MainActivity.MusicFactory.Background_MenuView);			
			menu_view_mMIDIPlayer.PlayMusic();
		}			
	}

	public void initBitmap()
	{
		menuImage_ = activity_.otherFactory_.getImage(MainActivity.OtherFactory.Background_MainMenu);
	}
	
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		
		canvas.drawBitmap(this.menuImage_, 0, 0, paint_);
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{	
	}

	public void surfaceCreated(SurfaceHolder holder)
	{	
		this.refreshThread_.setFlag(true);
		this.refreshThread_.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) 
	{	
		boolean retry = true;
		refreshThread_.setFlag(false);
		while (retry) 
		{
			try 
			{
				refreshThread_.join();
				retry = false;
			} catch (InterruptedException e)
			{
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			int x = (int)event.getX();
			int y = (int)event.getY();

			if (isSelected(x, y, MenuItem_DoublePlayer))
			{
				if(this.activity_.hasBackgroundMusic())
					this.menu_view_mMIDIPlayer.FreeMusic();
				activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu_DoublePlayer);
			}
			if (isSelected(x, y, MenuItem_About))
			{
				if(this.activity_.hasBackgroundMusic())
					this.menu_view_mMIDIPlayer.FreeMusic();
				activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu_About);
			}
			if (isSelected(x, y, MenuItem_Option))
			{
				if(this.activity_.hasBackgroundMusic())
					this.menu_view_mMIDIPlayer.FreeMusic();
				activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu_Option);
			}
			if (isSelected(x, y, MenuItem_Help))
			{
				if(this.activity_.hasBackgroundMusic())
					this.menu_view_mMIDIPlayer.FreeMusic();
				activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu_Help_1);
			}
			if (isSelected(x, y, MenuItem_Exit))
			{
				if(this.activity_.hasBackgroundMusic())
					this.menu_view_mMIDIPlayer.FreeMusic();
				activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu_Exit);
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	private boolean isSelected(int x, int y, int menuItemId)
	{
		if (x > menuItemRect[menuItemId][0][0] 
				&& x < menuItemRect[menuItemId][1][0] 
				&& y > menuItemRect[menuItemId][0][1]
				&& y < menuItemRect[menuItemId][1][1])
			return true;
		
		return false;
	}
	public boolean onKeyDownUp(int keyCode, KeyEvent event)
	{		
		return false;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		return false;
	}
	
///////////////////////////////////////////////////////////////////////////////
	class RefreshThread extends Thread 
	{
		SurfaceHolder 		surfaceHolder_	= null;
		MenuView				menuView_			= null;
		boolean 				flag_				= false;
	
		public RefreshThread(SurfaceHolder surfaceHolder, MenuView menuView)
		{
			this.surfaceHolder_		= surfaceHolder;
			this.menuView_			= menuView;
		}
	
		public void setFlag(boolean flag) 
		{
			this.flag_ = flag;
		}
	
		public void run() 
		{
			Canvas 			canvas;
	
			while (this.flag_) 
			{
				canvas = null;
	
				try 
				{
					// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
					canvas = this.surfaceHolder_.lockCanvas(null);
					synchronized (this.surfaceHolder_)
					{
						menuView_.draw(canvas);
					}
				}
				finally
				{
					if (canvas != null) 
						this.surfaceHolder_.unlockCanvasAndPost(canvas);
				}
			}
		}
	}	// RefreshThread
}

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

public class OptionView 		extends SurfaceView 
								implements SurfaceHolder.Callback 
{
	Bitmap 						optionImage_ 			= null;
	MainActivity activity_ 				= null;
	RefreshThread 				refreshThread_ 		= null;
	Paint 							paint_ 				= new Paint();
	Handler 						handler_ 				= null;

	private MIDIPlayer	                menu_view_mMIDIPlayer	                    = null;
	
	public OptionView(MainActivity activity)
	{
		super(activity);

		activity_ = activity;

		initBitmap();

		getHolder().addCallback(this);

		this.refreshThread_ = new RefreshThread(getHolder(), this);
		
		if(this.activity_.hasBackgroundMusic())
		{
			menu_view_mMIDIPlayer = this.activity_.musicFactory_.getMusicPlayer(
					MainActivity.MusicFactory.Background_MenuView);
			menu_view_mMIDIPlayer.PlayMusic();
		}
	}

	private void initBitmap() 
	{
		this.optionImage_ = activity_.otherFactory_.getImage(MainActivity.OtherFactory.Background_MainMenu_Option);
	}

	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		
		canvas.drawBitmap(this.optionImage_, 0, 0, paint_);
		
		if (this.activity_.hasBackgroundMusic())
		{
			canvas.drawBitmap(activity_.otherFactory_.getImage(MainActivity.OtherFactory.Voice_On), 160, 245, paint_);
		}
		else
		{
			canvas.drawBitmap(activity_.otherFactory_.getImage(MainActivity.OtherFactory.Voice_Off), 165, 245, paint_);
		}
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

	public boolean onTouchEvent(MotionEvent event) 
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			int x = (int) event.getX();
			int y = (int) event.getY();
			
			if (x > 165 && x < 470 && y > 230 && y < 300)
			{
				if (this.activity_.hasBackgroundMusic())
				{
					this.activity_.disableBackgroundMusic();
					menu_view_mMIDIPlayer.FreeMusic();
				}
				else
				{
					this.activity_.enableBackgroundMusic();
					menu_view_mMIDIPlayer = this.activity_.musicFactory_.getMusicPlayer(
							MainActivity.MusicFactory.Background_MenuView);
					menu_view_mMIDIPlayer.PlayMusic();
				}
			}
			else
			{
				if(this.activity_.hasBackgroundMusic())
					this.menu_view_mMIDIPlayer.FreeMusic();
				activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu);
			}
		}

		return super.onTouchEvent(event);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (KeyEvent.KEYCODE_BACK == keyCode)
		{
			activity_.myHandler_.sendEmptyMessage(MainActivity.Message_MainMenu);
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
///////////////////////////////////////////////////////////////////////////////
	class RefreshThread extends Thread 
	{
		SurfaceHolder 	surfaceHolder_ 	= null;
		OptionView			optionView_ 		= null;
		boolean flag_ = false;

		public RefreshThread(SurfaceHolder surfaceHolder, OptionView optionView) 
		{
			this.surfaceHolder_ = surfaceHolder;
			this.optionView_ = optionView;
		}

		public void setFlag(boolean flag)
		{
			this.flag_ = flag;
		}

		public void run() 
		{
			Canvas canvas;

			while (this.flag_)
			{
				canvas = null;

				try
				{
					// 锁定整个画布，在内存要求比较高的情况下，建议参数不要为null
					canvas = this.surfaceHolder_.lockCanvas(null);
					synchronized (this.surfaceHolder_)
					{
						optionView_.draw(canvas);
					}
				} finally
				{
					if (canvas != null)
						this.surfaceHolder_.unlockCanvasAndPost(canvas);
				}
			}
		}
	} // RefreshThread
}

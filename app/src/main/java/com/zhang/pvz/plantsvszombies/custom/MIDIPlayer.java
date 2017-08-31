package com.zhang.pvz.plantsvszombies.custom;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

import com.zhang.pvz.plantsvszombies.R;

public class MIDIPlayer
{
	public MediaPlayer	playerMusic	= null;
	private Context		mContext	= null;


	public MIDIPlayer(Context context,int view_type)
	{
		mContext = context;
		/*
		String str = "";
		str += view_type;
		Log.e("33333333333333", str);
		*/
		switch(view_type)
		{
		case 0:
			playerMusic = MediaPlayer.create(mContext, R.raw.mainview);
			break;
		case 1:
			playerMusic = MediaPlayer.create(mContext, R.raw.doubleplayer);
			break;
		case 5:
			playerMusic = MediaPlayer.create(mContext, R.raw.zombie_win);
			break;	
		case 6:
			playerMusic = MediaPlayer.create(mContext, R.raw.plant_win);
			break;			
		case 12:
			playerMusic = MediaPlayer.create(mContext, R.raw.people_dead);
			break;
		default:
			playerMusic = MediaPlayer.create(mContext, R.raw.mainview);
			break;
		}		
		/* �����Ƿ�ѭ�� */
		playerMusic.setLooping(true);
		try
		{
			playerMusic.prepare();
		}
		catch (IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void SetPlayerLoop(boolean isloop)
	{
		playerMusic.setLooping(isloop);
	}
	/* �������� */
	public void PlayMusic()
	{
		/* װ����Դ�е����� */				
		playerMusic.start();
	}

	/* ֹͣ���ͷ����� */
	public void FreeMusic()
	{
		if (playerMusic != null)
		{			
		    playerMusic.seekTo(0);
		    playerMusic.pause();
		}
	}
}

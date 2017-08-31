package com.zhang.pvz.plantsvszombies.custom;

import java.util.ArrayList;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zhang.pvz.plantsvszombies.MainActivity;

public class DoublePlayerModeAView extends SurfaceView
        implements SurfaceHolder.Callback {
    Bitmap background_ = null;
    MainActivity activity_ = null;
    RefreshThread refreshThread_ = null;
    MoveThread moveThread_ = null;
    Paint paint_ = new Paint();
    Handler handler_ = null;
    DoublePlayerModeAView doublePlayerAView_ = null;
    Bitmap[][] cards_ = null;

    int Player_Type = Player_Plant;

    static final int Player_Plant = 0;
    static final int Player_Zombie = 1;

    // 所有卡片的位置
    static final int[][] cardPlantPostion = {{0, 0}, {50, 0}, {100, 0}, {150, 0}, {200, 0}};
    static final int[][] cardZombiePostion = {{280, 0}, {330, 0}, {380, 0}, {430, 0}};
    static final int[] sunCardPostion = {0, 0};
    static final int[] tieQiaoPostion = {0, 30};
    static final int[] tieQiaoBeiJingPostion = {0, 30};

    public int refreshBackground_ = 0;

    public boolean[] mainZombies_ = {true, true, true, true, true};

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private byte currentState = -1;
    private static final byte mousedownState = 0;
    private static final byte mousemoveState = 1;
    private static final byte mouseupState = 2;
    private float x;
    private float y;
    private int l;
    private int r;
    private float H = 45;
    private int[] disable = new int[]{0, 1, 1, 0, 0, 0, 1, 1, 1, 0};
    static boolean[] plantflag = new boolean[]{false, false, false, false, false, false, false, false, false, false,};
    private boolean addflag = false;
    int type;
    private float sx = 40;
    private float sy = 45;
    private float ex = 300;
    private float ey = 305;
    private boolean ifdraw = false;
    static final int[] plantx = {40, 84, 129, 172, 216, 260, 304, 348, 392};
    static final int[] planty = {45, 97, 149, 201, 253};
    private int[] each_cost = {50, 100, 200, 25, 50, 50, 125, 200, 275};
    private double[] each_cd = {5, 5, 5, 15, 20, 5, 7.5, 10, 15};
    private double[] cs_last = {0, 10, 10, 10, 15, 0, 10, 10, 10};
    //private double[] cs_last      = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int count = 0;
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // 植物卡是否能使用
    //boolean[]						isPlantCardEnable_		= {true, false, false, false, false};
    boolean[] isPlantCardEnable_ = {true, true, true, true, true, true, true, true, true};
    // 僵尸卡是否能使用
    //boolean[]						isZombieCardEnable_		= {true, false, false, false, false};
    boolean[] isZombieCardEnable_ = {true, true, true, true};
    // 当前是否选择了植物卡片
    boolean isPlantCardSelected_ = false;
    // 当前是否先则了僵尸卡片
    boolean isZombieCardSelected_ = false;
    // 选中植物的图片
    Bitmap selectedPlantImage_ = null;
    // 选中僵尸的图片
    Bitmap selectedZombieImage_ = null;
    // 选中的植物种类
    int selectedPlantType_ = 0;
    // 选中的僵尸种类
    int selectedZombieType_ = 0;

    // 方格底边坐标
    static final int[][][] gridPostion =
            {
                    {{79, 89}, {123, 89}, {167, 89}, {211, 89}, {255, 89}, {299, 89}, {343, 89}, {387, 89}, {431, 89}},
                    {{79, 143}, {123, 143}, {167, 143}, {211, 143}, {255, 143}, {299, 143}, {343, 143}, {387, 143}, {431, 143}},
                    {{79, 197}, {123, 197}, {167, 197}, {211, 197}, {255, 197}, {299, 197}, {343, 197}, {387, 197}, {431, 197}},
                    {{79, 251}, {123, 251}, {167, 251}, {211, 251}, {255, 251}, {299, 251}, {343, 251}, {387, 251}, {431, 251}},
                    {{79, 305}, {123, 305}, {167, 305}, {211, 305}, {255, 305}, {299, 305}, {343, 305}, {387, 305}, {431, 305}},
            };

    // 卡片消耗阳光数量信息位置
    static final int[][] cardCostPostion =
            {{30, 30}, {80, 30}, {130, 30}, {180, 30}, {230, 30}, {310, 30}, {360, 30}, {410, 30}, {460, 30}, {510
                    , 30}};

    static final int[][] mainZombiesPostion = {{475, 89}, {475, 143}, {475, 197}, {475, 251}, {475, 305}};


    boolean isStarted = false;


    // 游戏数据
    int plantSunAmount_ = 75;
    int zombieSunAmount_ = 75;
    String sP_sun = "75";
    String sZ_sun = "75";
    ArrayList<Plant> plants_ = new ArrayList<Plant>();
    ArrayList<Zombie> zombies_ = new ArrayList<Zombie>();
    ArrayList<Bullet> bullets_ = new ArrayList<Bullet>();
    ArrayList<Sunshine> sunshines_ = new ArrayList<Sunshine>();

    Paint paintText_ = new Paint();

    boolean isPause_ = true;

    private MIDIPlayer doubleplayer_view_mMIDIPlayer = null;

    public DoublePlayerModeAView(MainActivity activity) {
        super(activity);

        this.activity_ = activity;

        getHolder().addCallback(this);

        initBitmap();

        paintText_.setColor(0xFFFF0000);
        paintText_.setTextSize(20);
        paintText_.setFakeBoldText(true);

        paint_.setColor(0xFF9400D3);

        for (int i = 0; i < 5; ++i)
            this.zombies_.add(new MainZombie(mainZombiesPostion[i][0], mainZombiesPostion[i][1], this));

        this.refreshThread_ = new RefreshThread(getHolder(), this);
        this.moveThread_ = new MoveThread(this);

        if (this.activity_.hasBackgroundMusic()) {
            doubleplayer_view_mMIDIPlayer = this.activity_.musicFactory_.getMusicPlayer(
                    MainActivity.MusicFactory.Background_Fighting);
            doubleplayer_view_mMIDIPlayer.PlayMusic();
        }
    }

    public void initBitmap() {
        background_ = activity_.otherFactory_.getImage(MainActivity.OtherFactory.Background_Gress);

        cards_ = new Bitmap[][]
                {
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_LuZhangJiangShi),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_MuBei),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_PuTongJiangShi),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_ShuangChongSheShou),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_Sun),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_TieTongJiangShi),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_TuDouLei),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_WanDouSheShou),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_XiangRiKui),
                        activity_.cardImageFactory_.getImage(MainActivity.CardImageFactory.Card_XiaoJianGuoQiang)
                };
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (++count == 10) {
            count = 0;
            for (int i = 0; i < 9; i++) {
                cs_last[i] -= 0.5;
                if (cs_last[i] <= 0) plantflag[i] = true;
                else plantflag[i] = false;
            }
        }

        canvas.drawBitmap(this.background_, 0, 0, paint_);

        synchronized (this.zombies_) {

            for (int i = 0; i < 5; ++i)
                mainZombies_[i] = false;

            for (Zombie zombie : zombies_) {
                if (0 != zombie.type_) {
                    if (DoublePlayerModeAView.gridPostion[0][0][1] == zombie.getY())
                        mainZombies_[0] = true;
                    else if (DoublePlayerModeAView.gridPostion[1][0][1] == zombie.getY())
                        mainZombies_[1] = true;
                    else if (DoublePlayerModeAView.gridPostion[2][0][1] == zombie.getY())
                        mainZombies_[2] = true;
                    else if (DoublePlayerModeAView.gridPostion[3][0][1] == zombie.getY())
                        mainZombies_[3] = true;
                    else if (DoublePlayerModeAView.gridPostion[4][0][1] == zombie.getY())
                        mainZombies_[4] = true;
                }
            }

            for (int i = 0; i < 5; ++i)
                if (!this.mainZombies_[i])
                    canvas.drawBitmap(this.activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi_Die)[8],
                            mainZombiesPostion[i][0] - 90, mainZombiesPostion[i][1] - 80, paint_);
        }

        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_XiangRiKui][disable[0]], cardPlantPostion[0][0], cardPlantPostion[0][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_WanDouSheShou][disable[1]], cardPlantPostion[1][0], cardPlantPostion[1][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_ShuangChongSheShou][disable[2]], cardPlantPostion[2][0], cardPlantPostion[2][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_TuDouLei][disable[3]], cardPlantPostion[3][0], cardPlantPostion[3][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_XiaoJianGuoQiang][disable[4]], cardPlantPostion[4][0], cardPlantPostion[4][1], paint_);


        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_MuBei][disable[5]], cardZombiePostion[0][0], cardZombiePostion[0][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_PuTongJiangShi][disable[6]], cardZombiePostion[1][0], cardZombiePostion[1][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_LuZhangJiangShi][disable[7]], cardZombiePostion[2][0], cardZombiePostion[2][1], paint_);
        canvas.drawBitmap(cards_[MainActivity.CardImageFactory.Card_TieTongJiangShi][disable[8]], cardZombiePostion[3][0], cardZombiePostion[3][1], paint_);


        for (int i = 0; i < 9; i++) {
            if (cs_last[i] > 0) {
                String _str = "";
                _str += (cs_last[i]);
                canvas.drawText(_str, cardCostPostion[i][0] - 20, cardCostPostion[i][1] - 10, paintText_);
            }

        }
        for (int i = 0; i < 5; i++) {
            if (each_cost[i] <= plantSunAmount_) disable[i] = 0;
            else disable[i] = 1;

        }
        for (int i = 5; i < 9; i++) {
            if (each_cost[i] <= zombieSunAmount_) disable[i] = 0;
            else disable[i] = 1;
        }

        try {
            synchronized (this.plants_) {
                for (Plant plant : plants_)
                    plant.draw(canvas);

                synchronized (this.zombies_) {
                    for (Zombie zombie : zombies_)
                        zombie.draw(canvas);
                }
                synchronized (this.bullets_) {
                    for (Bullet bullet : bullets_)
                        bullet.draw(canvas);
                }
            }
            synchronized (this.sunshines_) {
                for (Sunshine sunshine : this.sunshines_)
                    sunshine.draw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sZ_sun = "";
        sZ_sun += zombieSunAmount_;
        sP_sun = "";
        sP_sun += plantSunAmount_;

        canvas.drawBitmap(this.activity_.sunImageFactory_.getImage(MainActivity.SunImageFactory.Sun_Plant), 0, 30, paint_);
        canvas.drawBitmap(this.activity_.sunImageFactory_.getImage(MainActivity.SunImageFactory.Sun_Zombie), 440, 30, paint_);

        paintText_.setColor(0xFF0000CD);
        canvas.drawText(sP_sun, 5, 55, paintText_);
        canvas.drawText(sZ_sun, 440, 55, paintText_);
        paintText_.setColor(0xFFFF0000);

        canvas.drawText("50", cardCostPostion[0][0], cardCostPostion[0][1], paint_);
        canvas.drawText("100", cardCostPostion[1][0], cardCostPostion[1][1], paint_);
        canvas.drawText("200", cardCostPostion[2][0], cardCostPostion[2][1], paint_);
        canvas.drawText("25", cardCostPostion[3][0], cardCostPostion[3][1], paint_);
        canvas.drawText("50", cardCostPostion[4][0], cardCostPostion[4][1], paint_);
        canvas.drawText("50", cardCostPostion[5][0], cardCostPostion[5][1], paint_);
        canvas.drawText("125", cardCostPostion[6][0], cardCostPostion[6][1], paint_);
        canvas.drawText("200", cardCostPostion[7][0], cardCostPostion[7][1], paint_);
        canvas.drawText("275", cardCostPostion[8][0], cardCostPostion[8][1], paint_);

        if (ifdraw && currentState == mousemoveState) {

            int i, j;
            if (0 <= type && type <= 5) {
                i = 15;
                j = 30;
            } else {
                i = 60;
                j = 70;
            }
            canvas.drawBitmap(selectedPlantImage_, x - i, y - j, paint_);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.refreshThread_.setFlag(true);
        this.refreshThread_.start();
        this.moveThread_.setFlag(true);
        this.moveThread_.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        refreshThread_.setFlag(false);
        while (retry) {
            try {
                refreshThread_.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
        retry = true;
        moveThread_.setFlag(false);
        while (retry) {
            try {
                moveThread_.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public boolean onTouchEvent(MotionEvent event) {

        touchEvent(event);
        return true;
    }
    //////////////////////////////////////////////////////////////////////////////////////

    public void touchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ifdraw = false;
                currentState = mousedownState;
                x = event.getX();
                y = event.getY();
                selectedPlantImage_ = null;

                if (checkIn(x, y)) {


                    if (checkXY(x, y, cardPlantPostion[0][0], 0, cardPlantPostion[1][0], H)) {
                        //Log.e("app", "1if");
                        if (disable[0] == 0 && plantflag[0]) {
                            ifdraw = true;
                            addflag = true;
                            type = 0;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardPlantPostion[1][0], 0, cardPlantPostion[2][0],
                            H)) {

                        if (disable[1] == 0 && plantflag[1]) {

                            ifdraw = true;
                            addflag = true;
                            type = 1;

                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardPlantPostion[2][0], 0, cardPlantPostion[3][0], H)) {

                        if (disable[2] == 0 && plantflag[2]) {

                            ifdraw = true;
                            addflag = true;
                            type = 2;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardPlantPostion[3][0], 0, cardPlantPostion[4][0], H)) {

                        if (disable[3] == 0 && plantflag[3]) {
                            ifdraw = true;
                            addflag = true;
                            type = 3;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardPlantPostion[4][0], 0, cardPlantPostion[4][0] + 50, H)) {

                        if (disable[4] == 0 && plantflag[4]) {
                            ifdraw = true;
                            addflag = true;
                            type = 4;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardZombiePostion[0][0], 0, cardZombiePostion[1][0], H)) {

                        if (disable[5] == 0 && plantflag[5]) {
                            ifdraw = true;
                            addflag = true;
                            type = 5;
                            getPlant(type);
                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardZombiePostion[1][0], 0, cardZombiePostion[2][0], H)) {

                        if (disable[6] == 0 && plantflag[6]) {
                            ifdraw = true;
                            addflag = true;
                            type = 6;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardZombiePostion[2][0], 0, cardZombiePostion[3][0], H)) {

                        if (disable[7] == 0 && plantflag[7]) {
                            ifdraw = true;
                            addflag = true;
                            type = 7;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    } else if (checkXY(x, y, cardZombiePostion[3][0], 0, cardZombiePostion[3][0] + 50, H)) {

                        if (disable[8] == 0 && plantflag[8]) {
                            ifdraw = true;
                            addflag = true;
                            type = 8;
                            getPlant(type);

                        } else {
                            addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                            return;
                        }
                    }
                } else {
                    addflag = false;// Ã»ÓÐ¿ÉÒÔÖÖµÄÖ²Îï ŸÍ²»ÖŽÐÐtouch·œ·šµÄÊó±ê·Å¿ªÊÂŒþÂßŒ­
                }
                break;
            case MotionEvent.ACTION_MOVE:
                currentState = mousemoveState;
                x = event.getX();
                y = event.getY();
                //Log.e("app", "move");
                break;
            case MotionEvent.ACTION_UP:

                currentState = mouseupState;
                if (type >= 0 && type < 5) {
                    sx = 40;
                    sy = 45;
                    ex = 300;
                    ey = 305;
                } else {
                    sx = 300;
                    sy = 45;
                    ex = 471;
                    ey = 305;
                }
                if (addflag) {
                    if (sx <= x && x < ex
                            && sy <= y && ey > y) {
                        for (int i = 0; i < 9; i++) {
                            if (x >= plantx[i] && x < plantx[i] + 44) l = i;
                        }
                        //Log.e("app", "l");
                        for (int i = 0; i < 5; i++) {
                            if (y >= planty[i] && y < planty[i] + 52) r = i;
                        }

                        boolean ifadd = true;
                        for (Plant plant : plants_) {
                            if (type >= 0 && type <= 5) {
                                if (plant.getX() == gridPostion[r][l][0] && plant.getY() == gridPostion[r][l][1]) {
                                    ifadd = false;
                                }
                            }
                        }

                        if (ifadd) {
                            if (type >= 0 && type < 5) plantSunAmount_ -= each_cost[type];

                            else zombieSunAmount_ -= each_cost[type];

                            cs_last[type] = each_cd[type];
                            AddObject(r, l);
                        }
                        //Log.e("app", "add");
                    }
                }

                break;
        }
    }


    public boolean checkIn(float x, float y) {
        if (checkXY(x, y, cardPlantPostion[0][0], 0, cardZombiePostion[3][0] + 50, H)) {
            return true;
        }
        return false;
    }

    private boolean checkXY(float cx, float cy, float sx, float sy, float ex,
                            float ey) {

        if (cx >= sx && cx <= ex && cy >= sy && cy <= ey) {
            return true;
        }

        return false;
    }


    public boolean AddObject(int x, int y) {
        switch (type) {
            case 0:
                plants_.add(new XiangRiKui(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 1:
                plants_.add(new WanDouSheShou(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 2:
                plants_.add(new ShuangChongSheShou(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 3:
                plants_.add(new TuDouLei(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 4:
                plants_.add(new XiaoJianGuoQiang(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 5:
                plants_.add(new MuBei(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 6:
                zombies_.add(new PuTongJiangShi(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 7:
                zombies_.add(new MaoZiJiangShi(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
            case 8:
                zombies_.add(new TieTongJiangShi(gridPostion[x][y][0], gridPostion[x][y][1], this));
                break;
        }
        return true;
    }


    //////////////////////////////////////////////////////////////////////////////////

    public boolean isCardSeleted(int x, int y, int card) {
        if (x >= cardPlantPostion[card][0] && x <= cardPlantPostion[card][0] + 50 && y >= 0 && y <= 30)
            return true;

        return false;
    }

    public void getPlant(int type) {
        if (isPlantCardEnable_[type]) {
            this.isPlantCardSelected_ = true;
            this.selectedPlantType_ = type;
            switch (type) {
                case 0:
                    this.selectedPlantImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiangRiKui)[0];
                    break;
                case 1:
                    this.selectedPlantImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_WanDouSheShou)[0];
                    break;
                case 2:
                    this.selectedPlantImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_ShuangChongSheShou)[0];
                    break;
                case 3:
                    this.selectedPlantImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_TuDouLei)[0];
                    break;
                case 4:
                    this.selectedPlantImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_XiaoJianGuoQiang)[0];
                    break;
                case 5:
                    this.selectedPlantImage_ = activity_.plantImageFactory_.getImage(MainActivity.PlantImageFactory.Plant_MuBei)[0];
                    break;
                case 6:
                    this.selectedPlantImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_PuTongJiangShi)[0];
                    break;
                case 7:
                    this.selectedPlantImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_LuZhangJiangShi)[0];
                    break;
                case 8:
                    this.selectedPlantImage_ = activity_.zombieImageFactory_.getImage(MainActivity.ZombieImageFactory.Zombie_TieTongJiangShi)[0];
                    break;
            }
        } else {
            this.isPlantCardSelected_ = false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    class RefreshThread extends Thread {
        SurfaceHolder surfaceHolder_ = null;
        DoublePlayerModeAView doublePlayerModeAView_ = null;
        boolean flag_ = false;
        int span_ = 50;
        int count_ = 0;

        public RefreshThread(SurfaceHolder surfaceHolder,
                             DoublePlayerModeAView doublePlayerModeAView_) {
            this.surfaceHolder_ = surfaceHolder;
            this.doublePlayerModeAView_ = doublePlayerModeAView_;
        }

        public void setFlag(boolean flag) {
            this.flag_ = flag;
        }

        public void run() {
            Canvas canvas;

            while (this.flag_) {
                canvas = null;

                try {
                    canvas = this.surfaceHolder_.lockCanvas(null);

                    synchronized (this.surfaceHolder_) {
                        doublePlayerModeAView_.draw(canvas);

                        if (count_ < 10) {
                            Bitmap bmp = doublePlayerModeAView_.activity_.otherFactory_.getImage(MainActivity.OtherFactory.Text_Prepare_01);
                            canvas.drawBitmap(bmp, 163, 132, paint_);
                            ++count_;
                        } else if (count_ < 20) {
                            Bitmap bmp = doublePlayerModeAView_.activity_.otherFactory_.getImage(MainActivity.OtherFactory.Text_Prepare_02);
                            canvas.drawBitmap(bmp, 163, 132, paint_);
                            ++count_;
                        } else if (count_ == 20) {
                            isPause_ = false;
                        }
                    }
                } finally {
                    if (canvas != null)
                        this.surfaceHolder_.unlockCanvasAndPost(canvas);
                }

                try {
                    Thread.sleep(span_);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } // RefreshThread

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    class MoveThread extends Thread {
        int span_ = 20;
        boolean flag_ = true;
        DoublePlayerModeAView doublePlayerModeAView_ = null;
        ArrayList<Bullet> deleteBollet_ = new ArrayList<Bullet>();
        ArrayList<Zombie> deleteZombie_ = new ArrayList<Zombie>();
        ArrayList<Plant> deletePlant_ = new ArrayList<Plant>();
        ArrayList<Sunshine> deleteSunshines_ = new ArrayList<Sunshine>();
        int countZombie_ = 0;
        int countPlant_ = 0;
        int countBullet_ = 0;

        public MoveThread(DoublePlayerModeAView doublePlayerModeAView) {
            this.doublePlayerModeAView_ = doublePlayerModeAView;
        }

        public void setFlag(boolean flag) {
            this.flag_ = flag;
        }

        public void run() {
            while (flag_) {
                if (isPause_)
                    continue;

                try {
                    synchronized (this.doublePlayerModeAView_.bullets_) {
                        if (countBullet_ == 0) {
                            for (Bullet bullet : doublePlayerModeAView_.bullets_) {
                                bullet.move();
                                bullet.checkHit();
                            }

                            this.doublePlayerModeAView_.bullets_.removeAll(this.deleteBollet_);
                            this.deleteBollet_.clear();
                        }

                        countBullet_ = (countBullet_ + 1) % 5;
                    }
                    synchronized (this.doublePlayerModeAView_.zombies_) {
                        for (Zombie zombie : doublePlayerModeAView_.zombies_) {
                            if (zombie.isRemoveable())
                                deleteZombie_.add(zombie);
                        }

                        doublePlayerModeAView_.zombies_.removeAll(deleteZombie_);
                        this.deleteZombie_.clear();

                        int mainZombieAmount = 0;

                        for (int i = 0; i < 5; ++i)
                            doublePlayerModeAView_.mainZombies_[i] = false;

                        for (Zombie zombie : doublePlayerModeAView_.zombies_) {
                            if (0 != zombie.type_) {
                                ++mainZombieAmount;

                                if (DoublePlayerModeAView.gridPostion[0][0][1] == zombie.getY()) {
                                    doublePlayerModeAView_.mainZombies_[0] = true;
                                } else if (DoublePlayerModeAView.gridPostion[1][0][1] == zombie.getY()) {
                                    doublePlayerModeAView_.mainZombies_[1] = true;
                                } else if (DoublePlayerModeAView.gridPostion[2][0][1] == zombie.getY()) {
                                    doublePlayerModeAView_.mainZombies_[2] = true;
                                } else if (DoublePlayerModeAView.gridPostion[3][0][1] == zombie.getY()) {
                                    doublePlayerModeAView_.mainZombies_[3] = true;
                                } else if (DoublePlayerModeAView.gridPostion[4][0][1] == zombie.getY()) {
                                    doublePlayerModeAView_.mainZombies_[4] = true;
                                }
                            }
                        }

                        if (mainZombieAmount <= 2) {
                            if (this.doublePlayerModeAView_.activity_.hasBackgroundMusic())
                                doublePlayerModeAView_.doubleplayer_view_mMIDIPlayer.FreeMusic();
                            doublePlayerModeAView_.activity_.myHandler_.sendEmptyMessage(MainActivity.Message_PlantWin);
                        }

                        if (countZombie_ == 0) {
                            for (Zombie zombie : doublePlayerModeAView_.zombies_) {
                                if (zombie.getX() <= 30) {
                                    if (this.doublePlayerModeAView_.activity_.hasBackgroundMusic())
                                        doublePlayerModeAView_.doubleplayer_view_mMIDIPlayer.FreeMusic();
                                    doublePlayerModeAView_.activity_.myHandler_.sendEmptyMessage(MainActivity.Message_ZombieWin);
                                }

                                zombie.move();
                                zombie.checkHit();
                            }
                        }

                        countZombie_ = (countZombie_ + 1) % 10;
                    }

                    synchronized (this.doublePlayerModeAView_.plants_) {
                        for (Plant plant : doublePlayerModeAView_.plants_) {
                            if (plant.isDead())
                                deletePlant_.add(plant);
                            else if (plant.type_ == MainActivity.PlantImageFactory.Plant_TuDouLei)
                                plant.checkHit();
                        }

                        doublePlayerModeAView_.plants_.removeAll(deletePlant_);
                        this.deleteBollet_.clear();

                        if (countPlant_ == 0) {
                            for (Plant plant : doublePlayerModeAView_.plants_) {
                                plant.attack();
                            }
                        }

                        countPlant_ = (countPlant_ + 1) % 200;
                    }

                    synchronized (this.deleteSunshines_) {
                        for (Sunshine sunshine : this.doublePlayerModeAView_.sunshines_)
                            sunshine.move();

                        doublePlayerModeAView_.sunshines_.removeAll(deleteSunshines_);
                        this.deleteSunshines_.clear();

                    }

                    Thread.sleep(span_);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }    // MoveThread
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
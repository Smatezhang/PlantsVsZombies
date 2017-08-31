package com.zhang.pvz.plantsvszombies;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;

import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.zhang.pvz.plantsvszombies.custom.AboutView;
import com.zhang.pvz.plantsvszombies.custom.DoublePlayerModeAView;
import com.zhang.pvz.plantsvszombies.custom.HelpViewA;
import com.zhang.pvz.plantsvszombies.custom.HelpViewB;
import com.zhang.pvz.plantsvszombies.custom.MIDIPlayer;
import com.zhang.pvz.plantsvszombies.custom.MenuView;
import com.zhang.pvz.plantsvszombies.custom.OptionView;
import com.zhang.pvz.plantsvszombies.custom.PlantWinView;
import com.zhang.pvz.plantsvszombies.custom.ProcessView;
import com.zhang.pvz.plantsvszombies.custom.ZombieWinView;

public class MainActivity extends Activity {
    // 界面
    MenuView menuView_ = null;
    HelpViewA helpView_ = null;
    AboutView aboutView_ = null;
    OptionView optionView_ = null;
    DoublePlayerModeAView doublePlayerModeAView_ = null;
    ZombieWinView zombieWinView_ = null;
    PlantWinView plantWinView_ = null;

    public int screenWidth_ = 0;
    public int screenHeight_ = 0;

    MainActivity activity_ = this;

    // 加载游戏的进度条界面
    ProcessView processView_ = null;
    // 是否开启游戏音效
    boolean hasMusic_ = true;

    // 资源
    public ImageFactory imageFactory_ = null;
    public PlantImageFactory plantImageFactory_ = null;
    public ZombieImageFactory zombieImageFactory_ = null;
    public CardImageFactory cardImageFactory_ = null;
    public BulletImageFactory bulletImageFactory_ = null;
    public SunImageFactory sunImageFactory_ = null;
    public OtherFactory otherFactory_ = null;
    public MusicFactory musicFactory_ = null;

    public Bitmap processViewBackground_;

    public static final int Message_Failed = 0;
    public static final int Message_MainMenu = 1;
    public static final int Message_MainMenu_SinglePlayer = 2;
    public static final int Message_MainMenu_DoublePlayer = 3;
    public static final int Message_MainMenu_About = 4;
    public static final int Message_MainMenu_Option = 5;
    public static final int Message_MainMenu_Help_1 = 6;
    public static final int Message_MainMenu_Exit = 7;
    public static final int Message_ZombieWin = 8;
    public static final int Message_PlantWin = 9;
    public static final int Message_MainMenu_Help_2 = 10;
    public static final int Message_MainMenu_VisitWeb = 11;

    public Handler myHandler_ = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Message_Failed:
                    onFailed();
                    break;
                case Message_MainMenu:
                    onMainMenu();
                    break;
                case Message_MainMenu_DoublePlayer:
                    onMainMenuDoublePlayer();
                    break;
                case Message_MainMenu_About:
                    onMainMenuAbout();
                    break;
                case Message_MainMenu_Option:
                    onMainMenuOption();
                    break;
                case Message_MainMenu_Help_1:
                    onMainMenuHelpA();
                    break;
                case Message_MainMenu_Exit:
                    onExit();
                    break;
                case Message_ZombieWin:
                    onZombieWin();
                    break;
                case Message_PlantWin:
                    onPlantWin();
                    break;
                case Message_MainMenu_Help_2:
                    onMainMenuHelpB();
                    break;
                case Message_MainMenu_VisitWeb:
                    onVisitWeb();
                    break;
            }
        }
    };

    private void onFailed() {
        onExit();
    }

    private void onMainMenu() {
        this.menuView_ = new MenuView(this);
        this.setContentView(this.menuView_);
    }

    protected void onMainMenuSinglePlayer() {
        // TODO Auto-generated method stub
    }

    private void onMainMenuDoublePlayer() {
        this.doublePlayerModeAView_ = new DoublePlayerModeAView(this);
        this.setContentView(this.doublePlayerModeAView_);
    }

    private void onMainMenuAbout() {
        this.aboutView_ = new AboutView(this);
        this.setContentView(this.aboutView_);
    }

    private void onMainMenuOption() {
        this.optionView_ = new OptionView(this);
        this.setContentView(this.optionView_);
    }

    private void onMainMenuHelpA() {
        this.setContentView(new HelpViewA(this));
    }

    private void onExit() {
        System.exit(0);
    }

    private void onPlantWin() {
        this.plantWinView_ = new PlantWinView(this);
        this.setContentView(this.plantWinView_);
    }

    private void onZombieWin() {
        this.zombieWinView_ = new ZombieWinView(this);
        this.setContentView(this.zombieWinView_);
    }

    private void onMainMenuHelpB() {
        this.setContentView(new HelpViewB(this));
    }

    private void onVisitWeb() {
        /*
		Uri uri = Uri.parse("http://blog.csdn.net/MDL13412");
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
		*/
        onMainMenu();
    }

    public boolean hasBackgroundMusic() {
        return this.hasMusic_;
    }

    public void enableBackgroundMusic() {
        this.hasMusic_ = true;
    }

    public void disableBackgroundMusic() {
        this.hasMusic_ = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenHeight_= display.getHeight();
        screenWidth_ = display.getWidth();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.processViewBackground_ = BitmapFactory.decodeResource(getResources(), R.drawable.background_processview);

        // 加载进度条
        processView_ = new ProcessView(this, processViewBackground_);
        this.setContentView(processView_);


        new Thread() {
            public void run() {
                // 进度增长85%
                try {
                    activity_.imageFactory_ = new ImageFactory();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                activity_.plantImageFactory_ = new PlantImageFactory(activity_.imageFactory_);
                activity_.zombieImageFactory_ = new ZombieImageFactory(activity_.imageFactory_);
                activity_.cardImageFactory_ = new CardImageFactory(activity_.imageFactory_);
                activity_.bulletImageFactory_ = new BulletImageFactory(activity_.imageFactory_);
                activity_.sunImageFactory_ = new SunImageFactory(activity_.imageFactory_);
                activity_.otherFactory_ = new OtherFactory(activity_.imageFactory_);
                activity_.musicFactory_ = new MusicFactory(activity_.imageFactory_);

                ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 15);
            }
        }.start();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 进度增长45%
    public class ImageFactory {
        public Bitmap[][] plants_;
        public Bitmap[][] zombies_;
        public Bitmap[][] cards_;
        public Bitmap[][] bullets_;
        public Bitmap[] suns_;
        public Bitmap[] others_ = null;

        public ImageFactory() throws InterruptedException {
            initBitmaps();
        }

        private void initBitmaps() throws InterruptedException {
            initPlants();        // 进度增长30%
            initZombies();    // 进度增长30%
            initCards();        // 进度增长5%
            initBullets();    // 进度增长5%
            initSuns();        // 进度增长5%
            initOthers();        // 进度增长10%
        }

        public Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
            int width = bgimage.getWidth();
            int height = bgimage.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
                    matrix, true);
            return bitmap;

        }

        private void initPlants() {
            this.plants_ = new Bitmap[][]
                    {
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_11),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_12),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_13),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_14),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_shuangchongsheshou_15)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_08)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_bomb)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_ready)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_tudoulei_text)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_11),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_12),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_wandousheshou_13),
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_11),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_12),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_13),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_14),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_15),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_16),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_17),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiangrikui_18)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_11),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_12),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_13),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_14),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_15),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_16)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damageless_11)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_11),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_12),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_13),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_14),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_xiaojianguoqiang_damagemore_15)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.plant_mubei)
                            }
                    };

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 20);

            zoomPlant(this.plants_[PlantImageFactory.Plant_ShuangChongSheShou], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_TuDouLei], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_WanDouSheShou], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_XiangRiKui], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_XiaoJianGuoQiang], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_XiaoJianGuoQiang_DamageLess], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_XiaoJianGuoQiang_DamageMore], 40, 40);
            zoomPlant(this.plants_[PlantImageFactory.Plant_MuBei], 40, 40);

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 10);
        }

        private void zoomPlant(Bitmap[] plants, int width, int height) {
            int length = plants.length;

            for (int i = 0; i < length; ++i)
                plants[i] = zoomImage(plants[i], width, height);
        }

        private void initZombies() {
            this.zombies_ = new Bitmap[][]
                    {
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_01),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_02),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_03),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_04),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_05),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_06),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_07),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_08),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_09),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_10),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_11),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_12),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_13),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_14),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_15),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_16),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_17),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_18),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_19),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_20),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_21),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_22)

                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_01),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_02),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_03),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_04),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_05),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_06),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_07),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_08),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_09),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_10),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_11),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_12),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_13),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_14),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_15),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_16),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_17),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_18),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_19),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_20),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_21)},
                            {
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_01),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_02),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_03),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_04),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_05),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_06),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_07),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_08),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_09),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_10),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_attack_losthead_11)},
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_01),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_02),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_03),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_04),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_05),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_06),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_07),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_08),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_09),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_die_10)},
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_01),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_02),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_03),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_04),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_05),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_06),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_07),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_08),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_09),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_10),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_putongjiangshi_head_11),},
                            {
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_01),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_02),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_03),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_04),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_05),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_06),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_07),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_08),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_09),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_10),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_11),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_12),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_13),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_14),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_15),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_16),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_17),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_putongjiangshi_losthead_18)},
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_01),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_02),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_03),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_04),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_05),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_06),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_07),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_08),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_09),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_10),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_11),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_12),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_13),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_14),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_15),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_16),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_17),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_18),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_19),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_20),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_luzhangjiangshi_21)},
                            {
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_01),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_02),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_03),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_04),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_05),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_06),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_07),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_08),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_09),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_10),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_luzhangjiangshi_attack_11)},
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_01),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_02),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_03),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_04),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_05),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_06),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_07),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_08),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_09),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_10),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_11),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_12),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_13),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_14),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.zombie_tietongjiangshi_15)},
                            {
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_01),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_02),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_03),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_04),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_05),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_06),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_07),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_08),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_09),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_10),
                                    BitmapFactory
                                            .decodeResource(
                                            getResources(),
                                            R.drawable.zombie_tietongjiangshi_attack_11)},
                            {
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_01),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_02),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_03),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_04),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_05),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_06),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_07),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_08),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_09),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_10),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_11),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_12),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_13),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_14),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_15),
                                    BitmapFactory.decodeResource(getResources(), R.drawable.zombie_main_16)

                            }
                    };

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 20);

            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_PuTongJiangShi], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_PuTongJiangShi_Attack], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_PuTongJiangShi_Attack_Losthead], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_PuTongJiangShi_Die], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_PuTongJiangShi_Head], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_PuTongJiangShi_Losthead], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_LuZhangJiangShi], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_LuZhangJiangShi_Attack], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_TieTongJiangShi], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_TieTongJiangshi_Attack], 90, 80);
            zoomZombie(this.zombies_[ZombieImageFactory.Zombie_Main], 90, 80);

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 10);
        }

        private void zoomZombie(Bitmap[] zombies, int width, int height) {
            int length = zombies.length;

            for (int i = 0; i < length; ++i)
                zombies[i] = zoomImage(zombies[i], width, height);
        }

        private void initCards() {
            this.cards_ = new Bitmap[][]
                    {
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_luzhangjiangshi_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_luzhangjiangshi_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_mubei_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_mubei_off)},
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_putongjiangshi_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_putongjiangshi_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_shuangchongsheshou_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_shuangchongsheshou_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_sun)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_tietongjiangshi_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_tietongjiangshi_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_tudoulei_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_tudoulei_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_wandousheshou_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_wandousheshou_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_xiangrikui_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_xiangrikui_off)
                            },
                            {
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_xiaojianguoqiang_on),
                                    BitmapFactory.decodeResource(getResources(),
                                            R.drawable.card_xiaojianguoqiang_off)
                            }
                    };

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 5);
        }

        private void initBullets() {
            this.bullets_ = new Bitmap[][]
                    {
                            {BitmapFactory.decodeResource(getResources(), R.drawable.bullet_putongwandou)},
                            {BitmapFactory.decodeResource(getResources(), R.drawable.bullet_putongwandou_hit)},
                    };

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 5);
        }

        private void initSuns() {
            this.suns_ = new Bitmap[]
                    {
                            BitmapFactory.decodeResource(getResources(), R.drawable.sun_plant),
                            BitmapFactory.decodeResource(getResources(), R.drawable.sun_zombie)
                    };

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 5);
        }

        private void initOthers() {
            this.others_ = new Bitmap[]
                    {
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_processview),
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_gress),
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_mainmenu),
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_mainmenu_help_1),
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_mainmenu_about),
                            BitmapFactory.decodeResource(getResources(), R.drawable.text_prepare_01),
                            BitmapFactory.decodeResource(getResources(), R.drawable.text_prepare_02),
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_mainmenu_help_2),
                            BitmapFactory.decodeResource(getResources(), R.drawable.background_mainmenu_option),
                            BitmapFactory.decodeResource(getResources(), R.drawable.voice_on),
                            BitmapFactory.decodeResource(getResources(), R.drawable.voice_off),
                            BitmapFactory.decodeResource(getResources(), R.drawable.plant_win),
                            BitmapFactory.decodeResource(getResources(), R.drawable.zombie_win)
                    };

            ProcessView.setProcessPercent(ProcessView.getProcessPercent() + 10);
        }

        public Bitmap[] getPlant(int type) {
            return this.plants_[type];
        }

        public Bitmap[] getZombie(int type) {
            return this.zombies_[type];
        }

        public Bitmap[] getCard(int type) {
            return this.cards_[type];
        }

        public Bitmap getSun(int type) {
            return this.suns_[type];
        }

        public Bitmap[] getBullets(int type) {
            return this.bullets_[type];
        }

        public Bitmap getOthers(int type) {
            return this.others_[type];
        }
    }    // ImageFactory

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class PlantImageFactory {
        public static final int Plant_ShuangChongSheShou = 0;
        public static final int Plant_TuDouLei = 1;
        public static final int Plant_TuDouLei_Bomb = 2;
        public static final int Plant_TuDouLei_Ready = 3;
        public static final int Plant_TUDouLei_Text = 4;
        public static final int Plant_WanDouSheShou = 5;
        public static final int Plant_XiangRiKui = 6;
        public static final int Plant_XiaoJianGuoQiang = 7;
        public static final int Plant_XiaoJianGuoQiang_DamageLess = 8;
        public static final int Plant_XiaoJianGuoQiang_DamageMore = 9;
        public static final int Plant_MuBei = 10;

        ImageFactory imageFactory_;

        public PlantImageFactory(ImageFactory imageFactory) {
            this.imageFactory_ = imageFactory;
        }

        public Bitmap[] getImage(int type) {
            return this.imageFactory_.getPlant(type);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ZombieImageFactory {
        // 普通僵尸
        public static final int Zombie_PuTongJiangShi = 0;
        public static final int Zombie_PuTongJiangShi_Attack = 1;
        public static final int Zombie_PuTongJiangShi_Attack_Losthead = 2;
        public static final int Zombie_PuTongJiangShi_Die = 3;
        public static final int Zombie_PuTongJiangShi_Head = 4;
        public static final int Zombie_PuTongJiangShi_Losthead = 5;
        ;

        // 路障僵尸
        public static final int Zombie_LuZhangJiangShi = 6;
        public static final int Zombie_LuZhangJiangShi_Attack = 7;

        // 铁通僵尸
        public static final int Zombie_TieTongJiangShi = 8;
        public static final int Zombie_TieTongJiangshi_Attack = 9;

        public static final int Zombie_Main = 10;

        ImageFactory imageFactory_;

        public ZombieImageFactory(ImageFactory imageFactory) {
            this.imageFactory_ = imageFactory;
        }

        public Bitmap[] getImage(int type) {
            return this.imageFactory_.getZombie(type);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class CardImageFactory {
        public static final int Card_LuZhangJiangShi = 0;
        public static final int Card_MuBei = 1;
        public static final int Card_PuTongJiangShi = 2;
        public static final int Card_ShuangChongSheShou = 3;
        public static final int Card_Sun = 4;
        public static final int Card_TieTongJiangShi = 5;
        public static final int Card_TuDouLei = 6;
        public static final int Card_WanDouSheShou = 7;
        public static final int Card_XiangRiKui = 8;
        public static final int Card_XiaoJianGuoQiang = 9;

        ImageFactory imageFactory_;

        public CardImageFactory(ImageFactory imageFactory) {
            this.imageFactory_ = imageFactory;
        }

        public Bitmap[] getImage(int type) {
            return this.imageFactory_.getCard(type);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class BulletImageFactory {
        public static final int Bullet_PuTongWanDou = 0;
        public static final int Bullet_PuTongWanDou_Hit = 1;

        ImageFactory imageFactory_;

        public BulletImageFactory(ImageFactory imageFactory) {
            this.imageFactory_ = imageFactory;
        }

        public Bitmap[] getImage(int type) {
            return this.imageFactory_.getBullets(type);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class SunImageFactory {
        public static final int Sun_Plant = 0;
        public static final int Sun_Zombie = 1;

        ImageFactory imageFactory_;

        public SunImageFactory(ImageFactory imageFactory) {
            this.imageFactory_ = imageFactory;
        }

        public Bitmap getImage(int type) {
            return this.imageFactory_.getSun(type);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class OtherFactory {
        public final static int Background_ProcessView = 0;
        public final static int Background_Gress = 1;
        public final static int Background_MainMenu = 2;
        public final static int Background_MainMenu_Help_1 = 3;
        public final static int Background_MainMenu_About = 4;
        public final static int Text_Prepare_01 = 5;
        public final static int Text_Prepare_02 = 6;
        public final static int Background_MainMenu_Help_2 = 7;
        public final static int Background_MainMenu_Option = 8;
        public final static int Voice_On = 9;
        public final static int Voice_Off = 10;
        public final static int Background_Plant_Win = 11;
        public final static int Background_Zombie_Win = 12;

        ImageFactory imageFactory_;

        public OtherFactory(ImageFactory imageFactory) {
            this.imageFactory_ = imageFactory;
        }

        public Bitmap getImage(int type) {
            return this.imageFactory_.getOthers(type);
        }
    }    // OtherFactory

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class MusicFactory {
        public final static int Background_MenuView = 0;
        public final static int Background_Fighting = 1;
        public final static int Background_HelpViewA = 2;
        public final static int Background_HelpViewB = 3;
        public final static int Background_AboutView = 4;
        public final static int Background_Zombie_WinView = 5;
        public final static int Background_Plant_WinView = 6;
        public final static int Zombie_Eat = 7;
        public final static int Zombie_PuTong_Hited = 8;
        public final static int Zombie_TieTong_Hited = 9;
        public final static int Zombie_ComeIn = 10;
        public final static int Plant_ZhongXia = 11;
        public final static int People_Dead = 12;
    	/*
    	public final static int Zombie_TieTong_Hited							= 10;
    	public final static int Zombie_ComeIn								    = 11;
    	*/

        private MIDIPlayer[] musicplayer_;

        public MusicFactory(ImageFactory imageFactory_) {
            this.musicplayer_ = new MIDIPlayer[]
                    {
                            new MIDIPlayer(activity_, Background_MenuView),
                            new MIDIPlayer(activity_, Background_Fighting),
                            new MIDIPlayer(activity_, Background_HelpViewA),
                            new MIDIPlayer(activity_, Background_HelpViewB),
                            new MIDIPlayer(activity_, Background_AboutView),
                            new MIDIPlayer(activity_, Background_Zombie_WinView),
                            new MIDIPlayer(activity_, Background_Plant_WinView),
                            new MIDIPlayer(activity_, Zombie_Eat),
                            new MIDIPlayer(activity_, Zombie_PuTong_Hited),
                            new MIDIPlayer(activity_, Zombie_TieTong_Hited),
                            new MIDIPlayer(activity_, Zombie_ComeIn),
                            new MIDIPlayer(activity_, Plant_ZhongXia),
                            new MIDIPlayer(activity_, People_Dead)
                    };
        }

        public MIDIPlayer getMusicPlayer(int type) {
            return this.musicplayer_[type];
        }
    }
}

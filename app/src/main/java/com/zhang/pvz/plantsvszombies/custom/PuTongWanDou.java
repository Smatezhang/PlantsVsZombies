package com.zhang.pvz.plantsvszombies.custom;

import com.zhang.pvz.plantsvszombies.MainActivity;


public class PuTongWanDou extends Bullet {
    MainActivity activity_ = null;
    DoublePlayerModeAView doublePlayerModeAView_ = null;

    public PuTongWanDou(int x, int y, DoublePlayerModeAView activity) {
        super(x, y);

        this.bulletPower_ = 1;
        this.speed_ = 4;
        this.activity_ = activity.activity_;
        doublePlayerModeAView_ = activity;

        loadImage();
    }

    protected void loadImage() {
        buttleImage_ = activity_.bulletImageFactory_.getImage(MainActivity.BulletImageFactory.Bullet_PuTongWanDou)[0];
        buttleHitImage_ = activity_.bulletImageFactory_.getImage(MainActivity.BulletImageFactory.Bullet_PuTongWanDou_Hit)[0];
    }

    public void checkHit() {
        // 判断当前行是否有墓碑
        boolean mubeis[] = {false, false, false};
        Plant mubeisRef[] = {null, null, null};

        for (Plant plant : doublePlayerModeAView_.plants_) {
            if (plant.getY() == this.getY() && plant.type_ == MainActivity.PlantImageFactory.Plant_MuBei) {
                switch (plant.getX()) {
                    case 343:
                        mubeis[0] = true;
                        mubeisRef[0] = plant;
                        break;
                    case 387:
                        mubeis[1] = true;
                        mubeisRef[1] = plant;
                        break;
                    case 431:
                        mubeis[2] = true;
                        mubeisRef[2] = plant;
                        break;
                }
            }
        }

        if (this.x_ < 343)    // 在墓碑前面的情况
        {
            for (Zombie zombie : doublePlayerModeAView_.zombies_) {
                if (zombie.getY() == this.getY()) {
                    if (zombie.getX() >= this.getX()) {
                        if (this.getX() >= zombie.getLeftX()) {
                            if (!this.isHited()) {
                                zombie.setLife(zombie.getLife() - this.getPower());
                                if (zombie.getLife() == 10) {
                                    zombie.change();
                                }
                                zombie.updateState();
                                this.setHited();
                            }

                            if (this.isRemoveable()) {
                                doublePlayerModeAView_.moveThread_.deleteBollet_.add(this);
                            }
                        }
                    }
                }
            }
        } else if (this.x_ >= 475 - 10 && this.x_ <= 475 + 5) {
            for (Zombie zombie : doublePlayerModeAView_.zombies_) {
                if (zombie.getY() == this.getY()) {
                    if (zombie.getX() == 475) {
                        if (!this.isHited()) {
                            zombie.setLife(zombie.getLife() - this.getPower());
                            if (zombie.getLife() == 10) {
                                zombie.change();
                            }
                            zombie.updateState();
                            this.setHited();
                        }

                        if (this.isRemoveable()) {
                            doublePlayerModeAView_.moveThread_.deleteBollet_.add(this);
                        }
                    }
                }
            }
        } else    // 在僵尸草地的情况
        {
            if (this.getX() >= 343 - 10 && this.getX() <= 343 + 5) {
                if (mubeis[0]) {
                    if (!this.isHited()) {
                        mubeisRef[0].setLife(mubeisRef[0].getLife() - this.getPower());
                        this.setHited();
                    }

                    if (this.isRemoveable())
                        doublePlayerModeAView_.moveThread_.deleteBollet_.add(this);
                }
            } else if (this.getX() >= 387 - 10 && this.getX() <= 387 + 5) {
                if (mubeis[1]) {
                    if (!this.isHited()) {
                        mubeisRef[1].setLife(mubeisRef[1].getLife() - this.getPower());
                        this.setHited();
                    }

                    if (this.isRemoveable())
                        doublePlayerModeAView_.moveThread_.deleteBollet_.add(this);
                }
            } else if (this.getX() >= 431 - 5 && this.getX() <= 431 + 5) {
                if (mubeis[2]) {
                    if (!this.isHited()) {
                        mubeisRef[2].setLife(mubeisRef[2].getLife() - this.getPower());
                        this.setHited();
                    }

                    if (this.isRemoveable())
                        doublePlayerModeAView_.moveThread_.deleteBollet_.add(this);
                }
            }
            /*
			else
			{
				for (Zombie zombie : doublePlayerModeAView_.zombies_)
				{
					if (zombie.getY() == this.getY())
					{
						if (zombie.getX() >= this.getX())
						{
							if (this.getX() >= zombie.getLeftX())
							{
								if (!this.isHited())
								{
									zombie.setLife(zombie.getLife() - this.getPower());
									if(zombie.getLife()==10)
									{
										zombie.change();
									}
									zombie.updateState();
									this.setHited();
								}
								
								if (this.isRemoveable())
								{
									doublePlayerModeAView_.moveThread_.deleteBollet_.add(this);
								}
							}
						}
					}
				}
			}
			*/
        }
    }

    public int getLeftX() {
        return this.x_ - buttleImage_.getWidth() / 2;
    }

    public int getRightX() {
        return this.x_ + buttleImage_.getWidth() / 2;
    }
}

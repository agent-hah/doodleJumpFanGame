package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends GameObj {
    public static final String IMG_FILE_L = "files/doodleJumpPlayerLeft.png";
    public static final String IMG_FILE_R = "files/doodleJumpPlayerRight.png";
    public static final int SIZE = 50;
    public static final int INIT_POS_X = 400;
    public static final int INIT_POS_Y = 750;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = -25;
    public static final int INIT_ACCEL_Y = 2;
    public static final int INIT_HP = 1;
    public static final int AFFECTVY = 0;

    private static BufferedImage imgR;
    private static BufferedImage imgL;

    public Player(int courtWidth, int courtHeight) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight,
                INIT_ACCEL_Y, INIT_HP, AFFECTVY
        );
        try {
            if (imgR == null) {
                imgR = ImageIO.read(new File(IMG_FILE_R));
            }
            if (imgL == null) {
                imgL = ImageIO.read(new File(IMG_FILE_L));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(
                vx, vy, px, py, SIZE, SIZE, courtWidth, courtHeight,
                INIT_ACCEL_Y, INIT_HP, AFFECTVY
        );
        try {
            if (imgR == null) {
                imgR = ImageIO.read((new File(IMG_FILE_R)));
            }
            if (imgL == null) {
                imgL = ImageIO.read((new File(IMG_FILE_L)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bullet shootBullet() {
        int px = this.getPx() + Player.SIZE / 2;
        int py = this.getPy();
        return new Bullet(px, py, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
    }

    @Override
    public boolean intersects(GameObj that) {
        if (that instanceof Platform) {
            int pad = Math.min(Math.max(this.getVy(), 5), 20);
            return (this.getVy() >= 0
                    && that.getPy() + that.getHeight() >= this.getPy() + this.getHeight() - pad
                    && this.getPx() + this.getWidth() >= that.getPx()
                    && this.getPy() + this.getHeight() >= that.getPy() - pad
                    && that.getPx() + that.getWidth() >= this.getPx());
        } else {
            return super.intersects(that);
        }
    }

    @Override
    public void interact(GameObj that) {
        if (this.intersects(that)) {
            if (that instanceof Platform && that.getClass() != WeakPlatform.class) {
                this.setVy(that.getAffectVy());
            } else if (that instanceof Monster) {
                this.setHp(0);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (this.getVx() >= 0) {
            g.drawImage(
                    imgR, this.getPx(), this.getPy(), this.getWidth(),
                    this.getHeight(), null
            );
        } else {
            g.drawImage(
                    imgL, this.getPx(), this.getPy(), this.getWidth(),
                    this.getHeight(), null
            );
        }
    }

    @Override
    public void move() {
        super.move();
        if (this.getPx() < 0) {
            super.setPx(this.getMaxX(), true);
        } else if (this.getPx() > this.getMaxX()) {
            super.setPx(0, true);
        }
    }
}
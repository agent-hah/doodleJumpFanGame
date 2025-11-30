package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Monster extends GameObj {
    public static final String IMG_FILE1 = "files/monster1.png";
    public static final String IMG_FILE2 = "files/monster2.png";
    public static final String IMG_FILE3 = "files/monster3.png";

    public static final int SIZE = 50;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = -25;
    public static final int INIT_ACCEL_Y = 2;
    public static final int INIT_ACCEL_X = 0;
    public static final int INIT_HP = 1;
    public static final int AFFECTVY = 0;

    private BufferedImage imgToDraw;

    private boolean isDead = false;

    public static BufferedImage img1;
    public static BufferedImage img2;
    public static BufferedImage img3;

    public Monster(int px, int py, int courtWidth, int courtHeight, int height, int width, int choice) {
        super(INIT_VEL_X, INIT_VEL_Y, px, py, width, height, courtWidth, courtHeight,
                INIT_ACCEL_X, INIT_ACCEL_Y, INIT_HP, AFFECTVY);
        try {
            if (img1 == null) {
                img1 = ImageIO.read(new File(IMG_FILE1));
            }
            if (img2 == null) {
                img2 = ImageIO.read(new File(IMG_FILE2));
            }
            if (img3 == null) {
                img3 = ImageIO.read(new File(IMG_FILE3));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (choice == 2) {
            imgToDraw = img2;
        } else {
            imgToDraw = img3;
        }
    }

    @Override
    public void interact(GameObj that) {
        if (this.willIntersect(that) | this.intersects(that)) {
            if (that.getClass() == Bullet.class) {
                this.setHp(this.getHp() - 1);
                if (this.getHp() <= 0) {
                    this.isDead = true;
                }
            }
        }
    }

    public boolean isDead () {
        return this.isDead;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getHeight(), this.getWidth(), null);
    }


    /**
     * This monster will stay in place and not move
     * @param px initial x position
     * @param py initial y position
     * @param courtWidth maximum x bound
     * @param courtHeight maximum y bound
     * @return the monster object
     */
    public static Monster getRegularMonster(int px, int py, int courtWidth, int courtHeight) {
        return new Monster(px, py, courtWidth, courtHeight, 50, 50, 1);
    }

    /**
     * This monster will move left to right
     * @param px
     * @param py
     * @param courtWidth
     * @param courtHeight
     * @return the monster object
     */
    public static Monster getMovingMonster(int px, int py, int courtWidth, int courtHeight) {
        Monster monster = new Monster(px, py, courtWidth, courtHeight, 100, 30, 2) {

            @Override
            public void move() {
                int speed = 4;
                if (this.getPx() == this.getMaxX()) {
                    this.setVx(-speed);
                }
                if (this.getPx() <= 0) {
                    this.setVx(speed);
                }
                super.move();
            }
        };
        monster.setHp(monster.getHp() + 1);
        return monster;
    }
}
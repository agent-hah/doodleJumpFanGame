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

    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int INIT_ACCEL_X = 0;
    public static final int INIT_HP = 1;
    public static final int AFFECTVY = 0;

    public static final int MONSTER_WIDTH = 50;
    public static final int MONSTER_HEIGHT = 50;

    private final BufferedImage imgToDraw;

    private boolean isDead = false;

    private static BufferedImage img1;
    private static BufferedImage img2;
    private static BufferedImage img3;

    public Monster(
            int px, int py, int courtWidth, int courtHeight, int height, int width, int choice
    ) {
        super(
                INIT_VEL_X, INIT_VEL_Y, px, py, width, height, courtWidth, courtHeight,
                INIT_ACCEL_X, INIT_ACCEL_Y, INIT_HP, AFFECTVY
        );
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
        if (choice == 1) {
            imgToDraw = img1;
        } else {
            imgToDraw = img3;
        }
    }

    public void setPx(int px) {
        super.setPx(px, true);
    }

    @Override
    public void setPx(int px, boolean wantClipping) {
        this.setPx(px);
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

    public boolean isDead() {
        return this.isDead;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getHeight(), this.getWidth(), null);
    }

    /**
     * This monster will stay in place and not move
     * 
     * @param px          initial x position
     * @param py          initial y position
     * @param courtWidth  maximum x bound
     * @param courtHeight maximum y bound
     * @return the monster object
     */
    public static Monster getRegularMonster(int px, int py, int courtWidth, int courtHeight) {
        return new Monster(px, py, courtWidth, courtHeight, MONSTER_HEIGHT, MONSTER_WIDTH, 1);
    }

    /**
     * This monster will move left to right
     * 
     * @param px          initial x position
     * @param py          initial y position
     * @param courtWidth  maximum x bound
     * @param courtHeight maximum y bound
     * @return the monster object
     */
    public static Monster getMovingMonster(int px, int py, int courtWidth, int courtHeight) {
        Monster monster = new Monster(
                px, py, courtWidth, courtHeight, MONSTER_HEIGHT, MONSTER_WIDTH, 3
        ) {

            private static final int speed = 4;

            @Override
            public void move() {

                if (this.getPx() >= this.getMaxX()) {
                    this.setVx(-speed);
                } else if (this.getPx() <= 0) {
                    this.setVx(speed);
                }

                super.move();
            }
        };

        monster.setHp(monster.getHp() + 1);
        monster.setVx(4);
        return monster;
    }
}
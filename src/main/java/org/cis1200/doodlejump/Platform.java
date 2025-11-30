package org.cis1200.doodlejump;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Platform extends GameObj {
    public static final String IMG_FILE = "files/platform.png";
    public static final String IMG_FILE_BOUNCY = "files/bouncyPlatform.png";
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_X = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int HP = 1;
    public static final int AFFECTVY = -25;
    public static final int BOUNCYAFFECTVY = -40;

    private static BufferedImage img;
    private static BufferedImage imgBouncy;

    public Platform(int px, int py, int courtWidth, int courtHeight, int choose) {
        super(
                INIT_VEL_X, INIT_VEL_Y, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_X, INIT_ACCEL_Y, HP, AFFECTVY
        );
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBouncy == null) {
                imgBouncy = ImageIO.read(new File(IMG_FILE_BOUNCY));
            }
        } catch (IOException e) {
            // TODO: create a page that will be pushed toward the game
        }
        if (choose == 1) {
            this.setAffectVy(BOUNCYAFFECTVY);
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
        return;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage imgToDraw = img;
        if (this.getAffectVy() == BOUNCYAFFECTVY) {
            imgToDraw = imgBouncy;
        }
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        g.setColor(Color.RED);
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    public static Platform getRegPlatform(int px, int py, int courtWidth, int courtHeight) {
        return new Platform(px, py, courtWidth, courtHeight, 0);
    }

    public static Platform getBouncyPlatform(int px, int py, int courtWidth, int courtHeight) {
        return new Platform(px, py, courtWidth, courtHeight, 1);
    }
}
package org.cis1200.doodlejump;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Platform extends GameObj {
    public static final String IMG_FILE = "files/platform.png";
    public static final int WIDTH = 200;
    public static final int HEIGHT = 100;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_X = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int HP = 1;
    public static final int AFFECTVY = -10;

    public static BufferedImage img;

    public Platform(int px, int py, int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_X, INIT_ACCEL_Y, HP, AFFECTVY);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            // TODO: create a page that will be pushed toward the game
        }
    }

    @Override
    public void interact(GameObj that) {
        return ;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
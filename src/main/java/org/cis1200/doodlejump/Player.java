package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends GameObj{
    public static final String IMG_FILE_L = "files/doodleJumpPlayerLeft.png";
    public static final String IMG_FILE_R = "files/doodleJumpPlayerRight.png";
    public static final int SIZE = 50;
    public static final int INIT_POS_X = 250;
    public static final int INIT_POS_Y = 950;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = -25;
    public static final int INIT_ACCEL_Y = 2;
    public static final int INIT_ACCEL_X = 0;
    public static final int INIT_HP = 1;
    public static final int AFFECTVY = 0;

    private static BufferedImage img_r;
    private static BufferedImage img_l;

    public Player(int courtWidth, int courtHeight) {
        super(INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight,
                INIT_ACCEL_X, INIT_ACCEL_Y, INIT_HP, AFFECTVY);
        try {
            if (img_r == null) {
                img_r = ImageIO.read(new File(IMG_FILE_R));
            }
            if (img_l == null) {
                img_l = ImageIO.read(new File(IMG_FILE_L));
            }
        } catch (IOException e) {
            System.err.println("Error loading image");
            // TODO: create a page that will be pushed toward the game
        }
    }

    @Override
    public boolean intersects(GameObj that) {
        if (that.getClass() == Platform.class) {
            return this.getVy() >= 0 && super.intersects(that);
        } else { return super.intersects(that); }
    }

    @Override
    public boolean willIntersect(GameObj that) {
        int thisNextVy = this.getVy() + this.getVx();
        if (that.getClass() == Platform.class) {
            return thisNextVy >= 0 && super.willIntersect(that);
        } else { return super.willIntersect(that); }
    }


    @Override
    public void interact(GameObj that) {
        if (this.willIntersect(that)) {
            if (that.getClass() == Platform.class) {
                this.setVy(that.getAffectVy());
            }
            // TODO: add interaction for monster class
        }
    }

    @Override
    public void draw(Graphics g) {
        if (this.getVx() >= 0) {
            g.drawImage(img_r, this.getPx(), this.getPy(), this.getWidth(),
                    this.getHeight(), null);
        }
        else {
            g.drawImage(img_l, this.getPx(), this.getPy(), this.getWidth(),
                    this.getHeight(), null);
        }
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}

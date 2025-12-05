package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The Doodle Jumper himself! Can shoot bullets, move left and right (depending
 * on input),
 * and will auto jump.
 */
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
    public static final int AFFECT_VY = 0;

    private static BufferedImage imgR;
    private static BufferedImage imgL;

    /**
     * Method for loading the images for the object and dynamically updates the
     * image to be drawn
     * depending on whether the player is moving to the left or to the right
     */
    private void loadImage() {
        if (imgR == null) {
            try {
                imgR = ImageIO.read(new File(IMG_FILE_R));
            } catch (IOException e) {
                imgR = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_3BYTE_BGR);
            }
        }
        if (imgL == null) {
            try {
                imgL = ImageIO.read(new File(IMG_FILE_L));
            } catch (IOException e) {
                imgL = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_3BYTE_BGR);
            }
        }
        if (getVx() >= 0) {
            setImgToDraw(imgR);
        } else {
            setImgToDraw(imgL);
        }
    }

    /**
     * Constructor for creating a new player
     * 
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public Player(int courtWidth, int courtHeight) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight,
                INIT_ACCEL_Y, INIT_HP, AFFECT_VY
        );
        loadImage();
    }

    /**
     * Constructor for loading a player from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public Player(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(
                vx, vy, px, py, SIZE, SIZE, courtWidth, courtHeight,
                INIT_ACCEL_Y, INIT_HP, AFFECT_VY
        );
        loadImage();
    }

    /**
     * Method for the player shooting a bullet out!
     * 
     * @return the bullet object
     */
    public Bullet shootBullet() {
        int px = this.getPx() + Player.SIZE / 2 - Bullet.SIZE / 2;
        int py = this.getPy();
        return new Bullet(px, py, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
    }

    /**
     * The intersects method is overridden so that it gets padding to prevent
     * tunneling
     * (with platforms). Also returns true for platforms if the platform is below a
     * certain point
     * relative to the player (depends on the padding).
     * 
     * @param that The other object
     * @return true if the objects intersect; false otherwise
     */
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

    /**
     * The player only interacts with platforms and monsters. Gets the affect
     * velocity from
     * platforms, and gets killed by monsters.
     * 
     * @param that the other object
     */
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

    /**
     * Method for drawing the player onto the screen
     * 
     * @param g the graphics context
     */
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

    /**
     * Overrides the move method to make the game have a wrap-around field! If the
     * player goes
     * off the left, they will appear from the right (and vice versa).
     */
    @Override
    public void move() {
        super.move();
        if (this.getPx() < 0) {
            super.setPx(this.getMaxX());
        } else if (this.getPx() > this.getMaxX()) {
            super.setPx(0);
        }
    }
}
package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bullet extends GameObj {
    private boolean hitTarget = false;
    private boolean outOfBounds = false;

    public static final int SIZE = 20;
    public static final int AFFECTVY = 0;
    public static final int SPEED = -40;
    public static final int ACCEL = 0;
    public static final int HP = 1;
    public static final String IMG_FILE = "files/shuriken.png";

    private static BufferedImage img;

    /**
     * Constructor for the object
     *
     * @param px          initial x position
     * @param py          initial y position
     * @param courtWidth  max X bound
     * @param courtHeight max Y bound
     *
     */
    public Bullet(
            int px, int py, int courtWidth, int courtHeight
    ) {
        super(0, SPEED, px, py, SIZE, SIZE, courtWidth, courtHeight, ACCEL, ACCEL, HP, AFFECTVY);

        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPx(int px) {
        super.setPx(px, false);
    }

    @Override
    public void setPx(int px, boolean wantClipping) {
        this.setPx(px);
    }

    public boolean isHitTarget() {
        return this.hitTarget;
    }

    public boolean isOutOfBounds() {
        return this.outOfBounds;
    }

    @Override
    public void move() {
        this.setPx(this.getPx() + this.getVx());
        this.setPy(this.getPy() + this.getVy());

        if (this.getPx() <= 0 | this.getPx() >= this.getMaxX()) {
            this.outOfBounds = true;
        } else if (this.getPx() <= 0 | this.getPx() >= this.getMaxY()) {
            this.outOfBounds = true;
        }
    }

    @Override
    public void interact(GameObj that) {
        if (willIntersect(that) | intersects(that)) {
            if (that instanceof Monster) {
                this.hitTarget = true;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), SIZE, SIZE, null);
    }
}

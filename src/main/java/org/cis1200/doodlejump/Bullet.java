package org.cis1200.doodlejump;

import java.awt.*;

public class Bullet extends GameObj {
    boolean hitTarget = false;

    /**
     * Constructor for the object
     *
     * @param vx
     * @param vy
     * @param px
     * @param py
     * @param width
     * @param height
     * @param courtWidth
     * @param courtHeight
     * @param ax
     * @param ay
     * @param hp
     * @param affectVy
     */
    public Bullet(int vx, int vy, int px, int py, int width, int height, int courtWidth, int courtHeight, int ax, int ay, int hp, int affectVy) {
        super(vx, vy, px, py, width, height, courtWidth, courtHeight, ax, ay, hp, affectVy);
    }

    @Override
    public void interact(GameObj that) {

    }

    @Override
    public void draw(Graphics g) {

    }

    public boolean getHitTarget() {
        return this.hitTarget;
    }
}

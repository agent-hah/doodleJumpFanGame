package org.cis1200.doodlejump;

import javax.swing.*;
import java.awt.*;

public abstract class GameObj {
    /*
     * For our object, we want to have health, an x position, a y position,
     * velocity, acceleration,
     * size, bounds, intersection
     *
     */
    private int px;
    private int py;

    private final int width;
    private final int height;

    private int vx;
    private int vy;

    private int ax;
    private int ay;

    private int hp;

    private int affectVy;

    private final int maxX;
    private final int maxY;

    /** Constructor for the object */
    public GameObj(
            int vx, int vy, int px, int py, int width, int height, int courtWidth, int courtHeight,
            int ax, int ay, int hp, int affectVy
    ) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
        this.ax = ax;
        this.ay = ay;
        this.hp = hp;
        this.affectVy = affectVy;

        this.setPx(px, true);
    }

    // Getters
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getVx() {
        return this.vx;
    }

    public int getVy() {
        return this.vy;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getHp() {
        return this.hp;
    }

    public int getAffectVy() {
        return this.affectVy;
    }

    public int getAy() {
        return this.ay;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMaxY() {
        return this.maxY;
    }

    // Setters
    public void setPx(int px, boolean wantClipping) {
        this.px = px;
        if (wantClipping) {
            clip();
        }
    }

    public void setPy(int py) {
        this.py = py;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setAffectVy(int affectVy) {
        this.affectVy = affectVy;
    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // Updates and other methods
    /** Makes sure the object is in bounds */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
    }

    /** accelerates the object */
    private void accel() {
        this.vx += this.ax;
        this.vy += this.ay;
    }

    public void move() {
        this.px += this.vx;
        this.py += this.vy;

        accel();
    }

    /**
     * determines whether this game object is intersecting another object
     * 
     * @param that The other object
     * @return whether the object intersects the other object or not
     */
    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px
                && this.py + this.height >= that.py
                && that.px + that.width >= this.px
                && that.py + that.height >= this.py);
    }

    @Override
    public String toString() {
        return this.getPx() + "," + this.getPy() + "," + this.getVx() + "," + this.getVy();
    }

    /**
     * checks if the object will return one timestep later
     * 
     * @param that The other object
     * @return Whether an intersection will occur
     *
     */
    public boolean willIntersect(GameObj that) {
        int thisNextX = this.px + this.vx;
        int thisNextY = this.py + this.vy;
        int thatNextX = that.px + that.vx;
        int thatNextY = that.py + that.vy;

        return (thisNextX + this.width >= thatNextX
                && thisNextY + this.height >= thatNextY
                && thatNextX + that.width >= thisNextX
                && thatNextY + that.height >= thisNextY);
    }

    /**
     * An interaction with the other object, whatever that will be
     * 
     * @param that the other object
     */
    public abstract void interact(GameObj that);

    /**
     * the draw method that determines how the object will be drawn in the gui
     */
    public abstract void draw(Graphics g);
}

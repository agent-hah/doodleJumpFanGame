package org.cis1200.doodlejump;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The generic representation of an object that will be in the game
 */
public abstract class GameObj {
    /**
     * For our object, we want to have health, position,
     * velocity, acceleration,
     * size, and bounds
     */
    private int px;
    private int py;

    private final int width;
    private final int height;

    private int vx;
    private int vy;

    private int ay;

    private int hp;

    private final int affectVy;

    private final int maxX;
    private final int maxY;

    private BufferedImage imgToDraw;

    /**
     * Constructor for the GameObj
     * 
     * @param vx          the initial x velocity
     * @param vy          the initial y velocity
     * @param px          the initial x position
     * @param py          the initial y position
     * @param width       the width of the object
     * @param height      the height of the object
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param ay          the y acceleration
     * @param hp          the health of the object
     * @param affectVy    the y velocity the object imparts on others
     */
    public GameObj(
            int vx, int vy, int px, int py, int width, int height, int courtWidth, int courtHeight,
            int ay, int hp, int affectVy
    ) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
        this.ay = ay;
        this.hp = hp;
        this.affectVy = affectVy;
    }

    // Getters

    /**
     * @return x position
     */
    public int getPx() {
        return this.px;
    }

    /**
     * @return y position
     */
    public int getPy() {
        return this.py;
    }

    /**
     * @return x velocity
     */
    public int getVx() {
        return this.vx;
    }

    /**
     * @return the y velocity
     */
    public int getVy() {
        return this.vy;
    }

    /**
     * @return the width of the object
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * @return the height of the object
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return the health of the object
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * @return the y velocity the object imparts on others
     */
    public int getAffectVy() {
        return this.affectVy;
    }

    /**
     * @return the maximum x position the object can have (and be in bounds)
     */
    public int getMaxX() {
        return this.maxX;
    }

    /**
     * @return the maximum y position the object can have (and be in bounds)
     */
    public int getMaxY() {
        return this.maxY;
    }

    /**
     * @return the image to be drawn by the graphics context
     */
    public BufferedImage getImgToDraw() {
        return this.imgToDraw;
    }

    // Setters

    /**
     * @param px the x position
     */
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    /**
     * @param py the y position
     */
    public void setPy(int py) {
        this.py = py;
    }

    /**
     * @param vx the x velocity
     */
    public void setVx(int vx) {
        this.vx = vx;
    }

    /**
     * @param vy the y velocity
     */
    public void setVy(int vy) {
        this.vy = vy;
    }

    /**
     * @param ay the y acceleration
     */
    public void setAy(int ay) {
        this.ay = ay;
    }

    /**
     * @param hp the health
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * @param imgToDraw a bufferedImage that will be drawn onto the screen
     */
    public void setImgToDraw(BufferedImage imgToDraw) {
        this.imgToDraw = imgToDraw;
    }

    // Updates and other methods
    /** Makes sure the object is in bounds */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
    }

    /** accelerates the object */
    private void accel() {
        this.vy += this.ay;
    }

    /**
     * gets the object to move depending on the velocity (accelerates the object
     * too)
     */
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

    /**
     * The generic information required to save a game obj (will be overridden to
     * provide type
     * specific information)
     * 
     * @return the string representation
     */
    @Override
    public String toString() {
        return this.getPx() + "," + this.getPy() + "," + this.getVx() + "," + this.getVy();
    }

    /**
     * An interaction with the other object, whatever that will be
     * 
     * @param that the other object
     */
    public abstract void interact(GameObj that);

    /**
     * the draw method that determines how the object will be drawn onto the screen
     * 
     * @param g the graphics context
     */
    public abstract void draw(Graphics g);
}

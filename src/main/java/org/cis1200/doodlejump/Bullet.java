package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Bullet (shuriken) that the player shoots that can kill monsters
 */
public class Bullet extends GameObj {
    private boolean hitTarget = false;
    private boolean outOfBounds = false;

    public static final int SIZE = 20;
    public static final int AFFECT_VY = 0;
    public static final int SPEED = -40;
    public static final int ACCEL = 0;
    public static final int HP = 1;
    public static final String IMG_FILE = "files/shuriken.png";

    private static BufferedImage img;

    /**
     * Method for loading the bullet image, and sets it as the image to be drawn.
     */
    public void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_USHORT_GRAY);
            }
        }
        setImgToDraw(img);
    }

    /**
     * Constructor creating a new bullet
     * 
     * @param px          initial x position
     * @param py          initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     *
     */
    public Bullet(int px, int py, int courtWidth, int courtHeight) {
        super(0, SPEED, px, py, SIZE, SIZE, courtWidth, courtHeight, ACCEL, HP, AFFECT_VY);
        loadImage();
    }

    /**
     * Constructor for loading a bullet from a save file
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public Bullet(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(vx, vy, px, py, SIZE, SIZE, courtWidth, courtHeight, ACCEL, HP, AFFECT_VY);
        loadImage();
    }

    /**
     * Method for checking whether the bullet is outside the boundary of the game,
     * and updates
     * the internal state
     */
    private void checkBounds() {
        this.outOfBounds = this.getPy() <= -20 | this.getPy() >= this.getMaxY() + 5;
    }

    /**
     * Method for showing whether the bullet has it a monster or not
     * 
     * @return true if it has hit a monster; false otherwise
     */
    public boolean isHitTarget() {
        return this.hitTarget;
    }

    /**
     * Method for showing whether the bullet is outside the game area or not
     * 
     * @return true if it is outside the game area; false otherwise
     */
    public boolean isOutOfBounds() {
        return this.outOfBounds;
    }

    /**
     * Overrides the move method so that the object internally checks whether it is
     * out
     * of bounds or not
     */
    @Override
    public void move() {
        super.move();
        checkBounds();
    }

    /**
     * Bullet only interacts with monsters, and updates itself so that it returns
     * true for hitting
     * a monster (if it has)
     * 
     * @param that the other object
     */
    @Override
    public void interact(GameObj that) {
        if (intersects(that)) {
            if (that instanceof Monster) {
                this.hitTarget = true;
            }
        }
    }

    /**
     * Method for drawing the bullet on the screen
     * 
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(
                getImgToDraw(), this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),
                null
        );
    }
}

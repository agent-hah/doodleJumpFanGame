package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The generic monster that will be in Doodle Jump. It is an abstract class.
 */
public abstract class Monster extends GameObj {

    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int AFFECT_VY = 0;

    private boolean isDead = false;

    /**
     * The generic constructor for creating a new monster (can't be used)
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param height      the height of the monster
     * @param width       the width of the monster
     * @param hp          the health of the monster
     */
    public Monster(
            int px, int py, int courtWidth, int courtHeight, int height, int width, int hp
    ) {
        super(
                INIT_VEL_X, INIT_VEL_Y, px, py, width, height, courtWidth, courtHeight,
                INIT_ACCEL_Y, hp, AFFECT_VY
        );
    }

    /**
     * The generic constructor for loading a monster from a savefile (can't be used)
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param height      the height of the monster
     * @param width       the width of the monster
     * @param hp          the loaded health
     */
    public Monster(
            int px, int py, int vx, int vy, int courtWidth, int courtHeight, int height, int width,
            int hp
    ) {
        super(
                vx, vy, px, py, width, height, courtWidth, courtHeight,
                INIT_ACCEL_Y, hp, AFFECT_VY
        );
    }

    /**
     * The monster's specific interactions with other game objects:
     * <p>
     * - It loses 1 hp when it interacts with a bullet (and updates the monster's
     * state if
     * it dies)
     * </p>
     * - Does nothing if it collides with other objects
     * 
     * @param that the other object
     */
    @Override
    public void interact(GameObj that) {
        if (this.intersects(that)) {
            if (that.getClass() == Bullet.class) {
                this.setHp(this.getHp() - 1);
                if (this.getHp() <= 0) {
                    this.isDead = true;
                }
            }
        }
    }

    /**
     * For checking if the monster is dead or not
     * 
     * @return true if the monster is dead, false if the monster isn't
     */
    public boolean isDead() {
        return this.isDead;
    }

    /**
     * Method for drawing the monster onto the screen
     * 
     * @param g graphics context
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(
                getImgToDraw(), this.getPx(), this.getPy(), this.getWidth(), this.getHeight(),
                null
        );
    }

    /**
     * A method for getting the information of the monster, which will be put in a
     * savefile.
     * <p>
     * </p>
     * This method is overridden here to provide generic health information.
     * <p>
     * The instances of monster override this monster to provide type-specific
     * information.
     * 
     * @return the generic string representation of a monster
     */
    @Override
    public String toString() {
        return super.toString() + "," + this.getHp();
    }
}

/**
 * This is the most basic monster that doesn't move at all, and has only 1 hp.
 * Nevertheless,
 * it will kill the player if the player collides with it.
 */
class RegularMonster extends Monster {

    private static BufferedImage img;
    public static final String IMG_FILE = "files/monster1.png";
    private static final int INIT_HP = 1;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Loads the image for the regular monster, and sets the image of the monster to
     * be the loaded
     * image
     */
    private void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            }
        }
        super.setImgToDraw(img);
    }

    /**
     * Constructor for creating a regular monster outside a savefile
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public RegularMonster(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, HEIGHT, WIDTH, INIT_HP);
        loadImage();
    }

    /**
     * Constructor for loading a regular monster from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded horizontal velocity
     * @param vy          the loaded vertical velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param hp          the loaded health (I know its always going to be 1 but
     *                    every other monster takes
     *                    in the health input)
     */
    public RegularMonster(int px, int py, int vx, int vy, int courtWidth, int courtHeight, int hp) {
        super(px, py, vx, vy, courtWidth, courtHeight, HEIGHT, WIDTH, hp);
        loadImage();
    }

    /**
     * Method for getting the information of the regular monster for the savefile
     * 
     * @return The string representation of the object to be put in a savefile
     */
    @Override
    public String toString() {
        return "0," + super.toString();
    }
}

/**
 * This monster will move left to right across the entire game area. This
 * monster has 2 health.
 */
class MovingMonster extends Monster {

    private static final int SPEED = 4;
    private static final int INIT_HP = 2;
    private static BufferedImage img;
    private static final String IMG_FILE = "files/monster2.png";
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;

    /**
     * Loads the image for the moving monster, and sets the image of the monster to
     * be the loaded
     * image
     */
    private void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            }
        }
        super.setImgToDraw(img);
    }

    /**
     * Constructor for creating a moving monster outside a savefile
     * 
     * @param px          initial x position
     * @param py          initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public MovingMonster(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, HEIGHT, WIDTH, INIT_HP);

        RandomGenerator rng = new RandomGenerator();
        if (rng.nextBoolean()) {
            this.setVx(SPEED);
        } else {
            this.setVx(-SPEED);
        }
        loadImage();
    }

    /**
     * Constructor for loading a moving monster from a savefile
     * 
     * @param px          loaded x position
     * @param py          loaded y position
     * @param vx          loaded x velocity
     * @param vy          loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param hp          the loaded health
     */
    public MovingMonster(int px, int py, int vx, int vy, int courtWidth, int courtHeight, int hp) {
        super(px, py, vx, vy, courtWidth, courtHeight, HEIGHT, WIDTH, hp);
        loadImage();
    }

    /**
     * The move method for the moving monster allows it to move back and forth.
     */
    @Override
    public void move() {

        if (this.getPx() >= this.getMaxX()) {
            this.setVx(-SPEED);
        } else if (this.getPx() <= 0) {
            this.setVx(SPEED);
        }

        super.move();
    }

    /**
     * Method for getting the information of the moving monster for the savefile
     * 
     * @return The string representation of the object to be put in a savefile
     */
    @Override
    public String toString() {
        return "1," + super.toString();
    }
}

/**
 * This monster will chase the player wherever they are. It has 2 hp.
 */
class HomingMonster extends Monster {

    private static final int SPEED = 3;
    private static final int INIT_HP = 2;

    private static Player player;
    private static BufferedImage img;
    private static final String IMG_FILE = "files/monster3.png";
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * method for the HomingMonster to load its image. Sets the loaded image as the
     * image
     * to draw
     */
    private void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            }
        }
        super.setImgToDraw(img);
    }

    /**
     * Constructor for creating a new Homing Monster
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param player      the player (that will be chased after!)
     */
    public HomingMonster(int px, int py, int courtWidth, int courtHeight, Player player) {
        super(px, py, courtWidth, courtHeight, HEIGHT, WIDTH, INIT_HP);

        if (player != null && !player.equals(HomingMonster.player)) {
            HomingMonster.player = player;
        }
        loadImage();
    }

    /**
     * Constructor for loading a Homing Monster from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param hp          the loaded health
     * @param player      the player (that will be chased after!)
     */
    public HomingMonster(
            int px, int py, int vx, int vy, int courtWidth, int courtHeight, int hp, Player player
    ) {
        super(px, py, vx, vy, courtWidth, courtHeight, HEIGHT, WIDTH, hp);

        if (player != null && !player.equals(HomingMonster.player)) {
            HomingMonster.player = player;
        }
        loadImage();
    }

    /**
     * The move method that enables the monster to chase after the player
     */
    @Override
    public void move() {
        if (this.getPx() >= player.getPx()) {
            this.setVx(-SPEED);
        } else {
            this.setVx(SPEED);
        }
        if (this.getPy() >= player.getPy()) {
            this.setVy(-SPEED);
        } else {
            this.setVy(SPEED);
        }

        super.move();
    }

    /**
     * Method for getting the information of the homing monster for the savefile
     * 
     * @return The string representation of the object to be put in a savefile
     */
    @Override
    public String toString() {
        return "2," + super.toString();
    }
}
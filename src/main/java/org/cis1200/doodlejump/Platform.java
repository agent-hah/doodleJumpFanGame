package org.cis1200.doodlejump;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The generic platform that will be in Doodle Jump. It is an abstract class.
 */
public abstract class Platform extends GameObj {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int HP = 1;

    /**
     * Generic constructor for creating a platform.
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param affectVy    the y velocity the platform imparts on objects
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public Platform(int px, int py, int affectVy, int courtWidth, int courtHeight) {
        super(
                INIT_VEL_X, INIT_VEL_Y, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_Y, HP, affectVy
        );
    }

    /**
     * Generic constructor for loading a platform from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param affectVy    the y velocity the platform imparts on objects
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public Platform(int px, int py, int vx, int vy, int affectVy, int courtWidth, int courtHeight) {
        super(
                vx, vy, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_Y, HP, affectVy
        );
    }

    /**
     * Method for checking whether the platform intersects with another GameObj
     * <p>
     * </p>
     * Overrides the GameObj class to give a pad for the player, to prevent
     * tunneling. Also
     * returns true depending on whether the platform is below a certain point
     * relative to
     * the player (depends on the padding).
     * 
     * @param that The other object
     * @return whether the objects intersect or not
     */
    @Override
    public boolean intersects(GameObj that) {
        if (that instanceof Player) {
            int pad = Math.min(Math.max(that.getVy(), 5), 20);
            return (that.getVy() >= 0
                    && this.getPy() + this.getHeight() >= that.getPy() + that.getHeight() - pad
                    && that.getPx() + that.getWidth() >= this.getPx()
                    && that.getPy() + that.getHeight() >= this.getPy() - pad
                    && this.getPx() + this.getWidth() >= that.getPx());
        } else {
            return super.intersects(that);
        }
    }

    /**
     * The generic platform doesn't interact with anything because it's supposed to
     * be an obstacle
     * </p>
     * certain subtypes of platform change this
     * 
     * @param that the other object
     */
    @Override
    public void interact(GameObj that) {
    }

    /**
     * Method for drawing the generic platform onto the screen
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

    /**
     * Method for determining whether the object should be deleted or not.
     * <p>
     * </p>
     * default is false; but subtypes can override this
     * 
     * @return false
     */
    public boolean shouldDelete() {
        return false;
    }
}

/**
 * A Regular Platform that doesn't move and gives the player a basic velocity
 * upwards
 */
class RegularPlatform extends Platform {
    public static final String IMG_FILE = "files/platform.png";
    private static BufferedImage img;
    public static final int AFFECT_VY = -25;

    /**
     * Method for loading the image of the regular platform and setting it as the
     * image to be
     * drawn
     */
    private void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            }
        }
        super.setImgToDraw(img);
    }

    /**
     * Constructor for creating a new regular platform
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public RegularPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Constructor for loading a regular platform from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public RegularPlatform(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(px, py, vx, vy, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Method for getting the information needed to save the regular platform
     * 
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "0," + super.toString();
    }
}

/**
 * Like the regular platform, but gives the player a higher velocity upwards
 */
class BouncyPlatform extends Platform {
    public static final String IMG_FILE = "files/bouncyPlatform.png";
    public static final int AFFECT_VY = -40;
    private static BufferedImage img;

    /**
     * Method for loading the image of the bouncy platform and setting it as the
     * image to be drawn
     */
    private void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            }
        }
        super.setImgToDraw(img);
    }

    /**
     * Constructor for creating a new bouncy platform
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public BouncyPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Constructor for loading a bouncy platform from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public BouncyPlatform(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(px, py, vx, vy, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Gives the information required to save the bouncy platform to the savefile
     * 
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "1," + super.toString();
    }
}

/**
 * A platform that doesn't give the player velocity upwards. Instead, it breaks
 * and half, making
 * the player fall through.
 */
class WeakPlatform extends Platform {
    public static final String IMG_FILE = "files/weakPlatform.png";
    public static final String IMG_FILE_BROKEN = "files/weakPlatformBroken.png";
    public static final int AFFECT_VY = 0;

    private static BufferedImage img;
    private static BufferedImage imgBroken;

    private int state = 0;

    /**
     * Method for loading the images for the weak platform. It also updates the
     * image to be drawn
     * depending on the internal state of the platform
     */
    private void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            }
        }
        if (imgBroken == null) {
            try {
                imgBroken = ImageIO.read(new File(IMG_FILE_BROKEN));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
            }
        }

        if (state == 1) {
            super.setImgToDraw(imgBroken);
        } else {
            super.setImgToDraw(img);
        }
    }

    /**
     * Constructor for creating a new weak platform
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public WeakPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Constructor for loading a weak platform from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param state       the loaded state of the platform
     */
    public WeakPlatform(
            int px, int py, int vx, int vy, int courtWidth, int courtHeight, int state
    ) {
        super(px, py, vx, vy, AFFECT_VY, courtWidth, courtHeight);
        this.state = Math.min(Math.max(state, 0), 1);
        loadImage();
    }

    /**
     * Overrides the interact method so that the platform updates its state (and
     * image) when it
     * interacts with the player.
     * 
     * @param that the other object
     */
    @Override
    public void interact(GameObj that) {
        if (this.intersects(that)) {
            if (that.getClass() == Player.class) {
                this.state = 1;
                loadImage();
            }
        }
    }

    /**
     * Gives the information required for saving the weak platform to the savefile
     * 
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "2," + super.toString() + "," + this.state;
    }
}

/**
 * A platform that disappears after a certain amount of time. The image updates
 * to give you a hint
 * of how much time is left.
 */
class DisappearingPlatform extends Platform {

    private static final int TICK_STEP = 25;
    public static final String IMG_FILE_1 = "files/disappearingPlatformTick1.png";
    public static final String IMG_FILE_2 = "files/disappearingPlatformTick2.png";
    public static final String IMG_FILE_3 = "files/disappearingPlatformTick3.png";

    private static final int AFFECT_VY = -30;

    private static BufferedImage img1;
    private static BufferedImage img2;
    private static BufferedImage img3;

    private int state = 0;

    /**
     * Method for loading the images of the disappearing platform.
     * <p>
     * </p>
     * Dynamically updates the image to be loaded depending on the internal state
     * pf the object.
     */
    private void loadImage() {
        if (img1 == null) {
            try {
                img1 = ImageIO.read(new File(IMG_FILE_1));
            } catch (IOException e) {
                img1 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_BYTE_BINARY);
            }
        }
        if (img2 == null) {
            try {
                img2 = ImageIO.read(new File(IMG_FILE_2));
            } catch (IOException e) {
                img2 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_USHORT_555_RGB);
            }
        }
        if (img3 == null) {
            try {
                img3 = ImageIO.read(new File(IMG_FILE_3));
            } catch (IOException e) {
                img3 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_USHORT_565_RGB);
            }
        }

        if (state <= TICK_STEP) {
            super.setImgToDraw(img1);
        } else if (state <= TICK_STEP * 2) {
            setImgToDraw(img2);
        } else {
            setImgToDraw(img3);
        }
    }

    /**
     * Constructor for creating a new disappearing platform
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public DisappearingPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Constructor for loading a disappearing platform from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     * @param state       the loaded state of the platform
     */
    public DisappearingPlatform(
            int px, int py, int vx, int vy, int courtWidth, int courtHeight, int state
    ) {
        super(px, py, vx, vy, AFFECT_VY, courtWidth, courtHeight);
        this.state = Math.min(Math.max(0, state), TICK_STEP * 4 + 5);
        loadImage();
    }

    /**
     * Tells the tick method whether the object is on the screen or not. This
     * prevents the state
     * from ticking when it's not on screen.
     * 
     * @return true if the platform is on screen; false otherwise
     */
    private boolean inBounds() {
        return this.getPy() >= 0 && this.getPy() <= this.getMaxY();
    }

    /**
     * Method that increments the internal state of the platform (its timer).
     * Updates
     * the image accordingly depending on the state
     */
    private void tick() {
        if (inBounds()) {
            state++;
        }
        loadImage();
    }

    /**
     * Overrides the move method so that the platform's internal state ticks as it
     * moves
     */
    @Override
    public void move() {
        super.move();
        tick();
    }

    /**
     * Method for telling the game region whether to delete the platform or not.
     * <p>
     * </p>
     * Overrides the generic method because the object should be deleted if the
     * state says
     * so.
     * 
     * @return true if the state of the platform is above the threshold; false
     *         otherwise
     */
    @Override
    public boolean shouldDelete() {
        return (state > TICK_STEP * 3);
    }

    /**
     * Gives the information required to save the disappearing platform to the
     * savefile
     * 
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "3," + super.toString() + "," + state;
    }
}

/**
 * Platform that moves left and right across the screen.
 */
class MovingPlatform extends Platform {
    public static final String IMG_FILE_MOVING = "files/movingPlatform.png";

    private static final int SPEED = 4;
    private static final int AFFECT_VY = -35;

    private static BufferedImage img;

    /**
     * Method for loading the image of the platform, and setting it as the image to
     * be drawn.
     */
    public void loadImage() {
        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE_MOVING));
            } catch (IOException e) {
                img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            }
        }
        setImgToDraw(img);
    }

    /**
     * Constructor for creating a new moving platform
     * <p>
     * </p>
     * Will randomly determine the starting direction
     * 
     * @param px          the initial x position
     * @param py          the initial y position
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public MovingPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, AFFECT_VY, courtWidth, courtHeight);

        RandomGenerator rng = new RandomGenerator();
        if (rng.nextBoolean()) {
            this.setVx(SPEED);
        } else {
            this.setVx(-SPEED);
        }

        loadImage();
    }

    /**
     * Constructor for loading a moving platform from a savefile
     * 
     * @param px          the loaded x position
     * @param py          the loaded y position
     * @param vx          the loaded x velocity
     * @param vy          the loaded y velocity
     * @param courtWidth  the width of the game area
     * @param courtHeight the height of the game area
     */
    public MovingPlatform(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(px, py, vx, vy, AFFECT_VY, courtWidth, courtHeight);
        loadImage();
    }

    /**
     * Overrides the generic move method so that it can move left and right across
     * the width
     * of the screen
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
     * Gives the information required to save the moving platform to a savefile
     * 
     * @return the string representation of the object
     */
    @Override
    public String toString() {
        return "4," + super.toString();
    }
}
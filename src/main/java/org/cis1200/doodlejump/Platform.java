package org.cis1200.doodlejump;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public abstract class Platform extends GameObj {
    public static final String IMG_FILE = "files/platform.png";
    public static final String IMG_FILE_BOUNCY = "files/bouncyPlatform.png";
    public static final String IMG_FILE_MOVING = "files/movingPlatform.png";
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int HP = 1;
    public static final int AFFECTVY = -25;
    public static final int BOUNCYAFFECTVY = -40;

    private static BufferedImage img;
    private static BufferedImage imgBouncy;
    private static BufferedImage imgMoving;

    private final BufferedImage imgToDraw;

    public Platform(int px, int py, int courtWidth, int courtHeight, int type) {
        super(
                INIT_VEL_X, INIT_VEL_Y, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_Y, HP, AFFECTVY
        );
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBouncy == null) {
                imgBouncy = ImageIO.read(new File(IMG_FILE_BOUNCY));
            }
            if (imgMoving == null) {
                imgMoving = ImageIO.read(new File(IMG_FILE_MOVING));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgToDraw = switch (type) {
            case 1 -> imgBouncy;
            case 4 -> imgMoving;
            default -> img;
        };

        if (type == 1) {
            this.setAffectVy(BOUNCYAFFECTVY);
        }
    }

    public Platform(int px, int py, int vx, int vy, int courtWidth, int courtHeight, int type) {
        super(
                vx, vy, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_Y, HP, AFFECTVY
        );
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBouncy == null) {
                imgBouncy = ImageIO.read(new File(IMG_FILE_BOUNCY));
            }
            if (imgMoving == null) {
                imgMoving = ImageIO.read(new File(IMG_FILE_MOVING));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgToDraw = switch (type) {
            case 1 -> imgBouncy;
            case 4 -> imgMoving;
            default -> img;
        };

        if (type == 1) {
            this.setAffectVy(BOUNCYAFFECTVY);
        }
    }

    public void setPx(int px) {
        super.setPx(px, true);
    }

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

    @Override
    public void setPx(int px, boolean wantClipping) {
        this.setPx(px);
    }

    @Override
    public void interact(GameObj that) {
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}

class RegularPlatform extends Platform {
    public RegularPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 0);
    }

    public RegularPlatform(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(px, py, vx, vy, courtWidth, courtHeight, 0);
    }

    @Override
    public String toString() {
        return "0," + super.toString();
    }
}

class BouncyPlatform extends Platform {
    public BouncyPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 1);
    }

    public BouncyPlatform(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(px, py, vx, vy, courtWidth, courtHeight, 1);
    }

    @Override
    public String toString() {
        return "1," + super.toString();
    }
}

class WeakPlatform extends Platform {
    public static final String IMG_FILE = "files/weakPlatform.png";
    public static final String IMG_FILE_BROKEN = "files/weakPlatformBroken.png";
    private static BufferedImage img;
    private static BufferedImage imgBroken;
    private BufferedImage imgToDraw;

    private int state = 0;

    public WeakPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 0);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBroken == null) {
                imgBroken = ImageIO.read(new File(IMG_FILE_BROKEN));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.imgToDraw = img;
    }

    public WeakPlatform(
            int px, int py, int vx, int vy, int courtWidth, int courtHeight, int state
    ) {
        super(px, py, vx, vy, courtWidth, courtHeight, 0);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBroken == null) {
                imgBroken = ImageIO.read(new File(IMG_FILE_BROKEN));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (state == 1) {
            this.imgToDraw = imgBroken;
            this.state = 1;
        } else {
            this.imgToDraw = img;
            this.state = 0;
        }
    }

    @Override
    public void interact(GameObj that) {
        if (this.intersects(that)) {
            if (that.getClass() == Player.class) {
                this.imgToDraw = imgBroken;
                this.state = 1;
            }
        }
    }

    @Override
    public boolean intersects(GameObj that) {
        return super.intersects(that) && that.getVy() >= 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
                this.imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null
        );
    }

    @Override
    public String toString() {
        return "2," + super.toString() + "," + this.state;
    }
}

class DisappearingPlatform extends Platform {

    private static final int TICK_STEP = 32;
    public static final String IMG_FILE_1 = "files/disappearingPlatformTick1.png";
    public static final String IMG_FILE_2 = "files/disappearingPlatformTick2.png";
    public static final String IMG_FILE_3 = "files/disappearingPlatformTick3.png";

    private static BufferedImage img1;
    private static BufferedImage img2;
    private static BufferedImage img3;

    private int state;
    private BufferedImage imgToDraw;

    public DisappearingPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 0);

        RandomNumberGenerator rng = new RandomNumberGenerator();

        try {
            img1 = ImageIO.read(new File(IMG_FILE_1));
            img2 = ImageIO.read(new File(IMG_FILE_2));
            img3 = ImageIO.read(new File(IMG_FILE_3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgToDraw = img1;

        state = -Math.min(((courtHeight - this.getPy()) / 2), rng.next(100));
    }

    public DisappearingPlatform(
            int px, int py, int vx, int vy, int courtWidth, int courtHeight, int state
    ) {
        super(px, py, vx, vy, courtWidth, courtHeight, 0);

        try {
            img1 = ImageIO.read(new File(IMG_FILE_1));
            img2 = ImageIO.read(new File(IMG_FILE_2));
            img3 = ImageIO.read(new File(IMG_FILE_3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgToDraw = img1;

        this.state = state;
    }

    public boolean tick() {
        state++;
        if (state > TICK_STEP && state < TICK_STEP * 2) {
            imgToDraw = img2;
        } else if (state > TICK_STEP * 3) {
            imgToDraw = img3;
        }
        return shouldDelete();
    }

    public boolean shouldDelete() {
        return (state > TICK_STEP * 4);
    }

    @Override
    public String toString() {
        return "3," + super.toString() + "," + state;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}

class MovingPlatform extends Platform {

    private static final int SPEED = 4;
    private static final int AFFECTVY = -35;

    public MovingPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 4);

        Random rng = new Random();
        if (rng.nextBoolean()) {
            this.setVx(SPEED);
        } else {
            this.setVx(-SPEED);
        }

        this.setAffectVy(AFFECTVY);
    }

    public MovingPlatform(int px, int py, int vx, int vy, int courtWidth, int courtHeight) {
        super(px, py, vx, vy, courtWidth, courtHeight, 4);

        this.setAffectVy(AFFECTVY);
    }

    @Override
    public void move() {

        if (this.getPx() >= this.getMaxX()) {
            this.setVx(-SPEED);
        } else if (this.getPx() <= 0) {
            this.setVx(SPEED);
        }

        super.move();
    }

    @Override
    public String toString() {
        return "4," + super.toString();
    }
}
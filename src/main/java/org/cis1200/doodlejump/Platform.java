package org.cis1200.doodlejump;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Platform extends GameObj {
    public static final String IMG_FILE = "files/platform.png";
    public static final String IMG_FILE_BOUNCY = "files/bouncyPlatform.png";
    public static final int WIDTH = 100;
    public static final int HEIGHT = 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    public static final int INIT_ACCEL_X = 0;
    public static final int INIT_ACCEL_Y = 0;
    public static final int HP = 1;
    public static final int AFFECTVY = -25;
    public static final int BOUNCYAFFECTVY = -40;

    private static BufferedImage img;
    private static BufferedImage imgBouncy;

    private final BufferedImage imgToDraw;

    public Platform(int px, int py, int courtWidth, int courtHeight, int type) {
        super(
                INIT_VEL_X, INIT_VEL_Y, px, py, WIDTH, HEIGHT, courtWidth, courtHeight,
                INIT_ACCEL_X, INIT_ACCEL_Y, HP, AFFECTVY
        );
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBouncy == null) {
                imgBouncy = ImageIO.read(new File(IMG_FILE_BOUNCY));
            }
        } catch (IOException e) {
            // TODO: create a page that will be pushed toward the game
        }

        if (type == 1) {
            imgToDraw = imgBouncy;
            this.setAffectVy(BOUNCYAFFECTVY);
        } else {
            imgToDraw = img;
        }
    }

    public void setPx(int px) {
        super.setPx(px, true);
    }

    @Override
    public void setPx(int px, boolean wantClipping) {
        this.setPx(px);
    }

    @Override
    public void interact(GameObj that) {
        return;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        g.setColor(Color.RED);
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append(this.getPx());
        representation.append(",");
        representation.append(this.getPy());
        return representation.toString();
    }
}

class RegularPlatform extends Platform {
    public RegularPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 0);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("0,");
        representation.append(super.toString());
        return representation.toString();
    }
}

class BouncyPlatform extends Platform {
    public BouncyPlatform(int px, int py, int courtWidth, int courtHeight) {
        super(px, py, courtWidth, courtHeight, 1);
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("1,");
        representation.append(super.toString());
        return representation.toString();
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
            // TODO: Create some page
        }
        this.imgToDraw = img;
    }

    public WeakPlatform(int px, int py, int courtWidth, int courtHeight, int state) {
        super(px, py, courtWidth, courtHeight, 0);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
            if (imgBroken == null) {
                imgBroken = ImageIO.read(new File(IMG_FILE_BROKEN));
            }
        } catch (IOException e) {
            //TODO: Do something
        }

        if (state == 1) {
            this.imgToDraw = imgBroken;
            this.state = 1;
        } else  {
            this.imgToDraw = img;
            this.state = 0;
        }
    }

    @Override
    public void interact(GameObj that) {
        if (this.willIntersect(that)) {
            if (that.getClass() == Player.class) {
                this.imgToDraw = imgBroken;
                this.state = 1;
            }
        }
    }

    @Override
    public boolean willIntersect(GameObj that) {
        return super.willIntersect(that) && that.getVy() >= 0;
    }

    @Override
    public boolean intersects(GameObj that) {
        return super.intersects(that) && that.getVy() >= 0;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        g.setColor(Color.RED);
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("2,");
        representation.append(super.toString());
        representation.append(",");
        representation.append(this.state);
        return representation.toString();
    }
}

class DisappearingPlatform extends Platform {

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

    public boolean tick() {
        state++;
        if (state > 60 && state < 90) {
            imgToDraw = img2;
        } else if (state > 100) {
            imgToDraw = img3;
        }
        return shouldDelete();
    }

    public boolean shouldDelete() {
        return state > 115;
    }

    @Override
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append("3,");
        representation.append(super.toString());
        return representation.toString();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
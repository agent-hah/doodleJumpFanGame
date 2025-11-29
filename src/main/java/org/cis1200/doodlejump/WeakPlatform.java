package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeakPlatform extends Platform {
    public static String IMG_FILE = "files/weakPlatform.png";
    public static String IMG_FILE_BROKEN = "files/weakPlatformBroken.png";
    public static BufferedImage img;
    public static BufferedImage imgBroken;
    private BufferedImage imgToDraw;

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
        imgToDraw = img;
    }

    @Override
    public void interact(GameObj that) {
        if (this.willIntersect(that)) {
            if (that.getClass() == Player.class) {
                imgToDraw = imgBroken;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
        g.setColor(Color.RED);
        g.drawRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}

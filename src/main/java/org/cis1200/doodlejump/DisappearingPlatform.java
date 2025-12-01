package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisappearingPlatform extends Platform {

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
        this.setTypeRepresentation(3);
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
    public void draw(Graphics g) {
        g.drawImage(imgToDraw, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}

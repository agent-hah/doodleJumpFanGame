package org.cis1200.doodlejump;

import javax.swing.*;
import java.awt.*;

public class Instructions extends JPanel {
    public static final String TEXT_1 = "This Game is called Doodle Jump!";
    public static final String TEXT_2 = "The main objective of the game is to get as high";
    public static final String TEXT_3 = "as you can while avoiding the monsters and not falling.";
    public static final String TEXT_4 = "Use A and D to move left or right, respectively, and";
    public static final String TEXT_5 = "use space to shoot a shuriken up that can kill monsters!";
    public static final String TEXT_6 = "The blue monster dies with one shuriken, and the others";
    public static final String TEXT_7 = "take 2. Watch out for the saucers, " +
            "as they will chase you!";
    public static final String TEXT_8 = "The bouncy platforms will help you, but some platforms";
    public static final String TEXT_9 = "will disappear.";
    public static final String TEXT_10 = "Good luck!";

    public Instructions() {
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(TEXT_1, 5, 25);
        g.drawString(TEXT_2, 5, 50);
        g.drawString(TEXT_3, 5, 75);
        g.drawString(TEXT_4, 5, 100);
        g.drawString(TEXT_5, 5, 125);
        g.drawString(TEXT_6, 5, 150);
        g.drawString(TEXT_7, 5, 175);
        g.drawString(TEXT_8, 5, 200);
        g.drawString(TEXT_9, 5, 225);
        g.drawString(TEXT_10, 5, 250);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
}

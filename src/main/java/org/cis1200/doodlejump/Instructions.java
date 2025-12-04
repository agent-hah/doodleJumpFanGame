package org.cis1200.doodlejump;

import javax.swing.*;
import java.awt.*;

public class Instructions extends JTextArea {
    public static final String TEXT = """
            This Game is called Doodle Jump!
            
            The main objective of the game is to get as high as you can while avoiding the monsters \
            and not falling. Don't worry about jumping though, because your character is always jumping! \
            Use A and D to move left or right, respectively, and use SPACE to shoot a shuriken up \
            that can kill monsters! The blue monster dies with one shuriken, and the others take 2. \
            Watch out for the flying saucers, as they will chase you! The bouncy platforms will \
            help you, but some platforms will move or even disappear. Unlike Doodle Jump, you are not \
            able to jump on the monsters to kill them; they will kill you. This game does have a \
            wrap-around field! If you move beyond the right side of the game area, you'll come \
            out the left (and vice versa)!
            
            Good luck!
            """;

    public Instructions() {
        super.setEditable(false);
        super.setLineWrap(true);
        super.setWrapStyleWord(true);
        super.setRequestFocusEnabled(false);

        super.setFont(new Font("Monospaced", Font.PLAIN, 12));
        super.setBackground(Color.white);
        super.setOpaque(true);
        super.append(TEXT);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }
}

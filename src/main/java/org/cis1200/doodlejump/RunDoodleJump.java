package org.cis1200.doodlejump;

import java.awt.*;
import javax.swing.*;

public class RunDoodleJump implements Runnable {

    public static final String TEXT = """
            This Game is called Doodle Jump!
            
            The main objective of the game is to get as high as you can while avoiding the monsters
            and not falling. Don't worry about jumping though, because your character is always jumping!
            Use A and D to move left or right, respectively, and use SPACE to shoot a shuriken up
            that can kill monsters! The blue monster dies with one shuriken, and the others take 2.
            Watch out for the flying saucers, as they will chase you! The bouncy platforms will
            help you, but some platforms will move or even disappear. Unlike Doodle Jump, you are not
            able to jump on the monsters to kill them; they will kill you. This game does have a
            wrap-around field! If you move beyond the right side of the game area, you'll come
            out the left (and vice versa)!
            
            Good luck!
            """;

    @Override
    public void run() {
        // Instructions panel
        // ====================================================================
        final JButton instructionsButton = new JButton("Instructions");

        final JFrame frame = new JFrame("Doodle Jump!");
        frame.setLocation(500, 500);

        instructionsButton.addActionListener(e -> {
            this.showInstructions(frame);
        });

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running Doodle Jump!");
        status_panel.add(status);

        // Score panel
        final JLabel scoreLabel = new JLabel("Score: 0");

        // Control Panel with buttons
        final JButton resume = new JButton("Resume");
        final JButton reset = new JButton("Reset");
        final JButton pause = new JButton("Pause");
        final JButton save = new JButton("Save");

        // Main playing area
        final GameRegion court = new GameRegion(
                status, scoreLabel, resume, pause, reset, save,
                instructionsButton
        );
        frame.add(court, BorderLayout.CENTER);

        resume.addActionListener(e -> {
            court.unpause();
        });

        reset.addActionListener(e -> {
            court.reset();
        });

        pause.addActionListener(e -> {
            court.pause();
        });

        save.addActionListener(e -> {
            court.save();
        });

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        control_panel.add(pause);
        control_panel.add(scoreLabel);
        control_panel.add(resume);
        control_panel.add(reset);
        control_panel.add(save);
        control_panel.add(instructionsButton);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        if (!court.load()) {
            showInstructions(frame);
        }
    }

    private void showInstructions(Component frame) {
        JOptionPane.showMessageDialog(frame, TEXT, "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
}

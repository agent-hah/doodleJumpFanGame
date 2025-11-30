package org.cis1200.doodlejump;

import java.awt.*;
import javax.swing.*;

public class RunDoodleJump implements Runnable {

    @Override
    public void run() {
        final JFrame frame = new JFrame("Doodle Jump!");
        frame.setLocation(1000, 1000);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running Doodle Jump!");
        status_panel.add(status);

        // Score panel
        final JLabel scoreLabel = new JLabel("Score: 0");

        // Main playing area
        final GameRegion court = new GameRegion(status, scoreLabel);
        frame.add(court, BorderLayout.CENTER);

        // Control Panel with buttons
        final JButton resume = new JButton("Resume");
        final JButton reset = new JButton("Reset");
        final JButton pause = new JButton("Pause");

        resume.addActionListener(e -> {
            court.unpause();
            resume.setVisible(false);
            reset.setVisible(false);
        });

        reset.addActionListener(e -> {
            court.reset();
            resume.setVisible(false);
            reset.setVisible(false);
        });

        pause.addActionListener(e -> {
            court.pause();
            resume.setVisible(true);
            reset.setVisible(true);
        });

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        control_panel.add(pause);
        control_panel.add(scoreLabel);
        control_panel.add(resume);
        control_panel.add(reset);

        resume.setVisible(false);
        reset.setVisible(false);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}

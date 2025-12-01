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

        // Control Panel with buttons
        final JButton resume = new JButton("Resume");
        final JButton reset = new JButton("Reset");
        final JButton pause = new JButton("Pause");
        final JButton save = new JButton("Save");

        // Main playing area
        final GameRegion court = new GameRegion(status, scoreLabel, resume, pause, reset, save);
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

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.load();
    }
}

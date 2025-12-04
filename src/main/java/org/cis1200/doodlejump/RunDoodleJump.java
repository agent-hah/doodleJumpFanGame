package org.cis1200.doodlejump;

import java.awt.*;
import javax.swing.*;

public class RunDoodleJump implements Runnable {

    @Override
    public void run() {
        // Instructions panel ====================================================================
        final JFrame instructions = new JFrame("Instructions");
        instructions.setLocation(0, 0);
        instructions.add(new Instructions());

        instructions.pack();
        instructions.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        instructions.setVisible(true);

        final JButton instructions_button = new JButton("Instructions");
        instructions_button.addActionListener(e -> {
            instructions.setVisible(true);
        });


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
        final GameRegion court = new GameRegion(status, scoreLabel, resume, pause, reset, save, instructions_button);
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
        control_panel.add(instructions_button);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        if (court.load()) {
            instructions.setVisible(false);
        }
    }
}

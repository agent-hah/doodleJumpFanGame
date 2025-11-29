package org.cis1200.doodlejump;

import org.cis1200.doodlejump.GameRegion;

import java.awt.*;
import javax.swing.*;

public class RunDoodleJump implements Runnable{

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

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);
        control_panel.add(scoreLabel);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}

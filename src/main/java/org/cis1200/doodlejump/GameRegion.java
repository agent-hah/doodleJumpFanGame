package org.cis1200.doodlejump;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameRegion extends JPanel {

    // The state of the game
    private List<List<Platform>> platforms = new LinkedList<>();
    private Player player;
    private boolean playing = false;

    private final JLabel status;

    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 1000;
    public static final int PLAYER_VEL = 20;


    public static final int INTERVAL = 35;

    public GameRegion(JLabel status) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D:
                        player.setVx(PLAYER_VEL);
                        break;
                    case KeyEvent.VK_A:
                        player.setVx(-PLAYER_VEL);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                player.setVx(0);
            }
        });

        this.status = status;
    }

    private List<List<Platform>> createInitialPlatforms() {
        RandomNumberGenerator random = new RandomNumberGenerator();
        List<List<Platform>> res = new LinkedList<>();

        int max_count = 5;
        for(int count = 0; count <= max_count; count++) {
            int py = 150 * count + 20;
            Platform newPlatform1 = new
                    Platform(random.next(COURT_WIDTH), py, COURT_WIDTH, COURT_HEIGHT);
            Platform newPlatform2 = new
                    Platform(random.next(COURT_WIDTH), py, COURT_WIDTH, COURT_HEIGHT);
            while (newPlatform2.intersects(newPlatform1)) {
                newPlatform2 =
                        new Platform(random.next(COURT_WIDTH), py, COURT_WIDTH, COURT_HEIGHT);
            }
            List<Platform> toAdd = new LinkedList<>();
            toAdd.add(newPlatform1);
            toAdd.add(newPlatform2);
            res.add(toAdd);
        }

        return res;
    }

    public void reset() {
        player = new Player(COURT_WIDTH, COURT_HEIGHT);
        platforms = createInitialPlatforms();

        playing = true;
        requestFocusInWindow();
    }

    void tick() {
        if (playing) {
            player.move();

            // TODO: optimize collision checking
            for (List<Platform> platforms : platforms) {
                for (Platform platform : platforms) {
                    player.interact(platform);;
                }
            }
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        player.draw(g);
        for (List<Platform> platforms : platforms) {
            for (Platform platform : platforms) {
                platform.draw(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() { return new Dimension(COURT_WIDTH, COURT_HEIGHT); }
}

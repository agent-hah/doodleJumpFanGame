package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GameRegion extends JPanel {

    // The state of the game
    private LinkedList<LinkedList<Platform>> platforms = new LinkedList<>();
    private Player player;
    private boolean playing = false;

    private int interval = 80;

    private final JLabel status;

    private final JLabel scoreLabel;

    private int score = 0;

    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 800;
    public static final int PLAYER_VEL = 20;


    public static final int INTERVAL = 35;

    private static final String IMG_FILE = "files/doodleJumpBackground.jpg";

    private static BufferedImage img;
    private static BufferedImage imgIcon;
    private boolean paused = false;

    public GameRegion(JLabel status, JLabel score) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e)  {
                // TODO: Create some screen
            }
        }

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
        this.scoreLabel = score;
    }

    private LinkedList<Platform> getPlatformPair(int py) {
        RandomNumberGenerator random = new RandomNumberGenerator();
        Platform newPlatform1 = new
                Platform(random.next(COURT_WIDTH - Platform.WIDTH),
                py, COURT_WIDTH, COURT_HEIGHT, random.next(2));
        Platform newPlatform2 = new
                Platform(random.next(COURT_WIDTH - Platform.WIDTH),
                py, COURT_WIDTH, COURT_HEIGHT, random.next(2));
        while (newPlatform2.intersects(newPlatform1)) {
            newPlatform2 =
                    new Platform(random.next(COURT_WIDTH - Platform.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT, random.next(2));
        }
        LinkedList<Platform> toAdd = new LinkedList<>();
        toAdd.add(newPlatform1);
        toAdd.add(newPlatform2);
        return toAdd;
    }

    private LinkedList<LinkedList<Platform>> createInitialPlatforms() {
        LinkedList<LinkedList<Platform>> res = new LinkedList<>();

        int max_count = 10;
        for(int count = 0; count <= max_count; count++) {
            int py = interval * count + 20;
            res.add(getPlatformPair(py));
        }

        return res;
    }

    public void reset() {
        player = new Player(COURT_WIDTH, COURT_HEIGHT);
        this.interval = 80;
        platforms = createInitialPlatforms();
        this.score = 0;
        scoreLabel.setText("Score: " + score);
        status.setText("Running Doodle Jump!");

        playing = true;
        paused = false;
        requestFocusInWindow();
    }

    private void tick() {
        if (playing && !paused) {
            player.move();
            this.scrollDown();

            if (score > 1500) {
                interval = 200;
            }
            else if (score > 1000) {
                interval = 150;
            }
            else if (score > 500) {
                interval = 100;
            }

            for (List<Platform> platforms : platforms) {
                for(Platform platform : platforms) {
                    player.interact(platform);
                }
            }
        }

        repaint();
    }

    private void scrollDown() {
        if (player.getPy() < COURT_HEIGHT / 2) {
            int scoreToAdd = (COURT_HEIGHT / 2) - player.getPy();

            for (List<Platform> platforms : platforms) {
                for (Platform platform : platforms) {
                    platform.setPy(platform.getPy() + scoreToAdd);
                }
            }

            boolean fixing = true;
            while (fixing) { fixing = propagate(); }

            player.setPy(player.getPy() + scoreToAdd);
            score += scoreToAdd / 10;
            this.scoreLabel.setText("Score: " + score);
        }
    }

    private boolean propagate() {
        if (platforms.peekLast() != null && platforms.peekLast().peekLast() != null &&
        platforms.peekLast().peekLast().getPy() > COURT_HEIGHT - 20 -
                platforms.peekLast().peekLast().getHeight()) {
            platforms.removeLast();
            int py = platforms.peekFirst().peekLast().getPy();
            platforms.addFirst(getPlatformPair(py - interval));
            return true;
        }
        return false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void pause() {
        this.paused = true;
        this.status.setText("Paused");
    }

    public void unpause() {
        requestFocusInWindow();
        this.paused = false;
        this.status.setText("Running Doodle Jump!");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, COURT_WIDTH, COURT_HEIGHT, null);
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

package org.cis1200.doodlejump;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GameRegion extends JPanel {

    // The state of the game
    private List<List<Platform>> platforms = new LinkedList<>();
    private boolean playing = false;

    private final JLabel status;

    public static final int COURT_WIDTH = 1000;
    public static final int COURT_HEIGHT = 1000;
    public static final int PLAYER_INIT_VEL = 0;


    public static final int INTERVAL = 35;

    public GameRegion(JLabel status) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();
        platforms = createInitialPlatforms();

        setFocusable(true);

//        addKeyListener(new KeyAdapter (){
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode()) == KeyEvent.VK_D) {
//
//                }
//            }
//
//        });
        this.status = status;
    }

    private List<List<Platform>> createInitialPlatforms() {
        RandomNumberGenerator random = new RandomNumberGenerator();
        List<List<Platform>> res = new LinkedList<>();

        int max_count = 5;
        for(int count = 0; count <= max_count; count++) {
            int py = 200 * count;
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
        platforms = createInitialPlatforms();
        playing = true;

        requestFocusInWindow();
    }

    void tick() {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (List<Platform> platforms : platforms) {
            for (Platform platform : platforms) {
                platform.draw(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() { return new Dimension(COURT_WIDTH, COURT_HEIGHT); }
}

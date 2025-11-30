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
    private LinkedList<Monster> monsters = new LinkedList<>();
    private LinkedList<Bullet> bullets = new LinkedList<>();

    private boolean playing = false;

    private int yDist = 80;

    private final JLabel status;

    private final JLabel scoreLabel;

    private int score = 0;

    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 800;
    public static final int PLAYER_VEL = 20;

    public static final int INTERVAL = 35;

    public static final String IMG_FILE = "files/doodleJumpBackground.jpg";

    private static BufferedImage img;

    private boolean paused = false;

    public static final String SAVE_FILE = "files/doodleJump.txt";

    public GameRegion(JLabel status, JLabel score) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                // TODO: Create some screen
            }
        }

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    player.setVx(PLAYER_VEL);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    player.setVx(-PLAYER_VEL);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    bullets.add(player.shootBullet());
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

    private Platform createPlatform(int py, int choice) {
        RandomNumberGenerator random = new RandomNumberGenerator();

        return switch (choice) {
            case 1 -> Platform.getBouncyPlatform(
                    random.next(
                            COURT_WIDTH -
                                    Platform.WIDTH
                    ), py, COURT_WIDTH, COURT_HEIGHT
            );
            case 2 -> new WeakPlatform(
                    random.next(COURT_WIDTH - Platform.WIDTH),
                    py, COURT_WIDTH, COURT_HEIGHT
            );
            default -> Platform.getRegPlatform(
                    random.next(COURT_WIDTH - Platform.WIDTH),
                    py, COURT_WIDTH, COURT_HEIGHT
            );
        };
    }

    private LinkedList<Platform> getPlatformPair(int py) {
        Platform newPlatform1;
        Platform newPlatform2;
        RandomNumberGenerator random = new RandomNumberGenerator();

        if (score < 1000) {
            newPlatform1 = createPlatform(py, random.next(3));
            newPlatform2 = createPlatform(py, random.next(2));
        } else {
            newPlatform1 = new DisappearingPlatform(
                    random.next(COURT_WIDTH - Platform.WIDTH), py, COURT_WIDTH, COURT_HEIGHT
            );
            newPlatform2 = createPlatform(py, 1);
        }

        int dist = Math.abs(newPlatform2.getPx() - newPlatform1.getPx() - Platform.WIDTH);

        while (newPlatform2.intersects(newPlatform1) | dist <= 30) {
            newPlatform2.setPx(random.next(COURT_WIDTH - Platform.WIDTH));
            dist = Math.min(
                    Math.abs(newPlatform2.getPx() - newPlatform1.getPx() - Platform.WIDTH),
                    Math.abs(newPlatform1.getPx() - newPlatform2.getPx() - Platform.WIDTH)
            );
        }

        LinkedList<Platform> toAdd = new LinkedList<>();
        toAdd.add(newPlatform1);
        toAdd.add(newPlatform2);
        return toAdd;
    }

    private LinkedList<LinkedList<Platform>> createInitialPlatforms() {
        LinkedList<LinkedList<Platform>> res = new LinkedList<>();

        int maxCount = 9;
        for (int count = 0; count <= maxCount; count++) {
            int py = yDist * count + 20;
            res.add(getPlatformPair(py));
        }

        return res;
    }

    private void createMonster() {
        if (monsters.size() >= 2) {
            return;
        }

        Monster toAdd = null;

        RandomNumberGenerator random = new RandomNumberGenerator();
        int py = -40 - Monster.MONSTER_HEIGHT;
        if (monsters.peekFirst() != null && monsters.peekFirst().getPy() <= -30) {
            py = monsters.peekFirst().getPy() - 5 - Monster.MONSTER_HEIGHT;
        }
        if (score < 200) {
            switch (random.next(20)) {
                case 1:
                    toAdd = Monster.getRegularMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = Monster.getMovingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                default:
                    break;
            }
        } else if (score < 1000) {
            switch (random.next(10)) {
                case 1:
                    toAdd = Monster.getRegularMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = Monster.getMovingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 3:
                    // TODO: Create saucer monster
                default:
                    break;
            }
        } else {
            switch (random.next(5)) {
                case 1:
                    toAdd = Monster.getRegularMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = Monster.getMovingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 3:
                    // TODO: Create saucer monster
                    break;
                default:
                    break;
            }
        }

        if (toAdd != null) {
            for (Monster monster : monsters) {
                while (toAdd.intersects(monster)) {
                    toAdd.setPx(random.next(COURT_WIDTH - Monster.MONSTER_WIDTH));
                }
            }
            monsters.add(toAdd);
        }
    }

    public void reset() {
        player = new Player(COURT_WIDTH, COURT_HEIGHT);
        bullets = new LinkedList<>();
        monsters = new LinkedList<>();
        this.yDist = 80;
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

            createMonster();

            for (Monster monster : monsters) {
                monster.move();
            }

            for (Bullet bullet : bullets) {
                bullet.move();
            }

            this.scrollDown();

            if (score > 1500) {
                yDist = 200;
            } else if (score > 1000) {
                yDist = 150;
            } else if (score > 500) {
                yDist = 100;
            }

            for (LinkedList<Platform> platforms : platforms) {
                platforms.removeIf(platform -> {
                    if (platform.getClass() == DisappearingPlatform.class) {
                        return ((DisappearingPlatform) platform).tick();
                    } else {
                        return false;
                    }
                });
            }

            for (Monster monster : monsters) {
                for (Bullet bullet : bullets) {
                    monster.interact(bullet);
                    bullet.interact(monster);
                }
            }

            monsters.removeIf(monster -> monster.isDead());

            bullets.removeIf(bullet -> bullet.isHitTarget());

            for (LinkedList<Platform> platforms : platforms) {
                for (Platform platform : platforms) {
                    player.interact(platform);
                    if (platform.getClass() == WeakPlatform.class) {
                        platform.interact(player);
                    }
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

            for (Monster monster : monsters) {
                monster.setPy(monster.getPy() + scoreToAdd);
            }

            for (Bullet bullet : bullets) {
                bullet.setPy(bullet.getPy() + scoreToAdd);
            }

            boolean fixing = true;
            while (fixing) {
                fixing = propagate();
            }

            player.setPy(player.getPy() + scoreToAdd);
            score += scoreToAdd / 10;
            this.scoreLabel.setText("Score: " + score);
        }
    }

    private boolean propagate() {
        boolean keepGoing = false;

        if (platforms.peekLast() != null && platforms.peekLast().peekLast() != null &&
                platforms.peekLast().peekLast().getPy() > COURT_HEIGHT) {
            platforms.removeLast();
            int py = platforms.peekFirst().peekLast().getPy();
            platforms.addFirst(getPlatformPair(py - yDist));
            keepGoing = true;
        }

        if (monsters.peekLast() != null && monsters.peekLast().getPy() > COURT_HEIGHT) {
            monsters.removeLast();
            keepGoing = true;
        }

        if (bullets.peekLast() != null && bullets.peekLast().isOutOfBounds()) {
            bullets.removeLast();
            keepGoing = true;
        }

        return keepGoing;
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

    public void load() {

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
        for (Monster monster : monsters) {
            monster.draw(g);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}

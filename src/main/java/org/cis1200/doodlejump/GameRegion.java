package org.cis1200.doodlejump;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
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

    private final JButton resumeButton;

    private final JButton pauseButton;

    private final JButton resetButton;

    private final JButton saveButton;

    private final JButton instructions_button;

    private int score = 0;

    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 800;
    public static final int PLAYER_VEL = 20;

    public static final int INTERVAL = 35;

    public static final String IMG_FILE = "files/doodleJumpBackground.jpg";

    private static BufferedImage img;

    private boolean paused = false;

    public static final String SAVE_FILE = "files/saveFile.txt";

    public GameRegion(
            JLabel status, JLabel score, JButton resume, JButton pause, JButton reset,
            JButton save, JButton instructions_button
    ) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!paused && playing) {
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
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!paused && playing) {
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        player.setVx(0);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        player.setVx(0);
                    }
                }
            }
        });

        this.status = status;
        this.scoreLabel = score;
        this.resumeButton = resume;
        this.pauseButton = pause;
        this.resetButton = reset;
        this.saveButton = save;
        this.instructions_button = instructions_button;
    }

    private Platform createPlatform(int py, int choice) {
        RandomNumberGenerator random = new RandomNumberGenerator();

        return switch (choice) {
            case 1 -> new BouncyPlatform(
                    random.next(
                            COURT_WIDTH -
                                    Platform.WIDTH
                    ), py, COURT_WIDTH, COURT_HEIGHT
            );
            case 2 -> new WeakPlatform(
                    random.next(COURT_WIDTH - Platform.WIDTH),
                    py, COURT_WIDTH, COURT_HEIGHT
            );
            default -> new RegularPlatform(
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
        if (random.next(2) == 1) {
            newPlatform1 = new MovingPlatform(
                    random.next(COURT_WIDTH - Platform.WIDTH), py, COURT_WIDTH, COURT_HEIGHT
            );
            newPlatform2 = null;
        }

        if (newPlatform2 != null) {
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
        } else {
            LinkedList<Platform> toAdd = new LinkedList<>();
            toAdd.add(newPlatform1);
            return toAdd;
        }
    }

    private LinkedList<LinkedList<Platform>> createInitialPlatforms() {
        LinkedList<LinkedList<Platform>> res = new LinkedList<>();

        int maxCount = 8;
        for (int count = 0; count <= maxCount; count++) {
            int py = yDist * count + 20;
            res.add(getPlatformPair(py));
        }

        Platform initialPlatform = new RegularPlatform(350, 740, Platform.WIDTH, Platform.HEIGHT);
        LinkedList<Platform> toAdd = new LinkedList<>();
        toAdd.add(initialPlatform);
        res.add(toAdd);

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
                    toAdd = new RegularMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = new MovingMonster(
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
                    toAdd = new RegularMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = new MovingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 3:
                    toAdd = new HomingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT, player
                    );
                    break;
                default:
                    break;
            }
        } else {
            switch (random.next(5)) {
                case 1:
                    toAdd = new RegularMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = new MovingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 3:
                    new HomingMonster(
                            random.next(COURT_WIDTH - Monster.MONSTER_WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT, player
                    );
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
        playing = true;
        this.pauseButton.setVisible(true);
        this.resumeButton.setVisible(false);
        this.resetButton.setVisible(false);
        this.saveButton.setVisible(false);
        this.instructions_button.setVisible(false);
        requestFocusInWindow();

        player = new Player(COURT_WIDTH, COURT_HEIGHT);
        bullets = new LinkedList<>();
        monsters = new LinkedList<>();
        this.score = 0;
        this.yDist = 80;
        platforms = createInitialPlatforms();
        scoreLabel.setText("Score: " + score);
        status.setText("Running Doodle Jump!");

        wipeSave();
    }

    private void wipeSave() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(SAVE_FILE, false));
            bw.write("empty");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            status.setText("Error Clearing Save File. Please Do Not Save");
        }
    }

    public void beginGame(String buttonLabel) {
        this.paused = true;
        this.playing = true;
        this.pauseButton.setVisible(false);
        this.resumeButton.setVisible(true);
        this.resetButton.setVisible(false);
        this.saveButton.setVisible(false);
        this.instructions_button.setVisible(true);
        this.resumeButton.setText(buttonLabel);
    }

    public boolean load() {
        requestFocusInWindow();

        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
            String line;
            line = br.readLine();
            if (line != null && !line.equals("empty")) {
                this.score = Integer.parseInt(line);
                System.out.println(line);
                System.out.println(Integer.parseInt(line));
                this.player = SaveReader.loadPlayer(br.readLine());

                Platform newPlatform1;
                Platform newPlatform2;

                for (int count = 0; count < 10; count++) {
                    LinkedList<Platform> toAdd = new LinkedList<>();
                    newPlatform1 = SaveReader.loadPlatform(br.readLine());
                    newPlatform2 = SaveReader.loadPlatform(br.readLine());
                    if (newPlatform1 != null) {
                        toAdd.add(newPlatform1);
                    }
                    if (newPlatform2 != null) {
                        toAdd.add(newPlatform2);
                    }
                    platforms.add(toAdd);
                }

                line = br.readLine();

                if (line == null) {
                    return true;
                }

                switch (line) {
                    case "monsters":
                        line = br.readLine();
                        while (line != null && !line.equals("bullets")) {
                            this.monsters.add(SaveReader.loadMonster(line, this.player));
                            line = br.readLine();
                        }
                        if (line != null) {
                            line = br.readLine();
                            while (line != null) {
                                this.bullets.add(SaveReader.loadBullet(line));
                                line = br.readLine();
                            }
                        }
                        break;
                    case "bullets":
                        line = br.readLine();
                        while (line != null) {
                            this.bullets.add(SaveReader.loadBullet(line));
                            line = br.readLine();
                        }
                        break;
                    default:
                        break;
                }

                br.close();
                if (player.getPy() <= 0 | player.getPy() >= COURT_WIDTH | platforms.size() != 10) {
                    this.reset();
                    this.wipeSave();
                }

                scoreLabel.setText("Score: " + score);
                this.beginGame("Continue");
                status.setText("Continue The Game!");

                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                File file = new File(SAVE_FILE);
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
                this.reset();
                this.beginGame("Start");
                status.setText("Welcome To DoodleJump!");
                return false;
            } finally {
                wipeSave();
            }
        } catch (IOException | IllegalArgumentException e) {
            this.reset();
            this.beginGame("Start");
            status.setText("Welcome To DoodleJump!");
            return false;
        } finally {
            wipeSave();
        }

        this.reset();
        this.beginGame("Start");
        status.setText("Welcome To DoodleJump!");
        return false;
    }

    private void tick() {
        if (playing && !paused) {
            player.move();

            createMonster();

            for (LinkedList<Platform> platformPair : platforms) {
                for (Platform platform : platformPair) {
                    if (platform.getClass() == MovingPlatform.class) {
                        platform.move();
                    }
                }
            }

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
                player.interact(monster);
                for (Bullet bullet : bullets) {
                    monster.interact(bullet);
                    bullet.interact(monster);
                }
            }

            monsters.removeIf(Monster::isDead);

            bullets.removeIf(bullet -> (bullet.isHitTarget() | bullet.isOutOfBounds()));

            for (LinkedList<Platform> platforms : platforms) {
                for (Platform platform : platforms) {
                    player.interact(platform);
                    if (platform.getClass() == WeakPlatform.class) {
                        platform.interact(player);
                    }
                }
            }

            if (player.getPy() >= COURT_HEIGHT | player.getHp() == 0) {
                playing = false;
                this.status.setText("Game Over!");
                this.resetButton.setVisible(true);
                this.instructions_button.setVisible(true);
                this.pauseButton.setVisible(false);
                this.resumeButton.setVisible(false);
                this.saveButton.setVisible(false);
                this.saveButton.setEnabled(true);
                player.setVy(25);
                player.setAy(0);
                player.setVx(0);

                wipeSave();
            }
        } else if (!playing) {
            scrollUp();
            if (player.getPy() <= COURT_HEIGHT + 10) {
                player.move();
            }
        }

        repaint();
    }

    private void scrollDown() {
        if (player.getPy() < COURT_HEIGHT / 2) {
            int scoreToAdd = (COURT_HEIGHT / 2) - player.getPy();

            scroll(scoreToAdd);

            boolean fixing = true;
            while (fixing) {
                fixing = propagateUpward();
            }

            player.setPy(player.getPy() + scoreToAdd);
            score += scoreToAdd / 5;
            this.scoreLabel.setText("Score: " + score);
        }
    }

    private void scroll(int scoreToAdd) {
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
    }

    private void scrollUp() {
        int step = -30;

        if (!platforms.isEmpty() | !monsters.isEmpty() | !bullets.isEmpty()) {
            scroll(step);
            player.setPy(player.getPy() + step);
        }

        boolean fixing = true;
        while (fixing) {
            fixing = propagateDownward();
        }
    }

    private boolean propagateDownward() {
        boolean keepGoing = false;

        if (platforms.peekFirst() != null && platforms.peekFirst().peekFirst() != null &&
                platforms.peekFirst().peekFirst().getPy() <= 0) {
            platforms.removeFirst();
            keepGoing = true;
        }

        if (monsters.peekFirst() != null && monsters.peekLast().getPy() <= 0) {
            monsters.removeFirst();
            keepGoing = true;
        }

        if (bullets.peekFirst() != null && bullets.peekLast().getPy() <= 0) {
            bullets.removeFirst();
            keepGoing = true;
        }

        return keepGoing;
    }

    private boolean propagateUpward() {
        boolean keepGoing = false;

        if (platforms.peekLast() != null && platforms.peekLast().peekLast() != null &&
                platforms.peekLast().peekLast().getPy() > COURT_HEIGHT) {
            platforms.removeLast();
            int py = 0;
            if (platforms.peekFirst() != null && platforms.peekFirst().peekLast() != null) {
                py = platforms.peekFirst().peekLast().getPy();
            }
            platforms.addFirst(getPlatformPair(py - yDist));
            keepGoing = true;
        }

        if (monsters.peekLast() != null && monsters.peekLast().getPy() > COURT_HEIGHT) {
            monsters.removeLast();
            keepGoing = true;
        }

        if (bullets.peekLast() != null && bullets.peekLast().getPy() > COURT_HEIGHT) {
            bullets.removeLast();
            keepGoing = true;
        }

        return keepGoing;
    }

    public void pause() {
        this.paused = true;
        this.pauseButton.setVisible(false);
        this.resumeButton.setVisible(true);
        this.resetButton.setVisible(true);
        this.saveButton.setVisible(true);
        this.instructions_button.setVisible(true);
        this.status.setText("Paused");

    }

    public void unpause() {
        requestFocusInWindow();
        this.paused = false;
        this.status.setText("Running Doodle Jump!");
        this.pauseButton.setVisible(true);
        this.resumeButton.setVisible(false);
        this.resetButton.setVisible(false);
        this.saveButton.setVisible(false);
        this.saveButton.setEnabled(true);
        this.instructions_button.setVisible(false);
        if (!this.resumeButton.getText().equals("Resume")) {
            this.resumeButton.setText("Resume");
        }

        wipeSave();
    }

    public void save() {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(SAVE_FILE, false));

            bw.write(Integer.toString(this.score));
            bw.flush();
            bw.newLine();

            bw.write(this.player.toString());
            bw.flush();
            bw.newLine();

            for (LinkedList<Platform> platforms : platforms) {
                for (Platform platform : platforms) {
                    bw.write(platform.toString());
                    bw.flush();
                    bw.newLine();
                    if (platforms.size() == 1) {
                        bw.write("null");
                        bw.flush();
                        bw.newLine();
                    }
                }
            }

            if (!monsters.isEmpty()) {
                bw.write("monsters");
                bw.flush();
                bw.newLine();
            }

            for (Monster monster : monsters) {
                bw.write(monster.toString());
                bw.flush();
                bw.newLine();
            }

            if (!bullets.isEmpty()) {
                bw.write("bullets");
                bw.flush();
                bw.newLine();
            }

            for (Bullet bullet : bullets) {
                bw.write(bullet.toString());
                bw.flush();
                bw.newLine();
            }

            bw.close();

            this.status.setText("Saved Doodle Jump! Ok to Exit Game!");
            this.saveButton.setEnabled(false);
        } catch (IOException e) {
            this.status.setText(
                    "Error while writing file! Please Don't Close the Application and "
                            + "Just Continue Your Game!"
            );
            this.saveButton.setEnabled(false);
        }
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

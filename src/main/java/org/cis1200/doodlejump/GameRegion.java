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

/** The game region (the internal model) */
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

    private final JButton instructionsButton;

    private int score = 0;

    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 800;
    public static final int PLAYER_VEL = 20;

    public static final int INTERVAL = 35;

    public static final String IMG_FILE = "files/doodleJumpBackground.jpg";

    private static BufferedImage img;

    private boolean paused = false;

    public static final String SAVE_FILE = "src/main/java/org/cis1200/doodlejump/saveFile.txt";

    /**
     * Constructor for creating the game region
     * 
     * @param status             the status JLabel that will be updated to the state
     *                           of the game
     * @param score              the score JLabel that will be updated as the player
     *                           goes up
     * @param resume             the JButton that will allow the player to continue
     *                           playing
     * @param pause              the JButton that will temporarily stop the game
     * @param reset              the JButton that will have restart the game from an
     *                           initial position
     * @param save               the JButton that will save the internal state of
     *                           the game to a text file
     * @param instructionsButton the JButton that will release the instructions
     */
    public GameRegion(
            JLabel status, JLabel score, JButton resume, JButton pause, JButton reset,
            JButton save, JButton instructionsButton
    ) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (img == null) {
            try {
                img = ImageIO.read(new File(IMG_FILE));
            } catch (IOException e) {
                img = new BufferedImage(
                        img.getWidth(), img.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                );
            }
        }

        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start();

        setFocusable(true);

        /*
         * Key listener so that the velocity of the player will be updated
         */
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
        this.instructionsButton = instructionsButton;
    }

    /**
     * Modular method for creating one platform
     * 
     * @param py     the initial y position that the platform will be created with
     * @param choice the type that is chosen
     * @return the newly created platform object
     */
    private Platform createPlatform(int py, int choice) {
        RandomGenerator random = new RandomGenerator();

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

    /**
     * Modular method for creating a LinkedList that will be inserted into the
     * larger LinkedList
     * 
     * @param py the initial y position that the platforms will be created with
     * @return the LinkedList object
     */
    private LinkedList<Platform> getPlatformPair(int py) {
        Platform newPlatform1;
        Platform newPlatform2;
        RandomGenerator random = new RandomGenerator();

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

    /**
     * Modular method for creating all the initial platforms when the game is reset
     * 
     * @return the total initial platform data structure
     */
    private LinkedList<LinkedList<Platform>> createInitialPlatforms() {
        LinkedList<LinkedList<Platform>> res = new LinkedList<>();

        int maxCount = 8;
        for (int count = 0; count <= maxCount; count++) {
            int py = yDist * count + 20;
            res.add(getPlatformPair(py));
        }

        Platform initialPlatform = new RegularPlatform(
                350, 740, Platform.WIDTH,
                Platform.HEIGHT
        );
        LinkedList<Platform> toAdd = new LinkedList<>();
        toAdd.add(initialPlatform);
        res.add(toAdd);

        return res;
    }

    /**
     * Modular method for creating a monster
     */
    private void createMonster() {
        int multiplier = monsters.isEmpty() ? 1 : monsters.size();

        if (monsters.size() > 5) {
            multiplier *= (int) Math.round(Math.pow(10, monsters.size()));
        }

        Monster toAdd = null;

        RandomGenerator random = new RandomGenerator();
        int py = -60;
        if (score < 200) {
            switch (random.next(20 * multiplier)) {
                case 1:
                    toAdd = new RegularMonster(
                            random.next(COURT_WIDTH - RegularMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = new MovingMonster(
                            random.next(COURT_WIDTH - MovingMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                default:
                    break;
            }
        } else if (score < 1000) {
            switch (random.next(10 * multiplier)) {
                case 1:
                    toAdd = new RegularMonster(
                            random.next(COURT_WIDTH - RegularMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = new MovingMonster(
                            random.next(COURT_WIDTH - MovingMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 3:
                    toAdd = new HomingMonster(
                            random.next(COURT_WIDTH - HomingMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT, player
                    );
                    break;
                default:
                    break;
            }
        } else {
            switch (random.next(5 * multiplier)) {
                case 1:
                    toAdd = new RegularMonster(
                            random.next(COURT_WIDTH - RegularMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 2:
                    toAdd = new MovingMonster(
                            random.next(COURT_WIDTH - MovingMonster.WIDTH),
                            py, COURT_WIDTH, COURT_HEIGHT
                    );
                    break;
                case 3:
                    new HomingMonster(
                            random.next(COURT_WIDTH - HomingMonster.WIDTH),
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
                    toAdd.setPx(random.next(COURT_WIDTH - toAdd.getWidth()));
                }
            }
            monsters.addFirst(toAdd);
        }
    }

    /**
     * Public method for resetting the game to the initial state
     */
    public void reset() {
        playing = true;
        paused = false;
        this.pauseButton.setVisible(true);
        this.resumeButton.setVisible(false);
        this.resetButton.setVisible(false);
        this.saveButton.setVisible(false);
        this.saveButton.setEnabled(true);
        this.instructionsButton.setVisible(false);
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

    /**
     * The private, modular method that wipes the savefile in 3 possible situations:
     * <p>
     * 1. The game is loaded from the savefile
     * </p>
     * <p>
     * 2. The game is reset
     * </p>
     * <p>
     * 3. The game is unpaused
     * </P>
     */
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

    /**
     * Modular method for starting the game, and adjusting the button visibility
     * and the resume button text. Used only in the load method.
     * <p>
     * Overlaps with reset because reset can be called independently of beginGame
     * </p>
     * 
     * @param buttonLabel the text that will be put on the resume button
     */
    private void beginGame(String buttonLabel) {
        this.paused = true;
        this.playing = true;
        this.pauseButton.setVisible(false);
        this.resumeButton.setVisible(true);
        this.resetButton.setVisible(false);
        this.saveButton.setVisible(false);
        this.saveButton.setEnabled(true);
        this.instructionsButton.setVisible(true);
        this.resumeButton.setText(buttonLabel);
    }

    /**
     * Modular method for creating the save
     * 
     * @return false if loading failed (or savefile empty); true if loading was
     *         successful
     */
    private boolean loadSave() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE));
            String line;
            line = br.readLine();
            if (line != null && !line.equals("empty")) {
                this.score = Integer.parseInt(line);
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
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            try {
                new File(SAVE_FILE).createNewFile();
            } catch (IOException e1) {
                System.out.println("Something went wrong when trying to load the save file");
                return false;
            } finally {
                wipeSave();
            }
        } catch (IOException | IllegalArgumentException e) {
            return false;
        } finally {
            wipeSave();
        }
        return false;
    }

    /**
     * Method for loading the game, potentially from a save file
     * 
     * @return true if the game was backed from a save; false otherwise
     */
    public boolean load() {
        requestFocusInWindow();
        boolean result = loadSave();

        if (result) {
            scoreLabel.setText("Score: " + score);
            this.beginGame("Continue");
            status.setText("Continue The Game!");
        } else {
            this.reset();
            this.beginGame("Start");
            status.setText("Welcome To DoodleJump!");
        }
        return result;
    }

    /**
     * Where the logic and internal game state changes occur.
     */
    private void tick() {
        if (playing && !paused) {
            player.move();

            createMonster();

            for (LinkedList<Platform> platformPair : platforms) {
                for (Platform platform : platformPair) {
                    platform.move();
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
                yDist = 175;
            } else if (score > 1000) {
                yDist = 150;
            } else if (score > 500) {
                yDist = 100;
            }

            for (LinkedList<Platform> platforms : platforms) {
                platforms.removeIf(Platform::shouldDelete);
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
                    platform.interact(player);
                }
            }

            if (player.getPy() >= COURT_HEIGHT | player.getHp() == 0) {
                playing = false;
                this.status.setText("Game Over!");
                this.resetButton.setVisible(true);
                this.instructionsButton.setVisible(true);
                this.pauseButton.setVisible(false);
                this.resumeButton.setVisible(false);
                this.saveButton.setVisible(false);
                this.saveButton.setEnabled(true);
                player.setVy(25);
                player.setAy(0);
                player.setVx(0);
            }
        } else if (!playing) {
            scrollUp();
            if (player.getPy() <= COURT_HEIGHT + 10) {
                player.move();
            }
        }

        repaint();
    }

    /**
     * Method that makes the all game objects collectively move downward then the
     * player goes up
     * high enough
     */
    private void scrollDown() {
        if (player.getPy() < COURT_HEIGHT / 2) {
            int scoreToAdd = (COURT_HEIGHT / 2) - player.getPy();

            scroll(scoreToAdd);
            propagateUpward();

            player.setPy(player.getPy() + scoreToAdd);
            score += scoreToAdd / 3;
            this.scoreLabel.setText("Score: " + score);
        }
    }

    /**
     * Modular method that makes all game objects collectively move (up or down)
     * 
     * @param amount the amount that all the objects will move by (up or down
     *               depending on input)
     */
    private void scroll(int amount) {
        for (List<Platform> platforms : platforms) {
            for (Platform platform : platforms) {
                platform.setPy(platform.getPy() + amount);
            }
        }

        for (Monster monster : monsters) {
            monster.setPy(monster.getPy() + amount);
        }

        for (Bullet bullet : bullets) {
            bullet.setPy(bullet.getPy() + amount);
        }
    }

    /**
     * Method that makes all the objects collectively move upward (called when game
     * is lost)
     */
    private void scrollUp() {
        int step = -30;

        if (!platforms.isEmpty() | !monsters.isEmpty() | !bullets.isEmpty()) {
            scroll(step);
            player.setPy(player.getPy() + step);
            propagateDownward();
        }
    }

    /**
     * Modular method that deletes game objects that are above the top of the game
     * region
     */
    private void propagateDownward() {

        if (platforms.peekFirst() != null && platforms.peekFirst().peekFirst() != null &&
                platforms.peekFirst().peekFirst().getPy() <= 0) {
            platforms.removeFirst();
            propagateDownward();
        }

        if (monsters.peekFirst() != null && monsters.peekFirst().getPy() <= 0) {
            monsters.removeFirst();
            propagateDownward();
        }

        if (bullets.peekLast() != null && bullets.peekLast().getPy() <= 0) {
            bullets.removeFirst();
            propagateDownward();
        }

    }

    /**
     * Modular method that deletes game objects that are below the bottom of the
     * game region
     */
    private void propagateUpward() {

        if (platforms.peekLast() != null && platforms.peekLast().peekLast() != null &&
                platforms.peekLast().peekLast().getPy() > COURT_HEIGHT) {
            platforms.removeLast();
            int py = 0;
            if (platforms.peekFirst() != null && platforms.peekFirst().peekLast() != null) {
                py = platforms.peekFirst().peekLast().getPy();
            }
            platforms.addFirst(getPlatformPair(py - yDist));
            propagateUpward();
        }

        if (monsters.peekLast() != null && monsters.peekLast().getPy() > COURT_HEIGHT) {
            monsters.removeLast();
            propagateUpward();
        }

        if (bullets.peekFirst() != null && bullets.peekFirst().getPy() > COURT_HEIGHT) {
            bullets.removeFirst();
            propagateUpward();
        }
    }

    /**
     * public method that pauses the game
     */
    public void pause() {
        this.paused = true;
        this.pauseButton.setVisible(false);
        this.resumeButton.setVisible(true);
        this.resetButton.setVisible(true);
        this.saveButton.setVisible(true);
        this.instructionsButton.setVisible(true);
        this.status.setText("Paused");

    }

    /**
     * public method that resumes the game
     */
    public void unpause() {
        requestFocusInWindow();
        this.paused = false;
        this.status.setText("Running Doodle Jump!");
        this.pauseButton.setVisible(true);
        this.resumeButton.setVisible(false);
        this.resetButton.setVisible(false);
        this.saveButton.setVisible(false);
        this.saveButton.setEnabled(true);
        this.instructionsButton.setVisible(false);
        if (!this.resumeButton.getText().equals("Resume")) {
            this.resumeButton.setText("Resume");
        }

        wipeSave();
    }

    /**
     * modular method for saving the game
     * 
     * @return true if the game saved, false if not
     */
    private boolean saveGame() {
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
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * public method for saving the game
     */
    public void save() {
        if (saveGame()) {
            this.status.setText("Saved Doodle Jump! Ok to Exit Game!");
        } else {
            this.status.setText(
                    "Error while writing file! Please Don't Close the Application and "
                            + "Just Continue Your Game!"
            );
        }
        this.saveButton.setEnabled(false);
    }

    /**
     * the method that paints all game objects onto the screen (and the background)
     * 
     * @param g the graphics context
     */
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

    /**
     * method that gives the preferred method of the GameRegion
     * 
     * @return the Dimension
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    /**
     * method that shows whether the game is playing or not (used for testing
     * purposes)
     * 
     * @return true if the game is on, false if not
     */
    public boolean isPlaying() {
        return playing;
    }
}

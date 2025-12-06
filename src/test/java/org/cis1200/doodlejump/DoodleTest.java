package org.cis1200.doodlejump;

import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class DoodleTest {

    GameRegion court;
    String saveFile = "src/main/java/org/cis1200/doodlejump/saveFile.txt";

    @BeforeEach
    public void setUp() {
        final JButton resume = new JButton("Resume");
        final JButton reset = new JButton("Reset");
        final JButton pause = new JButton("Pause");
        final JButton save = new JButton("Save");
        final JLabel status = new JLabel("Status");
        final JLabel scoreLabel = new JLabel("Load");
        final JButton instructionsButton = new JButton("Sound");

        court = new GameRegion(
                status, scoreLabel, resume, pause, reset, save,
                instructionsButton
        );
        court.reset();
    }

    @Test
    public void testSaveReader() {
        String test = "45,-12,13,5";
        LinkedList<Integer> expected = new LinkedList<>(Arrays.asList(45, -12, 13, 5));
        LinkedList<Integer> actual = SaveReader.parse(test);

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveWriter() {
        String expectedPlayer = "480,480,0,0";
        String expectedPlatform = "0,134,-179,0,0";
        String expectedPlatformWeak = "2,134,-179,0,0,0";

        Platform weak = new WeakPlatform(134, -179, 30000, 30000);
        Platform regular = new RegularPlatform(134, -179, 30000, 30000);
        Player player = new Player(480, 480, 0, 0, 30000, 30000);

        assertEquals(expectedPlayer, player.toString());
        assertEquals(expectedPlatform, regular.toString());
        assertEquals(expectedPlatformWeak, weak.toString());
    }

    @Test
    public void testSaveWritingAndLoading() {
        Player expectedPlayer = new Player(480, 480, 0, 0, 30000, 30000);
        Player actualPlayer = SaveReader.loadPlayer(expectedPlayer.toString());

        assertEquals(expectedPlayer.getPx(), actualPlayer.getPx());
        assertEquals(expectedPlayer.getPy(), actualPlayer.getPy());
        assertEquals(expectedPlayer.getVx(), actualPlayer.getVx());
        assertEquals(expectedPlayer.getVy(), actualPlayer.getVy());
    }

    @Test
    public void testResettingGameWipesSave() {
        court.save();
        BufferedReader bw;
        try {
            bw = new BufferedReader(new FileReader(saveFile));
            assertNotEquals("empty", bw.readLine());
            bw.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        court.reset();
        try {
            bw = new BufferedReader(new FileReader(saveFile));
            assertEquals("empty", bw.readLine());
            bw.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetIsPlaying() {
        assertTrue(court.isPlaying());
    }

    @Test
    public void testCollisionBetweenPlayerAndMonster() {
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Monster monster = new RegularMonster(20, 20, 30000, 30000);

        assertTrue(player.intersects(monster));
        assertTrue(monster.intersects(player));
    }

    @Test
    public void testCollisionBetweenPlayerAndPlatform() {
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Platform platform = new RegularPlatform(20, 50, 30000, 30000);

        assertTrue(player.intersects(platform));
        assertTrue(platform.intersects(player));
    }

    @Test
    public void testCollisionBetweenPlayerAndPlatformTooHigh() {
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Platform platform = new RegularPlatform(20, 30, 30000, 30000);

        assertFalse(player.intersects(platform));
        assertFalse(platform.intersects(player));
    }

    @Test
    public void testCollisionBetweenPlayerAndBullet() {
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Bullet bullet = new Bullet(20, 20, 30000, 30000);

        assertTrue(player.intersects(bullet));
        assertTrue(bullet.intersects(player));
    }

    @Test
    public void testCollisionBetweenMonsterAndBullet() {
        Monster monster = new RegularMonster(20, 20, 30000, 30000);
        Bullet bullet = new Bullet(20, 20, 30000, 30000);

        assertTrue(monster.intersects(bullet));
        assertTrue(bullet.intersects(monster));
    }

    @Test
    public void testCollisionBetweenMonsterAndPlatform() {
        Monster monster = new RegularMonster(20, 20, 30000, 30000);
        Platform platform = new RegularPlatform(20, 50, 30000, 30000);

        assertTrue(platform.intersects(monster));
        assertTrue(monster.intersects(platform));
    }

    // For the monster, the collision should happen even if the platform is above
    // the monster's feet.
    @Test
    public void testCollisionBetweenPlatformAndMonsterHigh() {
        Monster monster = new RegularMonster(20, 20, 30000, 30000);
        Platform platform = new RegularPlatform(20, 20, 30000, 30000);

        assertTrue(platform.intersects(monster));
        assertTrue(monster.intersects(platform));
    }

    @Test
    public void testCollisionBetweenBulletAndPlatform() {
        Bullet bullet = new Bullet(20, 20, 30000, 30000);
        Platform platform = new RegularPlatform(20, 40, 30000, 30000);

        assertTrue(platform.intersects(bullet));
        assertTrue(bullet.intersects(platform));
    }

    // For the bullet, the collision should happen even if the platform is above the
    // bullet's halfway
    // point
    @Test
    public void testCollisionBetweenPlatformAndBulletHigh() {
        Bullet bullet = new Bullet(20, 20, 30000, 30000);
        Platform platform = new RegularPlatform(20, 20, 30000, 30000);

        assertTrue(platform.intersects(bullet));
        assertTrue(bullet.intersects(platform));
    }

    @Test
    public void testCollisionWithBulletAffectsOnlyMonster() {
        Bullet bullet = new Bullet(20, 20, 30000, 30000);
        Monster monster = new RegularMonster(20, 20, 30000, 30000);
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Platform platform = new RegularPlatform(20, 20, 30000, 30000);

        player.interact(bullet);
        monster.interact(bullet);
        platform.interact(bullet);

        assertEquals(0, monster.getHp());
        assertEquals(1, player.getHp());
        assertEquals(1, platform.getHp());
    }

    @Test
    public void testCollisionBulletOnlyRegistersHitWithMonster() {
        Bullet bullet = new Bullet(20, 20, 30000, 30000);
        Monster monster = new RegularMonster(20, 20, 30000, 30000);
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Platform platform = new RegularPlatform(20, 20, 30000, 30000);

        bullet.interact(player);
        assertFalse(bullet.isHitTarget());

        bullet.interact(platform);
        assertFalse(bullet.isHitTarget());

        bullet.interact(monster);
        assertTrue(bullet.isHitTarget());
    }

    /**
     */
    @Test
    public void testBulletDetectsBeingOutOfBounds() {
        Bullet bullet = new Bullet(20, 20, 30000, 30000);

        assertFalse(bullet.isOutOfBounds());

        // Note: we aren't going to check x bounds because it's impossible for the
        // bullet to go
        // out of bounds in the x direction
        bullet.setPy(40000000);
        assertTrue(bullet.isOutOfBounds());

        bullet.setPy(20);
        assertFalse(bullet.isOutOfBounds());
    }

    @Test
    public void testCollisionWithDifferentPlatformsGiveCorrectUpwardsSpeeds() {
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Platform platform = new RegularPlatform(20, 50, 30000, 30000);
        Platform platform2 = new BouncyPlatform(20, 50, 30000, 30000);
        Platform platform3 = new MovingPlatform(20, 50, 30000, 30000);
        Platform platform4 = new WeakPlatform(20, 50, 30000, 30000);
        Platform platform5 = new DisappearingPlatform(20, 50, 30000, 30000);

        player.interact(platform);
        assertEquals(-25, player.getVy());

        player.setVy(0);

        player.interact(platform2);
        assertEquals(-40, player.getVy());

        player.setVy(0);

        player.interact(platform3);
        assertEquals(-35, player.getVy());

        player.setVy(0);

        player.interact(platform4);
        assertEquals(0, player.getVy());

        player.setVy(0);

        player.interact(platform5);
        assertEquals(-30, player.getVy());
    }

    @Test
    public void testCollisionPlatformAndPlayerPreventsTunnel() {
        // It's not possible for the player to reach a speed event remotely close to 400 pixels
        // in the game so the padding isn't that big, but it's for dramatic effect
        Player player = new Player(20, 20, 0, 400, 30000, 30000);
        Platform platform = new RegularPlatform(20, 90, 30000, 30000);
        assertTrue(player.intersects(platform));
        assertTrue(platform.intersects(player));
        player.move();
        // Just to show that it would've been enough speed to tunnel
        assertFalse(player.intersects(platform));
        assertFalse(platform.intersects(player));
    }

    @Test
    public void testCollisionWithWeakPlatformChangesPlatformState() {
        Player player = new Player(20, 20, 0, 0, 30000, 30000);
        Platform platform = new WeakPlatform(20, 50, 30000, 30000);

        platform.interact(player);

        assertEquals(
                "1", String.valueOf((platform.toString().charAt(platform.toString().length() - 1)))
        );
    }

    @Test
    public void testDisappearingPlatformWillEventuallyWantToBeDeleted() {
        DisappearingPlatform platform = new DisappearingPlatform(20, 50, 30000, 30000);

        while (!platform.shouldDelete()) {
            platform.move();
        }

        assertTrue(platform.shouldDelete());
    }
}
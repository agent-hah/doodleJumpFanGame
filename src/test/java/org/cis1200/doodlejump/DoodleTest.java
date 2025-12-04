package org.cis1200.doodlejump;

import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class DoodleTest {

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

    @Test
    public void testBulletDetectsBeingOutOfBounds() {
        Bullet bullet = new Bullet(20, 20, 30000, 30000);
        assertFalse(bullet.isOutOfBounds());

        bullet.setPx(4000000);
        assertTrue(bullet.isOutOfBounds());

        bullet.setPx(20);
        assertFalse(bullet.isOutOfBounds());

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
        assertEquals(-25, player.getVy());
    }
}
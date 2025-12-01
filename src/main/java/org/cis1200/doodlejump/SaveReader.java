package org.cis1200.doodlejump;

import org.cis1200.Game;

import java.util.LinkedList;
import java.util.List;

public class SaveReader {

    private final static char COMMA = ',';

    public static LinkedList<Integer> parse(String line) {
        LinkedList<Integer> integerList = new LinkedList<Integer>();

        if (line == null) {
            throw new IllegalArgumentException();
        }

        boolean insideQuotes = false;
        char[] chars = line.toCharArray();
        List<String> result = new LinkedList<>();
        StringBuilder acc = new StringBuilder();

        if (chars.length == 0) {
            return integerList;
        }

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == COMMA) {
                integerList.add(Integer.parseInt(acc.toString()));
                acc = new StringBuilder();
            } else {
                acc.append(ch);
            }
            if (i == chars.length - 1) {
                integerList.add(Integer.parseInt(acc.toString()));
                acc = new StringBuilder();
            }
        }
        return integerList;
    }

    public static Player loadPlayer(String line) {
        if (line == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Integer> inputs = parse(line);
        if (inputs.size() != 4) {
            throw new IllegalArgumentException("Invalid Size of Inputs");
        }
        int px = inputs.removeFirst();
        int py = inputs.removeFirst();
        int vx = inputs.removeFirst();
        int vy = inputs.removeFirst();

        return new Player(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
    }

    public static Platform loadPlatform(String line) {
        if (line == null) {
            throw new IllegalArgumentException();
        }

        if (line.equals("null")) {
            return null;
        }

        LinkedList<Integer> inputs = parse(line);
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException("Invalid Size of Inputs");
        }
        if ((inputs.size() != 5 && inputs.getFirst() != 2) | (inputs.size() != 6 && inputs.getFirst() == 2)
                | (inputs.size() != 6 && inputs.getFirst() == 3)) {
            throw new IllegalArgumentException("Invalid Size of Inputs");
        }
        int type = inputs.removeFirst();
        int px = inputs.removeFirst();
        int py = inputs.removeFirst();
        int vx = inputs.removeFirst();
        int vy = inputs.removeFirst();

        int state = -1;
        if (type == 2) {
            state = inputs.removeFirst();
            if (state != 1 && state != 0) {
                throw new IllegalArgumentException("Invalid State for Weak Platform");
            }
        }

        if (type == 3) {
            state = inputs.removeFirst();
        }

        return switch (type) {
            case 0 -> new RegularPlatform(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
            case 1 -> new BouncyPlatform(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
            case 2 -> new WeakPlatform(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT, state);
            case 3 -> new DisappearingPlatform(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT, state);
            case 4 -> new MovingPlatform(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
            default -> throw new IllegalArgumentException("Invalid Type of Platform");
        };
    }

    public static Monster loadMonster(String line, Player player) {
        if (line == null) {
            throw new IllegalArgumentException();
        }

        LinkedList<Integer> inputs = parse(line);
        if (inputs.size() != 6) {
            throw new IllegalArgumentException("Invalid Size of Inputs");
        }

        int type = inputs.removeFirst();
        int px = inputs.removeFirst();
        int py = inputs.removeFirst();
        int vx = inputs.removeFirst();
        int vy = inputs.removeFirst();
        int hp = inputs.removeFirst();

        return switch (type) {
            case 0 -> new RegularMonster(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT, hp);
            case 1 -> new MovingMonster(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT, hp);
            case 2 -> new HomingMonster(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT, hp, player);
            default -> throw new IllegalArgumentException("Invalid Type of Monster");
        };
    }

    public static Bullet loadBullet(String line) {
        if (line != null) {
            LinkedList<Integer> inputs;
            inputs = parse(line);
            if (inputs.size() != 4) {
                throw new IllegalArgumentException("Invalid Size of Inputs");
            }

            int px = inputs.removeFirst();
            int py = inputs.removeFirst();
            int vx = inputs.removeFirst();
            int vy = inputs.removeFirst();

            return new Bullet(px, py, vx, vy, GameRegion.COURT_WIDTH, GameRegion.COURT_HEIGHT);
        } else {
            throw new IllegalArgumentException();
        }
    }
}

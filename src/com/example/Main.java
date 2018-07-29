package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static int WIDTH;
    private static int HEIGHT;

    public static void main(String[] args) throws InterruptedException, IOException {

        List<String> list = Files.readAllLines(Paths.get("Kok's Galaxy.txt"));
        HEIGHT = list.size();
        WIDTH = list.get(0).length();

        System.out.println(HEIGHT);
        System.out.println(WIDTH);

        boolean[][] current = init(list);

        view(current);

        while (true) {

            boolean[][] next = nextGenerate(current);

            if (!isChange(current, next)) {
                System.exit(0);
            }

            Thread.sleep(500);

            System.out.println("------------------------");

            view(next);

            current = next;
        }

    }

    /**
     * テキストファイルから初期状態を生成
     *
     * @param list
     * @return
     */
    private static boolean[][] init(List<String> list) {

        boolean[][] current = new boolean[HEIGHT][WIDTH];

        for (int y = 0; y < list.size(); y++) {
            char[] chars = list.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                char c = chars[x];
                if (c == '■') {
                    current[y][x] = true;
                } else {
                    current[y][x] = false;
                }
            }
        }
        return current;
    }

    /**
     * 次世代
     *
     * @param current
     * @return
     */
    private static boolean[][] nextGenerate(boolean[][] current) {
        boolean[][] next = new boolean[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int count = counter(current, y, x);
                if (count <= 1) {
                    next[y][x] = false;
                } else if (2 <= count && count <= 3 && current[y][x]) {
                    next[y][x] = true;
                } else if (count == 3 && !current[y][x]) {
                    next[y][x] = true;
                } else if (4 <= count) {
                    next[y][x] = false;
                }
            }
        }
        return next;
    }

    /**
     * 標準出力に表示
     *
     * @param world
     */
    private static void view(boolean[][] world) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                boolean cell = world[y][x];
                if (cell) {
                    System.out.print("■");
                } else {
                    System.out.print("□");
                }
            }
            System.out.println();
        }
    }

    /**
     * 近傍のセルの中から生きたセルの数を返す
     *
     * @param stage
     * @param y
     * @param x
     * @return
     */
    private static int counter(boolean[][] stage, int y, int x) {
        int count = 0;
        if (get(stage, y - 1, x - 1)) count++;
        if (get(stage, y - 1, x)) count++;
        if (get(stage, y - 1, x + 1)) count++;

        if (get(stage, y, x - 1)) count++;
        if (get(stage, y, x + 1)) count++;

        if (get(stage, y + 1, x - 1)) count++;
        if (get(stage, y + 1, x)) count++;
        if (get(stage, y + 1, x + 1)) count++;
        return count;
    }

    /**
     * 特定の座標の生死を返す。範囲外だったら死として扱う
     *
     * @param stage
     * @param x
     * @param y
     * @return
     */
    private static boolean get(boolean[][] stage, int y, int x) {

        if (y < 0 || y >= HEIGHT) {
            return false;
        }

        if (x < 0 || x >= WIDTH) {
            return false;
        }


        return stage[y][x];
    }

    /**
     * 変更があったかどうか
     *
     * @param stage
     * @param next
     * @return
     */
    private static final boolean isChange(boolean[][] stage, boolean[][] next) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (stage[y][x] != next[y][x]) {
                    return true;
                }
            }
        }
        return false;
    }
}

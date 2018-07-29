package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static int WIDTH;
    private static int HEIGHT;

    public static void main(String[] args) throws InterruptedException, IOException {

        List<String> list = Files.readAllLines(Paths.get("input.txt"));
        HEIGHT = list.size();
        WIDTH = list.get(0).length();

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
     * @param list
     * @return
     */
    private static boolean[][] init(List<String> list) {

        boolean[][] current = new boolean[HEIGHT][WIDTH];

        for (int i = 0; i < list.size(); i++) {
            char[] aa = list.get(i).toCharArray();
            for (int l = 0; l < aa.length; l++) {
                char c = aa[l];
                if (c == '■') {
                    current[i][l] = true;
                } else {
                    current[i][l] = false;
                }
            }
        }
        return current;
    }

    /**
     * 次世代
     * @param current
     * @return
     */
    private static boolean[][] nextGenerate(boolean[][] current) {
        boolean[][] next = new boolean[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int l = 0; l < WIDTH; l++) {
                int count = counter(current, i, l);
                if (count <= 1) {
                    next[i][l] = false;
                }
                if (2 <= count && count <= 3 && current[i][l]) {
                    next[i][l] = true;
                }

                if (count == 3 && !current[i][l]) {
                    next[i][l] = true;
                }
                if (4 <= count) {
                    next[i][l] = true;
                }
            }
        }
        return next;
    }

    /**
     * 標準出力に表示
     * @param next
     */
    private static void view(boolean[][] next) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int l = 0; l < WIDTH; l++) {
                boolean cell = next[i][l];
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
     * @param stage
     * @param x
     * @param y
     * @return
     */
    private static int counter(boolean[][] stage, int x, int y) {
        int count = 0;
        if (get(stage, x - 1, y - 1)) count++;
        if (get(stage, x, y - 1)) count++;
        if (get(stage, x + 1, y - 1)) count++;
        if (get(stage, x - 1, y)) count++;
        if (get(stage, x + 1, y)) count++;
        if (get(stage, x - 1, y + 1)) count++;
        if (get(stage, x, y + 1)) count++;
        if (get(stage, x + 1, y + 1)) count++;
        return count;
    }

    /**
     * 特定の座標の生死を返す。範囲外だったら死として扱う
     * @param stage
     * @param x
     * @param y
     * @return
     */
    private static boolean get(boolean[][] stage, int x, int y) {
        if (x < 0 || x >= WIDTH) {
            return false;
        }

        if (y < 0 || y >= HEIGHT) {
            return false;
        }

        return stage[x][y];
    }

    /**
     * 変更があったかどうか
     * @param stage
     * @param next
     * @return
     */
    private static final boolean isChange(boolean[][] stage, boolean[][] next) {
        for (int i = 0; i < HEIGHT; i++) {
            for (int l = 0; l < WIDTH; l++) {
                if (stage[i][l] != next[i][l]) {
                    return true;
                }
            }
        }
        return false;
    }
}

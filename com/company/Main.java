package com.company;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Main {

    // Maze dimensions
    static final int m = 10 ; // rows
    static final int n = 9 ; // columns
    
    static final Color BEAD_BACKGROUND = new Color(224, 207, 137);
    static final Color BEAD_BACKGROUND_HIGHLIGHTED = new Color(0, 207, 137);

    static final Color WALL_BACKGROUND_HIGHLIGHTED = new Color(0, 207, 137);
    static final Color WALL_BACKGROUND_PLACED = new Color(0, 200, 137);

    static State[][] states = new State[m][n];

    static JLabel[][] beads = new JLabel[9][9];
    static JLabel[][] WALLXICON = new JLabel[9][9];
    static JLabel[][] WALLYICON = new JLabel[9][9];

    static int[][] WALLX = new int[9][9];
    static int[][] WALLY = new int[9][9];
    static JFrame board;

    public static void main(String[] beans) {
        generateBoard();

    }

    static void generateBoard() {
        board = new JFrame();
        //frame set up
        board.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        board.setBackground(new java.awt.Color(255, 255, 153));
        board.setMinimumSize(new java.awt.Dimension(720, 830));
        board.setPreferredSize(new java.awt.Dimension(720, 830));
        board.setResizable(false);
        board.setSize(new java.awt.Dimension(720, 830));
        board.setLayout(null);
        board.setLocationRelativeTo(null);
        board.setVisible(true);

        // board.add(PLAYER2.icon);
        // اضافه کردن خانه ها
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                beads[i][j] = new JLabel();
                beads[i][j].setVisible(true);
                beads[i][j].setSize(60, 60);
                beads[i][j].setBackground(new Color(224, 207, 137));
                beads[i][j].setOpaque(true);
                beads[i][j].setBorder(javax.swing.BorderFactory.createEtchedBorder());
                beads[i][j].setLocation(80 * j, 80 * i);
                board.add(beads[i][j]);
                beads[i][j].repaint();
            }
        }

        //setting up walls trace
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                WALLYICON[i][j] = new JLabel();
                WALLXICON[i][j] = new JLabel();
                board.add(WALLYICON[i][j]);
                board.add(WALLXICON[i][j]);
                WALLY[i][j] = 0;
                WALLX[i][j] = 0;

            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // جایگذاری رد دیوارهای عمودی

                WALLYICON[i][j].setVisible(true);
                WALLYICON[i][j].setSize(20, 60);
                WALLYICON[i][j].setOpaque(true);
                // WALLYICON[i][j].setBorder(javax.swing.BorderFactory.createEtchedBorder());
                WALLYICON[i][j].setLocation(80 * j + 60, 80 * i);
                int wall_x = j;
                int wall_y = i;

                WALLYICON[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        // اگه دیوار قبلا قرار داده شده باشه ریترن میکنیم
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }

                        WALLY[wall_y][wall_x] = 1;
                        placeWall(wall, wall_x, wall_y, WALL_BACKGROUND_PLACED);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }
                        wall.setBackground(WALL_BACKGROUND_HIGHLIGHTED);
                        // wall.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }
                        wall.setBackground(null);
                        //  wall.repaint();

                    }

                });
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // جایگذاری رد دیوارهای افقی
                WALLXICON[i][j].setVisible(true);
                WALLXICON[i][j].setSize(60, 20);
                WALLXICON[i][j].setOpaque(true);
                WALLXICON[i][j].setBackground(null);
                WALLXICON[i][j].setLocation(80 * j, 80 * i + 60);
                int wall_x = j;
                int wall_y = i;
                WALLXICON[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        wall.setVisible(true);
                        // اگه دیوار قبلا قرار داده شده باشه ریترن میکنیم
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }

                        wall.setVisible(true);
                        wall.repaint();
                        WALLX[wall_y][wall_x] = 1;
                        System.out.println(wall_x + " " + wall_y);
                        placeWall(wall, wall_x, wall_y, WALL_BACKGROUND_PLACED);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }
                        wall.setBackground(WALL_BACKGROUND_HIGHLIGHTED);
                        wall.setVisible(true);
                        wall.repaint();

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }
                        wall.setBackground(null);

                    }

                });
            }
        }

    }

    static void placeWall(JLabel wall, int X, int Y, Color c) {
        if (iswallValid()) {
            wall.setBackground(c);
        }
        //....
        // بقیه تغییرات مربوط به جایگذاری دیوار اعمال بشه 

    }

    static boolean iswallValid() {
        //....
        // شرط قرارگیری دیوار چک بشه

        return true;
    }

    static void playAgain() {
        board.dispose();
        // اجرای دوباره بازی
        //new Main();
    }

}

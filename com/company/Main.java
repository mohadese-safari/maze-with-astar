package com.company;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class Main {

    enum BoardStatus {

        NOTHING_PLACED, START_PLACED, GOAL_PLACED
    }

    static BoardStatus status;
    static String beadText = "START";
    static State initial;/* = new State(null, 2, 3, 0);*/

    static State goal;/* = new State(null, 2, 3, 0);*/

    static ArrayList goalPath = new ArrayList(); // از پدران هدف به سمت ریشه حرکت میکنیم

    // Maze dimensions
    static final int m = 10; // rows
    static final int n = 10; // columns

    static boolean goalFound = false;
    private static PQueue frontier = new PQueue(m * n * 2000);
    private static PQueue closedList = new PQueue(m * n * 2000);

    static final Color BEAD_BACKGROUND = new Color(226, 240, 217);
    static final Color BEAD_BACKGROUND_HIGHLIGHTED = new Color(0, 207, 137);

    static final Color WALL_BACKGROUND_HIGHLIGHTED = new Color(199, 233, 183);
    static final Color WALL_BACKGROUND_PLACED = new Color(0, 200, 137);

    static final Color PATH_HIGHLIGHTED = new Color(52, 198, 184);

    static State[][] states = new State[m][n];

    static JLabel[][] beads = new JLabel[m][n];
    static JLabel[][] WALLXICON = new JLabel[m - 1][n];
    static JLabel[][] WALLYICON = new JLabel[m][n - 1];

    static WallX[][] WALLX = new WallX[m - 1][n];
    static WallY[][] WALLY = new WallY[m][n - 1];
    static JFrame board;

    public static void main(String[] beans) {
        status = BoardStatus.NOTHING_PLACED;
        generateBoard();

    }

    static State runAStar() {
        frontier.enqueue(initial);
        State current;
        while (!frontier.isEmpty()) {
            current = frontier.dequeue();

            System.out.println(current);

            current.expandChildren();
            System.out.println(frontier);
            for (State s : current.getChildren()) {
                if (s.matchGoal()) {
                    return s;
                }
                s.addIfWorthy();
            }
            System.out.println(frontier.getSize());
            closedList.enqueue(current);
        }
        return null;
    }

    public static void highlightPath(State goal) {
        if (goal == null) {
            System.out.println("Goal cannot be reached");
            JOptionPane.showMessageDialog(null, "Goal cannot be reached", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        State s = goal;
        System.out.print("path : ");
        while (s != initial) {
            beads[s.getRow()][s.getCol()].setBackground(PATH_HIGHLIGHTED);
            System.out.print(s + " -> ");
            s = s.getParent();
        }
        beads[s.getRow()][s.getCol()].setBackground(PATH_HIGHLIGHTED);
        System.out.println(initial);
    }

    static void generateBoard() {
        board = new JFrame();
        //frame set up
        board.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        board.setBackground(new java.awt.Color(255, 255, 153));
        board.setMinimumSize(new java.awt.Dimension(800, 860));
        board.setPreferredSize(new java.awt.Dimension(800, 860));
        board.setResizable(false);
        board.setSize(new java.awt.Dimension(800, 860));
        board.setLayout(null);
        board.setLocationRelativeTo(null);
        board.setVisible(true);

        //Add action button
        JButton action = new JButton();
        action.setVisible(true);
        action.setSize(100, 30);
        action.setText("Run A*");
        action.setOpaque(true);
        action.setLocation(310, 800);
        board.add(action);
        action.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                State result = runAStar();
                highlightPath(result);
            }
        });

        // board.add(PLAYER2.icon);
        // اضافه کردن خانه ها
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                beads[i][j] = new JLabel();
                beads[i][j].setVisible(true);
                beads[i][j].setSize(60, 60);
                beads[i][j].setBackground(BEAD_BACKGROUND);
                beads[i][j].setOpaque(true);
                beads[i][j].setBorder(javax.swing.BorderFactory.createEtchedBorder());
                beads[i][j].setLocation(80 * j, 80 * i);
                beads[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                board.add(beads[i][j]);
                beads[i][j].repaint();
                int row = i;
                int col = j;

                beads[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        beads[row][col].removeMouseListener(this);
                        if (status == BoardStatus.NOTHING_PLACED) {
                            beadText = "GOAL";
                            initial = new State(null, row, col, 0);
                            status = BoardStatus.START_PLACED;
                        } else if (status == BoardStatus.START_PLACED) {
                            status = BoardStatus.GOAL_PLACED;
                            goal = new State(null, row, col, 0);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (status == BoardStatus.GOAL_PLACED) {
                            beads[row][col].removeMouseListener(this);
                        } else {
                            beads[row][col].setText(beadText);
                        }

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        beads[row][col].setText("");
                    }
                });

            }
        }

        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                WALLXICON[i][j] = new JLabel();
                WALLX[i][j] = new WallX(i, j);
                board.add(WALLXICON[i][j]);
            }
        }

        //setting up walls trace
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                WALLYICON[i][j] = new JLabel();
                WALLY[i][j] = new WallY(i, j);
                board.add(WALLYICON[i][j]);
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                // جایگذاری رد دیوارهای عمودی

                WALLYICON[i][j].setVisible(true);
                WALLYICON[i][j].setSize(20, 60);
                WALLYICON[i][j].setOpaque(true);
                // WALLYICON[i][j].setBorder(javax.swing.BorderFactory.createEtchedBorder());
                WALLYICON[i][j].setLocation(80 * j + 60, 80 * i);
                int wall_x = i;
                int wall_y = j;

                WALLYICON[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel wall = (JLabel) e.getSource();
                        // اگه دیوار قبلا قرار داده شده باشه ریترن میکنیم
                        if (wall.getBackground().equals(WALL_BACKGROUND_PLACED)) {
                            return;
                        }

                        WALLY[wall_x][wall_y].setPlaced(true);
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

        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                // جایگذاری رد دیوارهای افقی
                WALLXICON[i][j].setVisible(true);
                WALLXICON[i][j].setSize(60, 20);
                WALLXICON[i][j].setOpaque(true);
                WALLXICON[i][j].setBackground(null);
                WALLXICON[i][j].setLocation(80 * j, 80 * i + 60);
                int wall_x = i;
                int wall_y = j;
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
                        WALLX[wall_x][wall_y].setPlaced(true);
                        //System.out.println(wall_x + " " + wall_y);
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
        wall.setBackground(c);
    }

    static void playAgain() {
        board.dispose();
        // اجرای دوباره بازی
        //new Main();
    }

    public static State getInitial() {
        return initial;
    }

    public static void setInitial(State initial) {
        Main.initial = initial;
    }

    public static State getGoal() {
        return goal;
    }

    public static void setGoal(State goal) {
        Main.goal = goal;
    }

    public static State[][] getStates() {
        return states;
    }

    public static void setStates(State[][] states) {
        Main.states = states;
    }

    public static PQueue getFrontier() {
        return frontier;
    }

    public static void setFrontier(PQueue aFrontier) {
        frontier = aFrontier;
    }

    public static PQueue getClosedList() {
        return closedList;
    }

    public static void setClosedList(PQueue aClosedList) {
        closedList = aClosedList;
    }

}

package com.company;

import java.util.HashSet;
import java.util.Set;

public class State {

    private Set<State> children;
    private State parent;
    private int row;
    private int col;
    private int g;

    public State(State parent, int row, int col, int g) {
        this.parent = parent;
        this.row = row;
        this.col = col;
        this.g = g;
    }

    public Set<State> getChildren() {
        return children;
    }

    public void setChildren(Set<State> children) {
        this.children = children;
    }

    public State getParent() {
        return parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    void expandChildren() {
        // شرط اضافه شدن به فرانتیر نبود مانع و تکراری نبودن گره است
        children = new HashSet<State>();
        try {
            if (!Main.WALLY[row][col - 1].placed()) {
                children.add(new State(this, row, col - 1, g + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        try {
            if (!Main.WALLY[row][col].placed()) {
                children.add(new State(this, row, col + 1, g + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        try {
            if (!Main.WALLX[row - 1][col].placed()) {
                children.add(new State(this, row - 1, col, g + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        try {
            if (!Main.WALLX[row][col].placed()) {
                children.add(new State(this, row + 1, col, g + 1));
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
    }

    public String toString() {
        return "(" + row + "," + col + ")";
    }

    // compute manhattan heuristic
    int getH() {
        return Math.abs(row - Main.goal.row) + Math.abs(col - Main.goal.col);
    }

    int getF() {
        return g + getH();
    }

    public boolean worthAdding() {
        PQueue frontier = Main.getFrontier();
        PQueue closedList = Main.getClosedList();
        State sameOnFrontier = frontier.getSamePositionSuccessor(this);
        State sameOnClosed = closedList.getSamePositionSuccessor(this);

        if (sameOnFrontier != null && sameOnFrontier.getF() <= this.getF()) {
            return false;
        } else if (sameOnClosed != null && sameOnClosed.getF() <= this.getF()) {
            return false;
        }
        // Otherwise
        return true;
    }

    public boolean matchGoal() {
        State goal = Main.getGoal();
        if (row == goal.getRow() && col == goal.getCol()) {
            return true;
        }
        return false;
    }

    public void addIfWorthy() {
        if (worthAdding()) {
            Main.getFrontier().enqueue(this);
        }
    }
}

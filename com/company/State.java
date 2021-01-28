package com.company;

import java.util.HashSet;
import java.util.Set;

public class State {

    private Set<State> children;
    private State parent;
    private int row;
    private int col;
    private int g;

    public State(State parent, int x, int y, int g) {
        this.parent = parent;
        this.row = x;
        this.col = y;
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
        children = new HashSet<State>();

        try {
            if (!Main.WALLY[row][col - 1].placed()) {
                children.add(Main.getStates()[row][col - 1]);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        try {
            if (!Main.WALLY[row][col].placed()) {
                children.add(Main.getStates()[row][col + 1]);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        try {
            if (!Main.WALLX[row - 1][col].placed()) {
                children.add(Main.getStates()[row - 1][col]);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

        try {
            if (!Main.WALLX[row][col].placed()) {
                children.add(Main.getStates()[row + 1][col]);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
        }

    }

    // compute manhattan heuristic
    int getH() {
        return Math.abs(row - Main.goal.row) + Math.abs(col - Main.goal.col);
    }

    int getF() {
        return g + getH();
    }

    /*
     State getBestSuccessor(){
     if()
     for(State s : children){
            
     }
     }
     */
}

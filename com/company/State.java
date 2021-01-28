package com.company;
import java.util.HashSet;
import java.util.Set;

public class State {
    private Set<State> children;
    private State parent ;
    private int x ;
    private int y ;

    public State(State parent, int x, int y) {
        this.parent = parent;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
    
    
}

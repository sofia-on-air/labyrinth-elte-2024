/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.util.ArrayList;
/**
 * Represents a position in a 2D space.
 */
public class Position {
    public int x, y;
    /**
     * Creates a new position with the given coordinates.
     * 
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }    
    /**
     * Moves the position in a specific direction.
     * 
     * @param d The direction to move.
     * @return A new position after moving.
     */
    public Position translate(Direction d){
        return new Position(x + d.x, y + d.y);
    }
    /**
     * Converts the position to a string for display.
     * 
     * @return A string representation of the position.
     */
    @Override
    public String toString() {
        return "Position{x=" + x + ", y=" + y + "}";
    }
    /**
     * Checks if this position is equal to another object.
     * 
     * @param obj The object to compare with.
     * @return True if the positions are the same, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }
    /**
     * Checks if a list of positions contains a specific position.
     * 
     * @param walls The list of positions to check.
     * @param pos The position to find.
     * @return True if the position is in the list, false otherwise.
     */
    public boolean contains(ArrayList<Position> walls, Position  pos){
        for (Position wall : walls) {
            if (wall.equals(pos)) {
                return true;
            }
        }
        return false;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

/**
 * Enum representing four directions (UP, DOWN, LEFT, RIGHT).
 */
public enum Direction {
    
    // Enum constants with associated x, y values
    DOWN(0, 1), LEFT(-1, 0), UP(0, -1), RIGHT(1, 0);
    
    // Coordinates representing the movement in the x and y
    public final int x, y;

    /**
     * Constructor for the Direction enum.
     * 
     * @param x The change in the x-coordinate when moving in this direction.
     * @param y The change in the y-coordinate when moving in this direction.
     */
    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.util.Objects;

/**
 * The GameID class represents the unique identifier for a game level
 */
public class GameID {
    
    public final String difficulty;  // The difficulty of the game
    public final int level;          // The level number of the game

    /**
     * Constructor that initializes a GameID with the given difficulty and level.
     * 
     * @param difficulty The difficulty of the game.
     * @param level The level number.
     */
    public GameID(String difficulty, int level) {
        this.difficulty = difficulty;
        this.level = level;
    }

    /**
     * Returns the hash code of the GameID object.
     * 
     * @return The hash code of this GameID object.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.difficulty);
        hash = 29 * hash + this.level;
        return hash;
    }

    /**
     * Compares this GameID object with another object for equality.
     * 
     * @param obj The object to compare with.
     * @return true if this GameID is equal to the other object; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameID other = (GameID) obj;
        if (this.level != other.level) {
            return false;
        }
        if (!Objects.equals(this.difficulty, other.difficulty)) {
            return false;
        }
        return true;
    }
}

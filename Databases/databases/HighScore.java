/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package databases;
import databases.HighScore;


public class HighScore {
    public final int score;
    public final String name;
    
    /**
     * Constructor to initialize a high score and player's name.
     * 
     * @param name the name of the player.
     * @param score the score achieved by the player.
     */
    public HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }
    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for score
    public int getScore() {
        return score;
    }
    // String representation
    @Override
    public String toString() {
        return name + "-" + score;
    }
}

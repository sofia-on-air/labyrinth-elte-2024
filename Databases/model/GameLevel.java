/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.Direction;
import view.Board;

/**
 * Represents a game level.
 */
public class GameLevel {
    public final GameID        gameID;
    public final int           rows, cols;
    public final LevelItem[][] level;
    public Position            player = new Position(0, 0);
    public Position            player1 = new Position(0, 0);
    private int                numSteps;
    public Position dragon;
    public Position wall;
    public ArrayList<Position> walls;
    private Board board;
    public Position            destination = null;  
    private Direction curDirection;
    /**
     * Creates a game level with the given rows and ID.
     * 
     * @param gameLevelRows Rows of the level
     * @param gameID Level ID
     */
    public GameLevel(ArrayList<String> gameLevelRows, GameID gameID){
        this.gameID = gameID;
        this.board = board;
        this.destination = destination;
        int c = 0;
        for (String s : gameLevelRows) if (s.length() > c) c = s.length();
        rows = gameLevelRows.size();
        cols = c;
        level = new LevelItem[rows][cols];
        numSteps = 0;
        dragon = generateDragonPos();
        walls = new ArrayList<>();
        curDirection = Direction.DOWN;
        
        
        for (int i = 0; i < rows; i++){
            String s = gameLevelRows.get(i);
            for (int j = 0; j < s.length(); j++){
                switch (s.charAt(j)){
                    case '@': player = new Position(j, i);
                              player1 = new Position(j, i); 
                              level[i][j] = LevelItem.EMPTY; break;
                    case '#': level[i][j] = LevelItem.WALL; break;
                    case '.': destination = new Position(j, i);
                              level[i][j] = LevelItem.EMPTY; break;
                    default:  level[i][j] = LevelItem.EMPTY; break;
                }
            }
            for (int j = s.length(); j < cols; j++){
                level[i][j] = LevelItem.EMPTY;
            }
        }
    }
    
    /**
     * Copies another game level.
     * 
     * @param gl The level to copy
     */
    public GameLevel(GameLevel gl) {
        gameID = gl.gameID;
        rows = gl.rows;
        cols = gl.cols;
        numSteps = gl.numSteps;
        destination = gl.destination;
        level = new LevelItem[rows][cols];
        player = new Position(gl.player.x, gl.player.y);
        dragon = generateDragonPos();
        for (int i = 0; i < rows; i++){
            System.arraycopy(gl.level[i], 0, level[i], 0, cols);
        }
        System.out.println(destination.x);
        System.out.println(destination.y);
    }
    /**
     * Finds a random position for the dragon.
     * 
     * @return Dragon position
     */
    private Position generateDragonPos() { 
    Random rand = new Random();
    Position randomPos;
    int x, y;

    do {
        x = rand.nextInt(cols);
        y = rand.nextInt(rows);
        randomPos = new Position(x, y);
        
        } while ((randomPos.equals(player))
            || (randomPos.equals(destination))
            || (isWall(randomPos))); 
    level[randomPos.y][randomPos.x] = LevelItem.DRAGON;

    return randomPos;
}

    /**
     * Moves the dragon.
     */
    public void moveDragon() {
        

        while (true) {
            Position nextPosition = dragon.translate(curDirection);

            if (isFree(nextPosition)) {
                level[dragon.y][dragon.x] = LevelItem.EMPTY;
                level[nextPosition.y][nextPosition.x] = LevelItem.DRAGON;
                dragon = nextPosition; 
                break;} else {
                chooseRandomDirection();
            }
        }
    }
    /**
     * Creates random direction.
     */
    private void chooseRandomDirection() {
        Direction[] directions = Direction.values();
        Random rand = new Random();
        curDirection = directions[rand.nextInt(directions.length)];
    }
    /**
     * Checks if a position is valid.
     * 
     * @param p The position
     * @return true if valid
     */
    public boolean isValidPosition(Position p){
        return (p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows);
    }
    /**
     * Checks if a position is free.
     * 
     * @param p The position
     * @return true if free
     */
    public boolean isFree(Position p){
        if (!isValidPosition(p)) return false;
        LevelItem li = level[p.y][p.x];
        return (li == LevelItem.EMPTY || li == LevelItem.DESTINATION);
    }
    /**
     * Checks if wall.
     * 
     * @param p The position
     * @return true if wall
     */
    
    public boolean isWall(Position p) {
    if (!isValidPosition(p)) return false;
    LevelItem li = level[p.y][p.x];
    return li == LevelItem.WALL;
}

    /**
     * Moves the player.
     * 
     * @param d The direction
     * @return true if moved
     */
    public boolean movePlayer(Direction d){
        Position curr = player;
        Position next = curr.translate(d);
        if (isFree(next)) {
            player = next;
            numSteps++;
            moveDragon();
            return true;
        } 
        return false;
    }
    
    /**
     * Prints the level.
     */
    public void printLevel(){
        int x = player.x, y = player.y;
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                if (i == y && j == x)
                    System.out.print('@');
                else 
                    System.out.print(level[i][j].representation);
            }
            System.out.println("");
        }
    }
    /**
     * Resets the level.
     */
    public void reset() {
        player = player1; 
    
        numSteps = 0;

        if (dragon != null) {
            level[dragon.y][dragon.x] = LevelItem.EMPTY;
    }

    dragon = generateDragonPos();}
    /**
     * Checks if near the dragon.
     * 
     * @param i Row
     * @param j Column
     * @return true if near
     */
    public boolean isNeighbours(int i, int j) {
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int nRow = i + row;
                int nCol = j + col;
                if (nRow == dragon.x && nCol == dragon.y){
                    return true;
                }
            }
        }return false;
    }
    /**
     * Gets the number of steps.
     * 
     * @return Steps
     */
    public int getNumSteps() {
        return numSteps;
    }   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import res.ResourceLoader;

/**
 * The Game class manages the game's levels, loads them from files.
 */
public class Game {
    
    // Stores game levels by difficulty and level number.
    private final HashMap<String, HashMap<Integer, GameLevel>> gameLevels;
    private GameLevel gameLevel = null;
    
    /**
     * Constructor that initializes the game and loads levels.
     */
    public Game() {
        gameLevels = new HashMap<>();
        readLevels();
    }

    /**
     * Loads a game level based on the provided GameID.
     * 
     * @param gameID The GameID specifying the difficulty and level to load.
     */
    public void loadGame(GameID gameID) {
        HashMap<Integer, GameLevel> levelsForDifficulty = gameLevels.get(gameID.difficulty);
        if (levelsForDifficulty != null) {
            gameLevel = levelsForDifficulty.get(gameID.level);
        } else {
            System.out.println("Game level not found for difficulty: " + gameID.difficulty);
            return;
        }
    }

    /**
     * Prints the current game level.
     */
    public void printGameLevel() { 
        gameLevel.printLevel(); 
    }
    
    /**
     * Moves the player in the given direction.
     * 
     * @param d The direction to move the player.
     * @return true if the player was successfully moved, false otherwise.
     */
    public boolean step(Direction d) {
        return gameLevel.movePlayer(d);
    }

    
    /**
     * Gets all available game difficulties.
     * 
     * @return A collection of difficulty levels.
     */
    public Collection<String> getDifficulties() { 
        return gameLevels.keySet(); 
    }
    
    /**
     * Gets all levels for a specified difficulty.
     * 
     * @param difficulty The difficulty level to check.
     * @return A collection of level numbers for the given difficulty.
     */
    public Collection<Integer> getLevelsOfDifficulty(String difficulty) {
        if (!gameLevels.containsKey(difficulty)) return null;
        return gameLevels.get(difficulty).keySet();
    }
    
    /**
     * Checks if a game level is loaded.
     * 
     * @return true if a level is loaded, false otherwise.
     */
    public boolean isLevelLoaded() { 
        return gameLevel != null; 
    }

    /**
     * Gets the number of rows in the current level.
     * 
     * @return The number of rows in the current level.
     */
    public int getLevelRows() { 
        return gameLevel.rows; 
    }

    /**
     * Gets the number of columns in the current level.
     * 
     * @return The number of columns in the current level.
     */
    public int getLevelCols() { 
        return gameLevel.cols; 
    }

    /**
     * Gets the current game level.
     * 
     * @return The current game level.
     */
    public GameLevel getLevel() { 
        return gameLevel; 
    }

    /**
     * Gets the item at the position in the current level.
     * 
     * @param row The row index.
     * @param col The column index.
     * @return The item at the specified position.
     */
    public LevelItem getItem(int row, int col) { 
        return gameLevel.level[row][col]; 
    }

    /**
     * Gets the GameID for the current level.
     * 
     * @return The GameID of the current level, or null if no level is loaded.
     */
    public GameID getGameID() { 
        return (gameLevel != null) ? gameLevel.gameID : null; 
    }

    /**
     * Gets the number of steps taken in the level.
     * 
     * @return The number of steps taken in the level.
     */
    public int getNumSteps() { 
        return (gameLevel != null) ? gameLevel.getNumSteps() : 0; 
    }

    /**
     * Gets the current position of the player.
     * 
     * @return A Position object representing the player's current position.
     */
    public Position getPlayerPos() {
        return new Position(gameLevel.player.x, gameLevel.player.y);
    }

    /**
     * Gets the current position of the dragon.
     * 
     * @return A Position object representing the dragon's current position.
     */
    public Position getDragonPos() {
        return new Position(gameLevel.dragon.x, gameLevel.dragon.y);
    }

    /**
     * Gets the current destination position.
     * 
     * @return A Position object representing the destination position.
     */
    public Position getDestinationPos() {
        return new Position(gameLevel.destination.x, gameLevel.destination.y);
    }

    /**
     * Checks if two positions are adjacent to each other.
     * 
     * @param i The row index of the first position.
     * @param j The column index of the first position.
     * @return true if the positions are adjacent, false otherwise.
     */
    public boolean areNeighbours(int i, int j) {
        return (gameLevel != null) ? gameLevel.isNeighbours(i, j) : false;
    }

    /**
     * Loads all the game levels from the resource files.
     */
    private void readLevels() {
        for (int i = 1; i <= 10; i++) {
            String levelFilePath = String.format("res/levels/%d.txt", i);
            InputStream is = ResourceLoader.loadResource(levelFilePath);

            if (is == null) {
                System.out.println("Level file " + levelFilePath + " not found!");
                continue;
            }

            try (Scanner sc = new Scanner(is)) {
                String line = readNextLine(sc);
                ArrayList<String> gameLevelRows = new ArrayList<>();
                
                while (!line.isEmpty()) {
                    GameID id = readGameID(line);
                    if (id == null) return;

                    gameLevelRows.clear();
                    line = readNextLine(sc);

                    while (!line.isEmpty() && line.trim().charAt(0) != ';') {
                        gameLevelRows.add(line);
                        line = readNextLine(sc);
                    }

                    addNewGameLevel(new GameLevel(gameLevelRows, id));
                }
            } catch (Exception e) {
                System.out.println("Error reading level file: " + levelFilePath);
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a new game level to the game.
     * 
     * @param gameLevel The game level to be added.
     */
    private void addNewGameLevel(GameLevel gameLevel) {
        HashMap<Integer, GameLevel> levelsOfDifficulty;
        if (gameLevels.containsKey(gameLevel.gameID.difficulty)) {
            levelsOfDifficulty = gameLevels.get(gameLevel.gameID.difficulty);
            levelsOfDifficulty.put(gameLevel.gameID.level, gameLevel);
        } else {
            levelsOfDifficulty = new HashMap<>();
            levelsOfDifficulty.put(gameLevel.gameID.level, gameLevel);
            gameLevels.put(gameLevel.gameID.difficulty, levelsOfDifficulty);
        }
    }

    /**
     * Reads the next non-empty line from the scanner.
     * 
     * @param sc The scanner to read from.
     * @return The next non-empty line.
     */
    private String readNextLine(Scanner sc) {
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()) {
            line = sc.nextLine();
        }
        return line;
    }

    /**
     * Reads and parses the GameID from a line.
     * 
     * @param line The line to parse the GameID from.
     * @return A GameID object or null if the line is not a valid GameID.
     */
    private GameID readGameID(String line) {
        line = line.trim();
        if (line.isEmpty() || line.charAt(0) != ';') return null;
        Scanner s = new Scanner(line);
        s.next();
        if (!s.hasNext()) return null;
        String difficulty = s.next().toUpperCase();
        if (!s.hasNextInt()) return null;
        int id = s.nextInt();
        return new GameID(difficulty, id);
    }
}

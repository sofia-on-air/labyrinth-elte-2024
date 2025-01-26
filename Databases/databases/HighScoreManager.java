/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The HighScoreManager class is managing high scores in a database.
 */
public class HighScoreManager {

    // Maximum number of scores to store in the database
    int maxScores;
    
    // Prepared statements for inserting and deleting scores
    PreparedStatement insertStatement;
    PreparedStatement deleteStatement;

    // Connection to the database
    Connection connection;

    // List to hold high scores
    private ArrayList<HighScore> highScores = new ArrayList<>();

    /**
     * Constructs a HighScoreManager with database name and maximum number of scores.
     * 
     * @param Name The name of the player.
     * @param maxScores The maximum number of high scores to store in the database.
     * @throws SQLException If there is an error connecting to the database or preparing statements.
     */
    public HighScoreManager(String Name, int maxScores) throws SQLException {
        this.maxScores = maxScores;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "");
        connectionProps.put("serverTimezone", "UTC");
        String dbURL = "jdbc:mysql://localhost:3307/Highscores";
        connection = DriverManager.getConnection(dbURL, connectionProps);

        String deleteQuery = "DELETE FROM HIGHSCORES WHERE SCORE=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    /**
     * Adds or updates a player's high score in the database.
     * @param name The name of the player.
     * @param score The score to be added or updated.
     * @throws SQLException If there is an error accessing the database.
     */
    public void putHighScore(String name, int score) throws SQLException {
        String checkQuery = "SELECT * FROM HIGHSCORES WHERE NAME = ?";
        PreparedStatement selectStatement = connection.prepareStatement(checkQuery);
        selectStatement.setString(1, name);
        ResultSet rs = selectStatement.executeQuery();

        if (rs.next()) {
            String deleteQuery = "DELETE FROM HIGHSCORES WHERE NAME = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setString(1, name);
            deleteStatement.executeUpdate();
        }
        insertScore(name, score);
    }

    /**
     * Loads the top high scores from the database, limited to the top 10.
     * 
     * @return An ArrayList of the top high scores.
     * @throws SQLException If there is an error querying the database.
     */
    public ArrayList<HighScore> loadHighScores() throws SQLException {
        highScores.clear();
        System.out.println("Attempting to load high scores from the database...");
        
        try (Statement stmt = connection.createStatement()) {
            String query = "SELECT * FROM highscores ORDER BY score DESC LIMIT 10";
            System.out.println("Executing query: " + query);
            
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.isBeforeFirst()) {
                System.out.println("No high scores found in the database.");
                return highScores;
            }
            
            while (rs.next()) {
                String name = rs.getString("name");
                int score = rs.getInt("score");
                System.out.println("Record fetched: " + name + " - " + score);
                highScores.add(new HighScore(name, score));
            }
        } catch (SQLException ex) {
            System.out.println("Error while loading high scores: " + ex.getMessage());
            throw ex;
        }
        
        return highScores;
    }

    /**
     * Inserts a new score into the high scores database.
     * 
     * @param name The name of the player.
     * @param score The score to be inserted.
     * @throws SQLException If there is an error executing the insert query.
     */
    public void insertScore(String name, int score) throws SQLException {
        String insertQuery = "INSERT INTO HIGHSCORES (NAME, SCORE) VALUES (?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString(1, name);
        insertStatement.setInt(2, score);
        insertStatement.executeUpdate();
    }
}

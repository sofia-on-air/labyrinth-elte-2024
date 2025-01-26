/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import databases.HighScore;
/**
 * HighScoreTableModel is used to display high scores in a table format.
 */
public class HighScoreTableModel extends AbstractTableModel{
    private final ArrayList<HighScore> highScores;
    private final String[] colName = new String[]{ "Name", "Labyrinths"};
    

    /**
     * Creates a new HighScoreTableModel with the given list of high scores.
     * 
     * @param highScores The list of high scores to display.
     */
    public HighScoreTableModel(ArrayList<HighScore> highScores){
        this.highScores = highScores;
    }
    /**
     * Returns the number of rows in the table.
     * 
     * @return The number of high scores.
     */
    @Override
    public int getRowCount() { return highScores.size(); }
    /**
     * Returns the number of columns in the table.
     * 
     * @return The number of columns.
     */
    @Override
    public int getColumnCount() { return colName.length; }
    /**
     * Returns the value at a specific cell in the table.
     * 
     * @param r The row index.
     * @param c The column index.
     * @return The value at the specified cell.
     */
    @Override
    public Object getValueAt(int r, int c) {
        HighScore h = highScores.get(r);
        if      (c == 0) return h.name;
        else if (c == 1) return h.score;
        return null;
    }
    /**
     * Returns the name of a specific column.
     * 
     * @param i The index of the column.
     * @return The name of the column.
     */
    @Override
    public String getColumnName(int i) {
        return colName[i]; 
    }    
    
}

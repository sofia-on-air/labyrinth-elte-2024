/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.WindowConstants;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import databases.HighScore;

public class HighScoreWindow extends JDialog{
    private final JTable table;
    /**
     * Creates a new HighScoreWindow to display a list of high scores.
     *
     * @param highScores the list of high scores to display.
     * @param parent the parent frame of this dialog.
     */
    public HighScoreWindow(ArrayList<HighScore> highScores, JFrame parent){
        super(parent, true);
        table = new JTable(new HighScoreTableModel(highScores));
        table.setFillsViewportHeight(true);
        
        TableRowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(table.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);
        
        add(new JScrollPane(table));
        setSize(400,400);
        setTitle("Best players");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
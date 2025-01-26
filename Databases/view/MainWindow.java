/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import model.Direction;
import model.Game;
import model.GameID;
import databases.HighScoreManager;
import databases.HighScore;
import java.util.ArrayList;
import java.sql.SQLException;



public class MainWindow extends JFrame {
    private final Game game;
    private Board board;
    private final JLabel gameStatLabel;
    private HighScoreManager highScoreManager;
    private Timer gameTimer;
    private long startTime;
    private long elapsedTime;
    private String playerName;
    private HighScoreManager hs;
    private int counter;
    /**
     * Creates and initializes the main window.
     * Sets up the UI components and game logic.
     *
     * @throws IOException if there is an error loading resources.
     */
    public MainWindow() throws IOException {
        try {
            hs = new HighScoreManager("HighScoresDB", 10);
        } catch (SQLException ex) {
            System.out.println("Error initializing HighScoreManager: " + ex.getMessage());
        }
        game = new Game();

        setTitle("Sokoban");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        URL url = MainWindow.class.getClassLoader().getResource("res/player.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));

        playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Name", JOptionPane.QUESTION_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "noName";
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        JMenu menuGameLevel = new JMenu("Level");
        JMenu menuGameScale = new JMenu("Zoom");
        createGameLevelMenuItems(menuGameLevel);
        createScaleMenuItems(menuGameScale, 1.0, 2.0, 0.5);

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenuItem menuGameRestart = new JMenuItem(new AbstractAction("Restart Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        JMenuItem menuHighScore = new JMenuItem(new AbstractAction("High Scores") { 
            @Override
            public void actionPerformed(ActionEvent e)  { 
                try {
                    displayHighScores(); 
                } catch (SQLException ex) {
                    System.out.println("Database problems");
                }
    } 
});
        menuGame.add(menuGameLevel);
        menuGame.add(menuGameScale);
        menuGame.addSeparator();
        menuGame.add(menuGameRestart);
        menuGame.add(menuGameExit);
        menuGame.add(menuHighScore);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);

        setLayout(new BorderLayout(0, 10));
        gameStatLabel = new JLabel("Steps: 0 | Time: 0s");
        add(gameStatLabel, BorderLayout.NORTH);

        try {
            add(board = new Board(game), BorderLayout.CENTER);
        } catch (IOException ex) {}

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                super.keyPressed(ke);
                if (!game.isLevelLoaded()) return;
                int kk = ke.getKeyCode();
                Direction d = null;
                switch (kk) {
                    case KeyEvent.VK_LEFT:  d = Direction.LEFT; break;
                    case KeyEvent.VK_RIGHT: d = Direction.RIGHT; break;
                    case KeyEvent.VK_UP:    d = Direction.UP; break;
                    case KeyEvent.VK_DOWN:  d = Direction.DOWN; break;
                    case KeyEvent.VK_ESCAPE: game.loadGame(game.getGameID());
                }
                refreshGameStatLabel();
                if (d != null && game.step(d)) {
                    board.repaint();
                    if (game.getDestinationPos().equals(game.getPlayerPos())) {
                        stopTimer();
                        JOptionPane.showMessageDialog(MainWindow.this, "Congratulations!", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                        counter++;
                        game.loadGame(game.getGameID());
                        try {
                            highScoreManager = new HighScoreManager(playerName, 10);
                            highScoreManager.putHighScore(playerName, counter);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        board.refresh();
                        pack();
                    }
                    if (game.areNeighbours(game.getPlayerPos().x, game.getPlayerPos().y)) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Ups, you lost!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                        game.loadGame(game.getGameID());
                        board.refresh();
                        restartGame();
                        pack();
                    }

                }
            }
        });

        setResizable(false);
        setLocationRelativeTo(null);
        game.loadGame(new GameID("EASY", 1));
        board.refresh();
        pack();
        refreshGameStatLabel();
        startTimer();
        setVisible(true);
    }
    /**
     * Restarts the current game by resetting the game state and starting a new timer.
     */
    private void restartGame() {
    stopTimer();
    elapsedTime = 0;
    startTime = System.currentTimeMillis();
    game.getLevel().reset();
    game.loadGame(game.getGameID());
    board.refresh();
    startTimer();
    refreshGameStatLabel();
    pack();
    }
    /**
     * Starts a timer to track the elapsed time during the game.
     */
    private void startTimer() {
        stopTimer();
        startTime = System.currentTimeMillis();
        gameTimer = new Timer(1000, (ActionEvent e) -> {
            elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
            refreshGameStatLabel();
        });
        gameTimer.start();
    }
    /**
     * Stops the timer if it is running and resets the elapsed time.
     */
    private void stopTimer() {
    if (gameTimer != null && gameTimer.isRunning()) {
        gameTimer.stop();
    }
    elapsedTime = 0;
}
    /**
     * Updates the game statistics label to show the current number of steps, elapsed time, and player name.
     */
    private void refreshGameStatLabel() {
        String s = "Steps: " + game.getNumSteps() + " | Time: " + elapsedTime + "s | Player: " + playerName;
        gameStatLabel.setText(s);
    }
    /**
     * Creates menu items for selecting game levels based on difficulty.
     *
     * @param menu the menu to add level items to.
     */

    private void createGameLevelMenuItems(JMenu menu) {
        for (String s : game.getDifficulties()) {
            JMenu difficultyMenu = new JMenu(s);
            menu.add(difficultyMenu);
            for (Integer i : game.getLevelsOfDifficulty(s)) {
                JMenuItem item = new JMenuItem(new AbstractAction("Level-" + i) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        stopTimer();
                        elapsedTime = 0;
                        startTime = System.currentTimeMillis();
                        startTimer(); 
                        game.loadGame(new GameID(s, i));
                        board.refresh();
                        refreshGameStatLabel();
                        pack();
                    }
                });
                difficultyMenu.add(item);
            }
        }
    }
    /**
     * Creates menu items for adjusting the zoom scale of the game board.
     *
     * @param menu the menu to add scale items to.
     * @param from the starting scale value.
     * @param to the ending scale value.
     * @param by the increment value.
     */
    private void createScaleMenuItems(JMenu menu, double from, double to, double by) {
        while (from <= to) {
            final double scale = from;
            JMenuItem item = new JMenuItem(new AbstractAction(from + "x") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.setScale(scale)) pack();
                }
            });
            menu.add(item);

            if (from == to) break;
            from += by;
            if (from > to) from = to;
        }
    }
    /**
     * Displays the high scores in a dialog window.
     *
     * @throws SQLException if there is an error fetching the high scores from the database.
     */
    private void displayHighScores() throws SQLException {
    ArrayList<HighScore> highScores = hs.loadHighScores();

    HighScoreTableModel tableModel = new HighScoreTableModel(highScores);

    JTable table = new JTable(tableModel);
    table.setEnabled(false);

    JScrollPane scrollPane = new JScrollPane(table);

    JDialog dialog = new JDialog(this, "High Scores", true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setSize(400, 300);
    dialog.setLayout(new BorderLayout());
    dialog.add(scrollPane, BorderLayout.CENTER);

    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

    
    /**
     * Main method to start the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {}
    }
}


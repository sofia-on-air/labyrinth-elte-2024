/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;
import java.awt.Color;
/**
 * The Board class is responsible for drawing the game board and its elements.
 */
public class Board extends JPanel {
    private Game game;
    private final Image dragon, destination, player, wall, empty;
    private double scale;
    private int scaled_size;
    private final int tile_size = 32;
    /**
     * Creates a new Board for the given game.
     * 
     * @param g The game to display on the board.
     * @throws IOException If an image resource cannot be loaded.
     */
    public Board(Game g) throws IOException{
        game = g;
        scale = 1.0;
        scaled_size = (int)(scale * tile_size);
        dragon = ResourceLoader.loadImage("res/dragon.png");
        destination = ResourceLoader.loadImage("res/destination.png");
        player = ResourceLoader.loadImage("res/player.png");
        wall = ResourceLoader.loadImage("res/wall.png");
        empty = ResourceLoader.loadImage("res/empty.png");
    }
    /**
     * Sets the scale for the board tiles.
     * 
     * @param scale The new scale factor.
     * @return True if the board was refreshed successfully, false otherwise.
     */
    public boolean setScale(double scale){
        this.scale = scale;
        scaled_size = (int)(scale * tile_size);
        return refresh();
    }
    /**
     * Refreshes the board to adjust its size and redraw the tiles.
     * 
     * @return True if the board was successfully refreshed, false otherwise.
     */
    public boolean refresh(){
        if (!game.isLevelLoaded()) return false;
        Dimension dim = new Dimension(game.getLevelCols() * scaled_size, game.getLevelRows() * scaled_size);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }
    /**
     * Paints the game board and its elements.
     * 
     * @param g The graphics object used for drawing.
     */
    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (!game.isLevelLoaded()) return;
    Graphics2D gr = (Graphics2D)g;
    int w = game.getLevelCols();
    int h = game.getLevelRows();
    Position p = game.getPlayerPos();
    int px = p.x;
    int py = p.y;


    for (int y = py - 3; y <= py + 3; y++) {
        for (int x = px - 3; x <= px + 3; x++) {
            if (y < 0 || y >= h || x < 0 || x >= w) continue;
            
            Image img = null;
            LevelItem li = game.getItem(y, x);
            switch (li) {
                case DRAGON: img = dragon; break;
                case DESTINATION: img = destination; break;
                case WALL: img = wall; break;
                case EMPTY: img = empty; break;
            }
            
            gr.setColor(Color.BLACK);
            gr.fillRect(x * scaled_size, y * scaled_size, scaled_size, scaled_size);
            
            gr.drawImage(img, x * scaled_size, y * scaled_size, scaled_size, scaled_size, null);
        }
    }
    gr.drawImage(player, px * scaled_size, py * scaled_size, scaled_size, scaled_size, null);
}
    
}

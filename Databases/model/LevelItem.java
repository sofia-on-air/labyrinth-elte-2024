/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;
/**
 * Represents items on a game level.
 */
public enum LevelItem {
    PLAYER('@'), DRAGON('*'), DESTINATION('.'), WALL('#'), EMPTY(' ');
    LevelItem(char rep){ representation = rep; }
    public final char representation;
}

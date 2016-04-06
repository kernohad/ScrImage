package edu.gvsu.cis.kernohad.scrimage;

import java.util.ArrayList;

/**
 * Created by Hans Dulimarta on Feb 08, 2016.
 */
public interface NumberPuzzle {
    /**
     * Reset the game logic to handle a board of a given dimension, and
     * randomize the numbers on the board
     *
     * @param height the number of rows in the board
     * @param width the number of columns in the board
     */
     void resizeBoard(int height, int width);

    void scramble();
    /**
     * Move a number into the empty spot
     * @param dir move direction of the number
     *
     * @return two cells affected by the move
     */
    Cell[] moveIntoEmptySpot(SlideDirection dir);

    /**
     *
     * @return an arraylist of Cells. Each cell holds the (row,column) and
     * value of a tile
     */
    ArrayList<Cell> getAllTiles();

    /**
     * Return the current state of the game
     * @return one of the possible values of GameStatus enum
     */
    GameStatus getStatus();
}

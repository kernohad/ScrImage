package edu.gvsu.cis.kernohad.scrimage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by dulimarh on 6/30/14.
 */
public class NumberGame implements NumberPuzzle {
    private int[][] grid;
    private Cell c1, c2;
    private ArrayList<Cell> cells;
    private Random rgen;
    private GameStatus gameStatus;
    private int z_row, z_col;

    public NumberGame() {
        c1 = new Cell();
        c2 = new Cell();
    }

    @Override
    public void resizeBoard (int NROW, int NCOL)
    {
        grid = new int[NROW][NCOL];
        cells = new ArrayList<Cell>();
        rgen = new Random();
        scramble();
    }

    @Override
    public void scramble() {
        ArrayList<Integer> values = new ArrayList<>();
        final int NROW = grid.length;
        final int NCOL = grid[0].length;
        for (int k = 0; k < NROW * NCOL; k++)
            values.add(k);
        Collections.shuffle(values);
        for (int k = 0; k < NROW * NCOL; k++) {
            int row = k / NCOL;
            int col = k % NCOL;
            grid[row][col] = values.get(k);
            if (grid[row][col] == 0) {
                z_row = row;
                z_col = col;
            }
        }

    }

    @Override
    public Cell[] moveIntoEmptySpot(SlideDirection dir)
    {
        boolean moveMade = false;
        int temp;
        c1.row = z_row;
        c1.column = z_col;
        switch (dir) {
            case LEFT:
                if (z_col > 0) {
                    temp = grid[z_row][z_col];
                    grid[z_row][z_col] = grid[z_row][z_col - 1];
                    c1.value = grid[z_row][z_col-1];
                    grid[z_row][z_col-1] = temp;
                    z_col--;
                    moveMade = true;
                }
                break;
            case RIGHT:
                if (z_col < grid[0].length - 1) {
                    temp = grid[z_row][z_col];
                    grid[z_row][z_col] = grid[z_row][z_col+1];
                    c1.value = grid[z_row][z_col + 1];
                    grid[z_row][z_col+1] = temp;
                    z_col++;
                    moveMade = true;
                }
                break;
            case DOWN:
                if (z_row < grid.length - 1) {
                    temp = grid[z_row][z_col];
                    grid[z_row][z_col] = grid[z_row + 1][z_col];
                    c1.value = grid[z_row+1][z_col];
                    grid[z_row+1][z_col] = temp;
                    z_row++;
                    moveMade = true;
                }
                break;
            case UP:
                if (z_row > 0) {
                    temp = grid[z_row][z_col];
                    grid[z_row][z_col] = grid[z_row-1][z_col];
                    c1.value = grid[z_row - 1][z_col];
                    grid[z_row-1][z_col] = temp;
                    z_row--;
                    moveMade = true;
                }
                break;
        }
        if (moveMade) {
            c2.row = z_row;
            c2.column = z_col;
            c2.value = grid[z_row][z_col];
            return new Cell[] {c1, c2};
        }
        else
            return null;
    }

    @Override
    public ArrayList<Cell> getAllTiles()
    {
        cells.clear();
        for (int k = 0; k < grid.length; k++)
            for (int m = 0; m < grid[0].length; m++)
                if (grid[k][m] != 0) {
                    Cell c = new Cell();
                    c.row = k;
                    c.column = m;
                    c.value = grid[k][m];
                    cells.add(c);
                }
        return cells;
    }

    @Override
    public GameStatus getStatus() {
        int expected = 1;
        final int NROW = grid.length;
        final int NCOL = grid[0].length;
        for (int k = 0; k < NROW * NCOL - 1; k++) {
            int r = k / NCOL;
            int c = k % NCOL;
            if (grid[r][c] == expected)
                expected++;
            else
                return GameStatus.IN_PROGRESS;
        }
        return GameStatus.USER_WON;
    }
}

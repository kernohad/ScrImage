package edu.gvsu.cis.kernohad.scrimage;

import java.util.ArrayList;

/**
 * Created by Dylan on 2/26/2016.
 */
public class Presenter implements  IPresenter {


    IView view;
    NumberPuzzle puzzle;
    NumberGame game;


    public Presenter(){

        game = new NumberGame();
        game.resizeBoard(4, 4);
        game.scramble();
    }

    @Override
    public void onSlide(SlideDirection dir) {
        Cell[] cells = game.moveIntoEmptySpot(dir);


        if (cells == null)
            return;

        int r1 = cells[0].row;
        int c1 = cells[0].column;
        int r2 = cells[1].row;
        int c2 = cells[1].column;

        view.swapTiles(r1, c1, r2, c2);
        GameStatus gameStatus = game.getStatus();

        if (gameStatus == GameStatus.USER_WON)
            view.showMessage("Congratulations! You Win! ");
    }

    @Override
    public void onRandomizeTiles() {

        game.scramble();

        int[][] arr = new int[4][4];
        ArrayList<Cell> cells;
        cells = game.getAllTiles();
        int row;
        int col;
        int val;

        for (int k = 0; k < cells.size(); k++){
            row = cells.get(k).row;
            col = cells.get(k).column;
            val = cells.get(k).value;
            arr[row][col] = val;
        }


        view.redrawTiles(arr);
    }

    @Override
    public void onAttachView(IView v) {

        view = v;

        int[][] arr = new int[4][4];
        ArrayList<Cell> cells;
        cells = game.getAllTiles();
        int row;
        int col;
        int val;

        for (int k = 0; k < cells.size(); k++){
            row = cells.get(k).row;
            col = cells.get(k).column;
            val = cells.get(k).value;
            arr[row][col] = val;
        }

        view.redrawTiles(arr);

    }
}

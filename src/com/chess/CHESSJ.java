package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;


public class CHESSJ {
    public static void main(String[] args){
        Board board = Board.createStandardBoard();
        System.out.print(board);
        Table table = new Table();

    }
}

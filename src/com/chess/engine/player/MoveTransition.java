package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
    protected final Board transitionBoard;
    protected final Move move;
    protected final MoveStatus moveStatus;


    public MoveTransition(final Board transitionBoard, final Move move,final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }
    public Board getTransitionBoard(){
        return this.transitionBoard;
    }
}

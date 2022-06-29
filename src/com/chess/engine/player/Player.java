package com.chess.engine.player;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Player {
    protected final King playerKing;
    protected final Board board;
    protected final Collection<Move> legalMoves;
    protected final boolean isInCheck;


    public Player(final Board board,
                  final Collection<Move> opponentMoves,
                  final Collection<Move> legalMoves) {
        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves,calculateKingCastles(legalMoves,opponentMoves)));
        this.isInCheck = !attacksOnTile(this.playerKing.getPiecePosition(),opponentMoves).isEmpty();
    }






    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();

    public boolean isMoveLegal(final Move move){
        return legalMoves.contains(move);
    }
    public boolean isInCheck(){
        return this.isInCheck;
    }
    public boolean isInCheckMate(){
        return isInCheck()&&!hasEscapeMoves();
    }
    public boolean isInStaleMate(){
        return !isInCheck()&&!hasEscapeMoves();
    }
    public boolean isCastled(){
        return false;
    }
    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
        }
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.attacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                                                            transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }

    protected Piece getPlayerKing(){
        return this.playerKing;
    }

    protected static Collection<Move> attacksOnTile(final int piecePosition,final Collection<Move> opponentMoves){
        ArrayList<Move> attackMoves = new ArrayList<>();
        for(Move move:opponentMoves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }
        return attackMoves;
    }
     King establishKing() {
        for(Piece piece: getActivePieces()){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board!");
    }
    public boolean hasEscapeMoves(){
        for(Move move:this.legalMoves){
            final MoveTransition transistion = makeMove(move);
            if(transistion.moveStatus.isDone()){
                return true;
            }
        }
        return false;
    }
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    protected abstract Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,final Collection<Move> opponentLegals);

}

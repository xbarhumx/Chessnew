package com.chess.engine.piece;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    private final int[] CANDIDATE_MOVE_COORDINATE = {-9,-8,-7,-1,1,7,8,9};

    public King(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.KING,true);
    }
    public King(int piecePosition, Alliance pieceAlliance,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,Piecetype.KING,isFirstMove);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATE){
            final int candidateDestination = this.piecePosition + candidateCoordinateOffset;
            if(( isFirstColumnExclusion(this.piecePosition,candidateCoordinateOffset))
                    || (isEightColumnExclusion(this.piecePosition,candidateCoordinateOffset)) ){
                continue;
            }
            if(BoardUtil.isValidCoordinate(candidateDestination)){
                final Tiles tileAtSetCoordinate = board.getTile(candidateDestination);
                if(!tileAtSetCoordinate.isOccupied()){
                    possibleMoves.add(new Move.MajorMove(this,board,candidateDestination));
                }
                else{
                    final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                    final Alliance allianceOfPieceAtSetCoordinate = pieceAtSetCoordinate.getPieceAlliance();
                    if(this.pieceAlliance != allianceOfPieceAtSetCoordinate){
                        possibleMoves.add(new Move.AttackMove(this,board,candidateDestination,pieceAtSetCoordinate));
                    }
                }
            }
        }
        return possibleMoves;
    }
    public boolean isFirstColumnExclusion(int piecePosition, int currentCandidate){
        return (BoardUtil.FIRST_COLUMN[piecePosition])&&((currentCandidate==-1)
                || (currentCandidate==-9)
                || (currentCandidate==7));
    }
    public boolean isEightColumnExclusion(int piecePosition, int currentCandidate){
        return (BoardUtil.EIGHT_COLUMN[piecePosition])&&((currentCandidate==1)
                || (currentCandidate==9)
                || (currentCandidate==-7));
    }

    @Override
    public String toString(){
        return Piecetype.KING.toString();
    }
    public Piece movePiece(Move move){
        return new King(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}

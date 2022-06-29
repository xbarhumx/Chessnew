package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17 ,-15 ,-10 ,-6 ,6 ,10 ,15 ,17};


    public Knight(int pieceCoordinate,
                  Alliance pieceAlliance){
        super(pieceCoordinate,pieceAlliance,Piecetype.KNIGHT,true);
    }

    public Knight(int pieceCoordinate,
                  Alliance pieceAlliance,
                  final boolean isFirstMove){
        super(pieceCoordinate,pieceAlliance,Piecetype.KNIGHT,isFirstMove);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;
            if(isFirstColumnExclusion(candidateDestinationCoordinate,currentCandidateOffset)||
                    isSecondColumnExclusion(candidateDestinationCoordinate,currentCandidateOffset)||
                    isSeventhColumnExclusion(candidateDestinationCoordinate,currentCandidateOffset)||
                    isEightColumnExclusion(candidateDestinationCoordinate,currentCandidateOffset)) {
                continue;
            }
            candidateDestinationCoordinate += currentCandidateOffset;
            if(BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                final Tiles tileAtSetCoordinate = board.getTile(candidateDestinationCoordinate);
                if(!tileAtSetCoordinate.isOccupied()){
                    legalMoves.add(new Move.MajorMove(this,board,candidateDestinationCoordinate));
                }
                else{
                    final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                    final Alliance AllianceOfPieceAtSetDestination = pieceAtSetCoordinate.pieceAlliance;
                    if(this.pieceAlliance != AllianceOfPieceAtSetDestination){
                        legalMoves.add(new Move.AttackMove(this,board,candidateDestinationCoordinate,pieceAtSetCoordinate));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
    public boolean isFirstColumnExclusion(int currentPosition,
                                          int candidateOffset){
        return (BoardUtil.FIRST_COLUMN[currentPosition])&&(((candidateOffset==-17)
                            || (candidateOffset==-10)
                            || (candidateOffset==6)
                            || (candidateOffset == 15)));
    }
    public boolean isSecondColumnExclusion(int piecePosition,
                                           int currentCandidate){
        return (BoardUtil.SECOND_COLUMN[piecePosition])&&
                ((currentCandidate==-10)
                || (currentCandidate==6));
    }
    public boolean isSeventhColumnExclusion(int piecePosition, int currentCandidate){
        return (BoardUtil.SEVENTH_COLUMN[piecePosition])&&
                ((currentCandidate == -6) ||
                        (currentCandidate == 10));
    }
    public boolean isEightColumnExclusion(int piecePosition, int currentCandidate){
        return (BoardUtil.EIGHT_COLUMN[piecePosition])&&((currentCandidate==17)
                || (currentCandidate==10)
                || (currentCandidate==-6)
                || (currentCandidate == -15));
    }

    @Override
    public String toString(){
        return Piecetype.KNIGHT.toString();
    }
    public Piece movePiece(Move move){
        return new Knight(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}

package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    private final int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9,-8,-7,-1,1,7,8,9};
    public Queen(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.QUEEN,true);
    }
    public Queen(int piecePosition, final Alliance pieceAlliance,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,Piecetype.QUEEN,isFirstMove);
    }

    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        List<Move> possibleMove = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATE){
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExklusion(candidateDestinationCoordinate,currentCandidateOffset)||
                   isEightColumnExklusion(candidateDestinationCoordinate,currentCandidateOffset)){
                    break;
                }

                candidateDestinationCoordinate += currentCandidateOffset;
                if(!BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                    break;
                }
                else if(BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                    final Tiles tileAtSetCoordinate = board.getTile(candidateDestinationCoordinate);
                    if(!tileAtSetCoordinate.isOccupied()){
                        possibleMove.add(new Move.MajorMove(this,board,candidateDestinationCoordinate));
                    }
                    else{
                        final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                        final Alliance AllianceOfPieceAtSetDestination = pieceAtSetCoordinate.pieceAlliance;
                        if(this.pieceAlliance != AllianceOfPieceAtSetDestination){
                            possibleMove.add(new Move.AttackMove(this,board,candidateDestinationCoordinate,pieceAtSetCoordinate));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(possibleMove);
    }

    private boolean isEightColumnExklusion(final int candidatePosition,
                                           final int currentPosition) {
        return (BoardUtil.EIGHT_COLUMN[candidatePosition])&&((currentPosition == -7)
                                                        || (currentPosition == 9)
                                                        || (currentPosition == 1 ));
    }

    private boolean isFirstColumnExklusion(final int candidatePosition,
                                           int currentPosition) {
        return (BoardUtil.FIRST_COLUMN[candidatePosition])&&((currentPosition == 7)
                || (currentPosition == -9)
                || (currentPosition == -1 ));
    }
    @Override
    public String toString(){
        return Piecetype.QUEEN.toString();
    }
    public Piece movePiece(Move move){
        return new Queen(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}

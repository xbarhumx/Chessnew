package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    private static int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9,-7,7,9};

    public Bishop(final int piecePosition,
                  final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.BISHOP,true);
    }
    public Bishop(int piecePosition, Alliance pieceAlliance,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,Piecetype.BISHOP,isFirstMove);
    }
    @Override
    public List<Move> calculatePossibleMoves(Board board) {
        List<Move> possibleMoves = new ArrayList<>();
        for(final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATE){
            int candidateDestinationCoordinate = this.piecePosition;
            while(BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                if(isFirstColumnExklusion(candidateDestinationCoordinate,currentCandidateOffset)||
                   isEightColumnExklusion(candidateDestinationCoordinate,currentCandidateOffset)){
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
                if(BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                    final Tiles tileAtSetCoordinate = board.getTile(candidateDestinationCoordinate);
                    if(!tileAtSetCoordinate.isOccupied()){
                        possibleMoves.add(new Move.MajorMove(this,board,candidateDestinationCoordinate));
                    }
                    else{
                        final Piece pieceAtSetCoordinate = tileAtSetCoordinate.getPiece();
                        final Alliance AllianceOfPieceAtSetDestination = pieceAtSetCoordinate.pieceAlliance;
                        if(this.pieceAlliance != AllianceOfPieceAtSetDestination){
                            possibleMoves.add(new Move.AttackMove(this,board,candidateDestinationCoordinate,pieceAtSetCoordinate));
                        }
                        break;
                    }
                }

            }

        }
        return ImmutableList.copyOf(possibleMoves);
    }

    private boolean isEightColumnExklusion(int candidateDestinationCoordinate,
                                           int currentCandidate) {
        return (BoardUtil.FIRST_COLUMN[candidateDestinationCoordinate]) &&
                ((currentCandidate == 7) || (currentCandidate == -9));
    }

    private boolean isFirstColumnExklusion(int candidateDestinationCoordinate,
                                           int currentCandidate) {
        return (BoardUtil.FIRST_COLUMN[candidateDestinationCoordinate]) &&
                ((currentCandidate == -7) || (currentCandidate == 9));
    }
    @Override
    public String toString(){
        return Piecetype.BISHOP.toString();
    }
    public Piece movePiece(Move move){
        return new Bishop(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}

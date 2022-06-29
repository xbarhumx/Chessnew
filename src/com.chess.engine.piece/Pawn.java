package com.chess.engine.piece;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnAttackMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece{

    private final int[] CANDIDATE_MOVE_COORDINATE = {7,8,9,16};
    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance,Piecetype.PAWN,true);
    }
    public Pawn(int piecePosition, Alliance pieceAlliance,final boolean isFirstMove) {
        super(piecePosition, pieceAlliance,Piecetype.PAWN,isFirstMove);
    }
    @Override
    public List<Move> calculatePossibleMoves(Board board) {

        List<Move> possibleMoves = new ArrayList<>();
        for(final int candidateCoordinateOffset : CANDIDATE_MOVE_COORDINATE){
            int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getPieceDirection()*candidateCoordinateOffset);
            if(!BoardUtil.isValidCoordinate(candidateDestinationCoordinate)){
                continue;
            }
            if(candidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isOccupied()){

                //TODO Needs more work here(create class PawnMove) deal with promotion
                possibleMoves.add(new Move.MajorMove(this,board,candidateDestinationCoordinate));
            }
            else if(candidateCoordinateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtil.SEVENTH_RANK[this.piecePosition] && this.pieceAlliance.isBlack())||
                            (BoardUtil.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite()))){
                final int coordinateBehindCandidateCoordinate = this.piecePosition + (this.pieceAlliance.getPieceDirection() * 8);
                if(!board.getTile(coordinateBehindCandidateCoordinate).isOccupied() && !board.getTile(candidateDestinationCoordinate).isOccupied()){
                    possibleMoves.add(new PawnJump(this,board,candidateDestinationCoordinate));
                }
            }

            else if(candidateCoordinateOffset == 9 && !
                    ((BoardUtil.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())
                    ||(BoardUtil.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                if(board.getTile(candidateDestinationCoordinate).isOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.pieceAlliance != this.pieceAlliance){
                        possibleMoves.add(new PawnAttackMove(this,board,candidateDestinationCoordinate,pieceOnCandidate));

                    }
                }
                else if(board.getEnPassantPawn() != null){
                    if(board.getEnPassantPawn().piecePosition == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))){
                        Piece enPassantPawn = board.getEnPassantPawn();
                        if (enPassantPawn.getPieceAlliance() != this.pieceAlliance){
                            possibleMoves.add(new PawnEnPassantAttackMove(this,board,candidateDestinationCoordinate,enPassantPawn));
                        }
                    }
                }
            }
            else if(candidateCoordinateOffset == 7 &&
                    !((BoardUtil.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())
                            ||(BoardUtil.EIGHT_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))){
                if(board.getTile(candidateDestinationCoordinate).isOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if(pieceOnCandidate.pieceAlliance != this.pieceAlliance){
                        possibleMoves.add(new PawnAttackMove(this,board,candidateDestinationCoordinate,pieceOnCandidate));
                    }
                }
                else if(board.getEnPassantPawn() != null &&
                        board.getEnPassantPawn().piecePosition == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))){
                        final Piece enPassantPawn = board.getEnPassantPawn();
                        if (this.pieceAlliance != enPassantPawn.getPieceAlliance()){
                            possibleMoves.add(new PawnEnPassantAttackMove(this,board,candidateDestinationCoordinate,enPassantPawn));
                        }
                }
            }
        }
        return Collections.unmodifiableList(possibleMoves);
    }

    @Override
    public String toString(){
        return Piecetype.PAWN.toString();
    }
    public Piece movePiece(Move move){
        return new Pawn(move.getDestinationCoordinate(),move.getMovedPiece().getPieceAlliance());
    }
}

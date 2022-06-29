package com.chess.engine.piece;
import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.*;
import java.util.*;
 public abstract class Piece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    protected final Piecetype pieceType;
    protected final int cachedHashCode;

     public Piece(final int piecePosition,
                  final Alliance pieceAlliance,
                  final Piecetype pieceType,
                  boolean isFirstMove){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition=piecePosition;
        this.pieceType = pieceType;
        //TODO more work here!!
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

     protected int computeHashCode(){
         int result = pieceType.hashCode();
         result = 31 * result + pieceAlliance.hashCode();
         result = 31 * result + piecePosition;
         result = 31 * result + (isFirstMove ? 1 : 0);
         return result;
     }

     public abstract Piece movePiece(Move move);

     public Integer getPiecePosition(){
      return this.piecePosition;
    }

    public abstract List<Move> calculatePossibleMoves(Board board);

     public Alliance getPieceAlliance() {
         return pieceAlliance;
     }

     public boolean isFirstMove(){
         return isFirstMove;
     }

     public Piecetype getPieceType(){
         return this.pieceType;
     }

     @Override
     public int hashCode(){
        return this.cachedHashCode;
     }
     @Override
     public boolean equals(final Object other){
         if(other == this){
             return true;
         }
         if(!(other instanceof Piece)){
             return false;
         }
         Piece otherPiece = (Piece) other;
         return this.piecePosition == otherPiece.piecePosition &&
                 this.pieceAlliance == otherPiece.pieceAlliance &&
                 this.pieceType == otherPiece.pieceType &&
                 this.isFirstMove == otherPiece.isFirstMove;
     }
     public int getPieceValue(){
         return this.pieceType.getPieceValue();
     }


     public enum Piecetype {
         KING("K",300){
             @Override
             public boolean isKing() {
                 return true;
             }

             @Override
             public boolean isRook() {
                 return false;
             }

         },
         QUEEN("Q",900) {
             @Override
             public boolean isKing() {
                 return false;
             }
             @Override
             public boolean isRook() {
                 return false;
             }
         },
         BISHOP("B",300) {
             @Override
             public boolean isKing() {
                 return false;
             }
             @Override
             public boolean isRook() {
                 return false;
             }
         },
         KNIGHT("N",300) {
             @Override
             public boolean isKing() {
                 return false;
             }
             @Override
             public boolean isRook() {
                 return false;
             }
         },
         PAWN("P",100) {
             @Override
             public boolean isKing() {
                 return false;
             }
             @Override
             public boolean isRook() {
                 return false;
             }
         },
         ROOK("R",500) {
             @Override
             public boolean isKing() {
                 return false;
             }
             @Override
             public boolean isRook() {
                 return true;
             }
         };


         private String pieceName;
         private int pieceValue;
         Piecetype(final String pieceName, final int pieceValue){
             this.pieceName = pieceName;
         }

         @Override
         public String toString(){
             return this.pieceName;
         }

         public abstract boolean isKing();
         public abstract boolean isRook();

         public int getPieceValue(){
             return this.pieceValue;
         }

     }
 }

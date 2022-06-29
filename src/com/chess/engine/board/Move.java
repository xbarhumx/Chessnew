package com.chess.engine.board;

import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Rook;

public abstract class Move {
    final protected Piece movedPiece;
    final protected Board board;
    final protected int destinationCoordinate;
    final protected boolean isFirstMove;

    private Move(final Piece movedPiece,
                 final Board board,
                 final int destinationCoordinate){
        this.movedPiece = movedPiece;
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }
    private Move(final Board board,
                 final int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    public Board execute() {

        final Builder builder = new Builder();
        for(final Piece piece:this.board.currentPlayer().getActivePieces()){
            if(!movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
    public abstract boolean isCastlingMove();

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public int hashCode(){
        int result = 1;
        final int prime = 31;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.getPiecePosition();
        result = result + (isFirstMove ? 1:0);
        return result;
    }

    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Move)){
            return false;
        }

        final Move otherObject = (Move) other;
        return getDestinationCoordinate() == otherObject.getDestinationCoordinate()&&
               getMovedPiece().equals(otherObject.getMovedPiece())&&
                getCurrentCoordinate() == otherObject.getCurrentCoordinate();
    }
    public Piece getAttackedPiece(){
        return null;
    }


    public static class MajorMove extends Move {

        public MajorMove(final Piece movedPiece,
                          final Board board,
                          final int destinationCoordinate) {
            super(movedPiece, board, destinationCoordinate);
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }

        @Override
        public boolean equals(Object other){
           return this == other && other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return this.movedPiece.getPieceType() + BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnEnPassantAttackMove extends PawnAttackMove{
        public PawnEnPassantAttackMove(final Piece movedPiece,
                                       final Board board,
                                       final int destinationCoordinate,
                                       final Piece attackedPiece){
            super(movedPiece,board,destinationCoordinate,attackedPiece);

        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }
        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Piece piece:this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces()){
                if(!piece.equals(getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }



    public static class AttackMove extends Move{
        private Piece attackedPiece;
        public AttackMove(final Piece movedPiece,
                           final Board board,
                           final int destinationCoordinate,
                           final Piece attackedPiece) {
            super(movedPiece, board, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }

        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(Object other){
            if(this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }

            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(other) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public String toString(){
            return BoardUtil.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
                    BoardUtil.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }
    public static class MajorAttackMove extends AttackMove{
        public MajorAttackMove(final Piece movedPiece,
                               final Board board,
                               final int destinationCoordinate,
                               final Piece attackedPiece){
            super(movedPiece,board,destinationCoordinate,attackedPiece);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        @Override
        public String toString(){
            return this.movedPiece.getPieceType() + BoardUtil.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public static class PawnMove extends Move{


        private PawnMove(Piece movedPiece, Board board, int destinationCoordinate) {
            super(movedPiece, board, destinationCoordinate);
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnMove  && super.equals(other);
        }
        @Override
        public String toString(){
            return BoardUtil.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Piece movedPiece, Board board, int destinationCoordinate, Piece attackedPiece) {
            super(movedPiece, board, destinationCoordinate, attackedPiece);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtil.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0,1) + "x" +
            BoardUtil.getPositionAtCoordinate(this.getDestinationCoordinate());
        }
    }

    public static class PawnJump extends Move{

        public PawnJump(Piece movedPiece, Board board, int destinationCoordinate) {
            super(movedPiece, board, destinationCoordinate);
        }

        @Override
        public boolean isCastlingMove() {
            return false;
        }

        @Override
        public Board execute(){
            Builder builder = new Builder();
            for(Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!(this.getMovedPiece().equals(piece))){
                    builder.setPiece(piece);
                }
            }
            for(Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            Pawn movedPawn  = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        @Override
        public String toString(){
            return BoardUtil.getPositionAtCoordinate(destinationCoordinate);
        }
    }

    public static abstract class CastleMove extends Move{
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        public CastleMove(Piece movedPiece, Board board, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(movedPiece, board, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        public Piece getCastleRook(){
            return this.castleRook;
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(Piece piece: this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for(Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            //TODO do more work for isFirstMove()
            builder.setPiece(new Rook(this.castleRookDestination,this.castleRook.getPieceAlliance()));
            builder.setNextMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

            return builder.build();
        }
        @Override
        public int hashCode(){
            int prime = 31;
            int result = super.hashCode();
            result = prime*result + this.castleRook.hashCode();
            result = prime*result + this.castleRookDestination;
            return result;
        }
        @Override
        public boolean equals(final Object other){
            if(this == other){
                return true;
            }
            else if (!(other instanceof CastleMove)){
                return false;
            }
            CastleMove otherMove = (CastleMove) other;
            return super.equals(other) && this.castleRook.equals(otherMove.getCastleRook());
        }
    }




    public static  class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(Piece movedPiece, Board board, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(movedPiece, board, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);

        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }
    }
    public static  class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(Piece movedPiece, Board board, int destinationCoordinate, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(movedPiece, board, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }
    }
    private static class NullMove extends Move{
        public NullMove(){
            super(null,-1);
        }
        public Board execute(){
            throw new RuntimeException("cannot execute this method!");
        }
        public int getCurrentCoordinate(){
            return -1;
        }
        @Override
        public boolean isCastlingMove(){
            return false;
        }

    }

    public static abstract class MoveFactory{

        private static final Move NULL_MOVE = new NullMove();

        private MoveFactory() {
            throw new RuntimeException("Not extensible!");
        }

        public static Move createMove(Board board,
                                int destinationCoordinate,
                                int currentCoordinate){

           for(Move move : board.getAllLegalMoves()){
               if(move.getCurrentCoordinate() == currentCoordinate &&
                  move.getDestinationCoordinate() == destinationCoordinate ){

                   return move;
               }
           }
           return NULL_MOVE;
        }
    }


}

package com.chess.engine.board;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.piece.*;
import com.chess.engine.player.Blackplayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tiles> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    private final WhitePlayer whitePlayer;
    private final Blackplayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;

    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(Alliance.WHITE,this.gameBoard);
        this.blackPieces = calculateActivePieces(Alliance.BLACK,this.gameBoard);
        final Collection<Move> whiteLegalStandardMoves = calculateLegalMoves(whitePieces);
        final Collection<Move> blackLegalStandardMoves = calculateLegalMoves(blackPieces);
        this.enPassantPawn = builder.enPassantPawn;
        this.whitePlayer = new WhitePlayer(this,whiteLegalStandardMoves,blackLegalStandardMoves);
        this.blackPlayer = new Blackplayer(this,whiteLegalStandardMoves,blackLegalStandardMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.blackPlayer,this.whitePlayer);
    }
    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }
    public Tiles getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }

    private Collection<Piece> calculateActivePieces(final Alliance alliance,final List<Tiles> gameBoard){
        List<Piece> activePieces = new ArrayList<>();
        for(Tiles tile:gameBoard){
            Piece piece = tile.getPiece();
            if(tile.isOccupied()&&alliance == piece.getPieceAlliance()){
                activePieces.add(piece);
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){
        final List<Move> legalMoves = new ArrayList<>();
        for(Piece piece:pieces){
            legalMoves.addAll(piece.calculatePossibleMoves(this));
        }
        return legalMoves;
    }

    public static Board createStandardBoard(){
        final Builder board = new Builder();
        //Black side
        board.setPiece(new Rook(0,Alliance.BLACK));
        board.setPiece(new Knight(1,Alliance.BLACK));
        board.setPiece(new Bishop(2,Alliance.BLACK));
        board.setPiece(new Queen(3,Alliance.BLACK));
        board.setPiece(new King(4,Alliance.BLACK));
        board.setPiece(new Bishop(5,Alliance.BLACK));
        board.setPiece(new Knight(6,Alliance.BLACK));
        board.setPiece(new Rook(7,Alliance.BLACK));
        board.setPiece(new Pawn(8,Alliance.BLACK));
        board.setPiece(new Pawn(9,Alliance.BLACK));
        board.setPiece(new Pawn(10,Alliance.BLACK));
        board.setPiece(new Pawn(11,Alliance.BLACK));
        board.setPiece(new Pawn(12,Alliance.BLACK));
        board.setPiece(new Pawn(13,Alliance.BLACK));
        board.setPiece(new Pawn(14,Alliance.BLACK));
        board.setPiece(new Pawn(15,Alliance.BLACK));
        //White side
        board.setPiece(new Pawn(48,Alliance.WHITE));
        board.setPiece(new Pawn(49,Alliance.WHITE));
        board.setPiece(new Pawn(50,Alliance.WHITE));
        board.setPiece(new Pawn(51,Alliance.WHITE));
        board.setPiece(new Pawn(52,Alliance.WHITE));
        board.setPiece(new Pawn(53,Alliance.WHITE));
        board.setPiece(new Pawn(54,Alliance.WHITE));
        board.setPiece(new Pawn(55,Alliance.WHITE));
        board.setPiece(new Rook(56,Alliance.WHITE));
        board.setPiece(new Knight(57,Alliance.WHITE));
        board.setPiece(new Bishop(58,Alliance.WHITE));
        board.setPiece(new Queen(59,Alliance.WHITE));
        board.setPiece(new King(60,Alliance.WHITE));
        board.setPiece(new Bishop(61,Alliance.WHITE));
        board.setPiece(new Knight(62,Alliance.WHITE));
        board.setPiece(new Rook(63,Alliance.WHITE));
        //White to move
        board.setNextMoveMaker(Alliance.WHITE);
        return board.build();
    }

    public List<Tiles> createGameBoard (final Builder builder){
        final Tiles[] tiles = new Tiles[BoardUtil.NUM_TILES];
        for(int i = 0;i<BoardUtil.NUM_TILES;i++){
            tiles[i] = Tiles.createTile(i,builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i=0;i<BoardUtil.NUM_TILES;i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if((i+1) % BoardUtil.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }
    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(),this.blackPlayer.getLegalMoves()));
    }




    public static class Builder{
        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker; // The person who is moving right now
        Pawn enPassantPawn;


        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setNextMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        }

        public Builder setEnPassantPawn(final Pawn enPassantPawn){
            this.enPassantPawn = enPassantPawn;
            return this;
        }


    }
}

package com.chess.engine.board;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;


import java.util.HashMap;
import java.util.Map;

public abstract  class Tiles {

    protected final int tileCoordinate;

    private static final Map<Integer,EmptyTile> EMPTY_TILES_CASH = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles(){
        final Map<Integer,EmptyTile> placeHolderForEmptyTiles = new HashMap<>();

        for(int i=0;i<64;i++){
            placeHolderForEmptyTiles.put(i,new EmptyTile(i));
        }
        return ImmutableMap.copyOf(placeHolderForEmptyTiles);
    }

    public static Tiles createTile(final int tileCoordinate, Piece piece){
        return piece != null ? new OccupiedTile(piece,tileCoordinate): EMPTY_TILES_CASH.get(tileCoordinate);
    }

    private Tiles(int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }

    public abstract boolean isOccupied();

    public abstract Piece getPiece();

    public int getTileCoordinate(){
        return this.tileCoordinate;
    }



    public static final class EmptyTile extends Tiles{

        private EmptyTile(final int tileCoordinate){
            super(tileCoordinate);
        }
        @Override
        public boolean isOccupied(){
            return false;
        }
        @Override
        public Piece getPiece(){
            return null;
        }
        @Override
        public String toString(){
            return "-";
        }

    }

    public static final class OccupiedTile extends Tiles{
        private final Piece piece;

        private OccupiedTile(Piece  piece, final int tileCoordinate){
            super(tileCoordinate);
            this.piece = piece;
        }
        @Override
        public boolean isOccupied(){
            return true;
        }
        @Override
        public Piece getPiece(){
            return this.piece;
        }
        @Override
        public String toString(){
            return piece.getPieceAlliance().isBlack() ? piece.toString().toLowerCase() :
                    piece.toString();
        }

    }

}

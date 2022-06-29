package com.chess.engine.player;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class WhitePlayer extends Player {
    public WhitePlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves){

        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);
    }



    @Override
    public Alliance getAlliance(){
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent(){
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        ArrayList<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            if(!this.board.getTile(61).isOccupied() &&
               !this.board.getTile(62).isOccupied()){
                Tiles rookTile = this.board.getTile(63);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.attacksOnTile(61,opponentLegals).isEmpty()&&
                       Player.attacksOnTile(62,opponentLegals).isEmpty() &&
                       rookTile.getPiece().getPieceType().isRook()){
                        //TODO create castleMove class
                        kingCastles.add(new Move.KingSideCastleMove(this.getPlayerKing(),
                                        this.board,
                                        62,
                                        (Rook) rookTile.getPiece(),
                                        rookTile.getTileCoordinate(),
                                        61));
                    }
                }
            }
            if(!this.board.getTile(59).isOccupied()&&
               !this.board.getTile(58).isOccupied()&&
               !this.board.getTile(57).isOccupied()){
                Tiles rookTile = this.board.getTile(56);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.attacksOnTile(59,opponentLegals).isEmpty()&&
                       Player.attacksOnTile(58,opponentLegals).isEmpty()&&
                       Player.attacksOnTile(57,opponentLegals).isEmpty()&&
                       rookTile.getPiece().getPieceType().isRook()){
                        //TODO create castleMove class
                        kingCastles.add(new Move.QueenSideCastleMove(this.playerKing,this.board,
                                58,
                                        (Rook) rookTile.getPiece(),
                                        rookTile.getTileCoordinate(),
                                59));
                    }
                }
            }
        }
        return kingCastles;
    }

    @Override
    public String toString(){
        return "White";
    }
}

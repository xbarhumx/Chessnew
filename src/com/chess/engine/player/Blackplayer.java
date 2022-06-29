package com.chess.engine.player;

import com.chess.engine.alliance.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;

import java.util.ArrayList;
import java.util.Collection;

public class Blackplayer extends Player{

    public Blackplayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves){

        super(board,whiteStandardLegalMoves,blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }
    @Override
    public Alliance getAlliance(){
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent(){
        return this.board.whitePlayer();
    }



    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        ArrayList<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            // king side
            if(!this.board.getTile(6).isOccupied() &&
                    !this.board.getTile(5).isOccupied()){
                Tiles rookTile = this.board.getTile(7);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.attacksOnTile(6,opponentLegals).isEmpty()&&
                            Player.attacksOnTile(5,opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()){
                        //TODO create castleMove class
                        kingCastles.add(new KingSideCastleMove(this.getPlayerKing(),
                                                                    this.board,
                                                                    6,
                                                                    (Rook) rookTile.getPiece(),
                                                                    rookTile.getTileCoordinate(),
                                                                    5));
                    }
                }
            }
            //queen side
            if(!this.board.getTile(1).isOccupied()&&
                    !this.board.getTile(2).isOccupied()&&
                    !this.board.getTile(3).isOccupied()){
                Tiles rookTile = this.board.getTile(0);
                if(rookTile.isOccupied() && rookTile.getPiece().isFirstMove()){
                    if(Player.attacksOnTile(1,opponentLegals).isEmpty()&&
                            Player.attacksOnTile(2,opponentLegals).isEmpty()&&
                            Player.attacksOnTile(3,opponentLegals).isEmpty()&&
                            rookTile.getPiece().getPieceType().isRook()){
                        //TODO create castleMove class
                        kingCastles.add(new QueenSideCastleMove(this.playerKing,this.board,
                                2,
                                        (Rook) rookTile.getPiece(),
                                        rookTile.getTileCoordinate(),
                                3));
                    }
                }
            }
        }
        return kingCastles;
    }

    @Override
    public String toString(){
        return "Black";
    }

}

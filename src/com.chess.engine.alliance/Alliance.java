package com.chess.engine.alliance;

import com.chess.engine.player.Blackplayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
    BLACK{
        public int getPieceDirection(){
            return 1;
        }
        public int getOppositeDirection(){
            return -1;
        }
        @Override
        public Player choosePlayer(Blackplayer blackPlayer, WhitePlayer whitePlayer) {
            return blackPlayer;
        }

        public boolean isBlack(){
            return true;
        }
        public boolean isWhite(){
            return false;
        }
    },
    WHITE{
        public int getPieceDirection(){
            return -1;
        }
        public int getOppositeDirection(){
            return 1;
        }
        @Override
        public Player choosePlayer(Blackplayer blackPlayer, WhitePlayer whitePlayer) {
            return whitePlayer;
        }

        public boolean isBlack(){
            return false;
        }
        public boolean isWhite(){
            return true;
        }
    };

    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract int getPieceDirection();
    public abstract int getOppositeDirection();
    public abstract Player choosePlayer(Blackplayer blackPlayer, WhitePlayer whitePlayer);
}

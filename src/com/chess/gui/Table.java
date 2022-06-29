package com.chess.gui;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtil;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tiles;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    public final static String defaultPieceImagesPath = "fancy/";
    private Board chessBoard;
    private final JFrame gameFrame;
    private BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private static Dimension TITLE_PANEL_DIMENSION = new Dimension(10,10);
    private static Color lightTileColor =  Color.decode("#FFFACD");
    private static Color darkTileColor =  Color.decode("#593E1A");
    private  Tiles sourceTile;
    private  Tiles destinationTile;
    private  Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    private final MoveLog moveLog;

    public Table(){
        this.moveLog = new MoveLog();
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = populateMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.chessBoard = Board.createStandardBoard();
        boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame.add(takenPiecesPanel,BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel,BorderLayout.CENTER);
        this.gameFrame.add(gameHistoryPanel,BorderLayout.EAST);
        this.gameFrame.setVisible(true);

    }



    public JMenu createPreferencesMenu(){
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        JCheckBoxMenuItem legalhighLighterCheckBox = new JCheckBoxMenuItem("HighLight Legal Move",false);
        legalhighLighterCheckBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                highlightLegalMoves = legalhighLighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalhighLighterCheckBox);
        return preferencesMenu;
    }

    public JMenuBar populateMenuBar(){
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;

    }

    public JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");
        JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.print("open up that PGN file!");
            }
        });
        fileMenu.add(openPGN);
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {System.exit(0);};
        });
        fileMenu.add(exitMenu);
        return fileMenu;
    }

    public static class MoveLog{
        public final List<Move> moves;
        public MoveLog(){
            this.moves = new ArrayList<>();
        }
        public List<Move> getMoves(){
            return this.moves;
        }
        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }
        public void clear(){
            this.moves.clear();
        }
        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }
    }

    public class BoardPanel extends JPanel{
      public List<TilePanel> boardTiles;

      public BoardPanel(){
          super(new GridLayout(8,8));
          this.boardTiles = new ArrayList<>();
          for(int i = 0; i< BoardUtil.NUM_TILES; i++){
              final TilePanel titlePanel = new TilePanel(this , i);
              this.boardTiles.add(titlePanel);
              add(titlePanel);
          }
          setPreferredSize(BOARD_PANEL_DIMENSION);
          validate();
      }

        public void drawBoard(final Board chessBoard) {

          removeAll();
          for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)){
              tilePanel.drawTiles(chessBoard);
              add(tilePanel);
          }
          validate();
          repaint();
        }
    }



    private class TilePanel extends JPanel{
        private int tileId;
        TilePanel(final BoardPanel boardPanel,
                         final int titleId){
            super(new GridBagLayout());
            this.tileId = titleId;
            setPreferredSize(TITLE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePiece(chessBoard);
            addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (isRightMouseButton(e)) {
                        sourceTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(e)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if(humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }else{
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = Move.MoveFactory.createMove(chessBoard,
                                                                    destinationTile.getTileCoordinate(),
                                                                     sourceTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable(){

                            @Override
                            public void run() {
                                boardPanel.drawBoard(chessBoard);
                                gameHistoryPanel.redo(chessBoard,moveLog);
                                takenPiecesPanel.redo(moveLog);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            validate();
        }

        public void assignTileColor(){
            if(BoardUtil.EIGHT_RANK[this.tileId]||
                    BoardUtil.SIXTH_RANK[this.tileId]||
                    BoardUtil.FORTH_RANK[this.tileId]||
                    BoardUtil.SECOND_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0? lightTileColor : darkTileColor);
            }
            else if (BoardUtil.SEVENTH_RANK[this.tileId]||
                    BoardUtil.FIFTH_RANK[this.tileId]||
                    BoardUtil.THIRD_RANK[this.tileId]||
                    BoardUtil.FIRST_RANK[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? darkTileColor : lightTileColor);
            }
        }

        public void assignTilePiece(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isOccupied()){
                try{
                    BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath+ board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0,1)
                                                                        + board.getTile(this.tileId).getPiece().toString().substring(0,1) + ".gif"));

                    add(new JLabel(new ImageIcon(image)));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        public void drawTiles(final Board chessBoard) {
            assignTileColor();
            assignTilePiece(chessBoard);
            highlightLegalMoves(chessBoard);
            validate();
            repaint();
        }

        private void highlightLegalMoves(final Board board){
            if(highlightLegalMoves){
                for(final Move move : pieceLegalMoves(board)){
                    if(move.getDestinationCoordinate() == this.tileId){
                        try{
                           add(new JLabel(new ImageIcon(ImageIO.read(new File("misc/green_dot.png")))));
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        private List<Move> pieceLegalMoves(final Board board){
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                return humanMovedPiece.calculatePossibleMoves(board);
            }
            return Collections.emptyList();
        }
    }

    public enum BoardDirection {
        NORMAL{
            @Override
            public List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            public BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            public List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            public BoardDirection opposite() {
                return NORMAL;
            }
        };

        public abstract List<TilePanel> traverse (final List<TilePanel> boardTiles);
        public abstract BoardDirection opposite();
    }
}

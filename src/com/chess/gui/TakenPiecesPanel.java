package com.chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.chess.engine.board.Move.AttackMove;

import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

public class TakenPiecesPanel extends JPanel {
    final JPanel northPanel;
    final JPanel southPanel;
    final Color PANEL_COLOR = Color.decode("0xFDFE6");
    final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40,80);
    final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    public TakenPiecesPanel(){
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.northPanel = new JPanel(new GridLayout(8,2));
        this.southPanel = new JPanel(new GridLayout(8,2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel,BorderLayout.NORTH);
        this.add(this.southPanel,BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }
    public void redo(final MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        List<Piece> whiteTakenPieces = new ArrayList<>();
        List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move : moveLog.getMoves()){
            if(move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                }
                else if (takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                }
                else{
                    throw new RuntimeException("Should not be here!");
                }
            }
        }
        Collections.sort(whiteTakenPieces,new Comparator<Piece>(){
            @Override
            public int compare(Piece o1,Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces,new Comparator<Piece>(){
            @Override
            public int compare(Piece o1,Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        for(final Piece piece: whiteTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("fancy/"+piece.getPieceAlliance().toString().substring(0,1)+
                                                        piece.toString().substring(0,1) + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        for(final Piece piece: blackTakenPieces){
            try{
                final BufferedImage image = ImageIO.read(new File("fancy/"+piece.getPieceAlliance().toString().substring(0,1)+
                        piece.toString().substring(0,1) + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.northPanel.add(imageLabel);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}

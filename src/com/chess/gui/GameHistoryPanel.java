package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table.MoveLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameHistoryPanel extends JPanel {
    private final DataModel model;
    private final JScrollPane scrollPane;
    private final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100,400);
    public GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane,BorderLayout.CENTER);
        this.setVisible(true);
    }
    public void redo(final Board board,
                     final MoveLog moveLog){
        int currentRow = 0;
        this.model.clear();
        for(final Move move : moveLog.getMoves()){
            final String moveText = move.toString();
            if(move.getMovedPiece().getPieceAlliance().isWhite()){
                this.model.setValueAt(moveText,currentRow,0);
            }
            else if(move.getMovedPiece().getPieceAlliance().isBlack()){
                this.model.setValueAt(moveText,currentRow,1);
                currentRow++;
            }
        }
        if(moveLog.getMoves().size()>0){
            final Move lastMove = moveLog.getMoves().get(moveLog.size()-1);
            final String moveText = lastMove.toString();
            if(lastMove.getMovedPiece().getPieceAlliance().isWhite()){
                this.model.setValueAt(moveText + calculateCheckAndMateHash(board),currentRow,0);
            }
            else if(lastMove.getMovedPiece().getPieceAlliance().isBlack()){
                this.model.setValueAt(moveText + calculateCheckAndMateHash(board),currentRow-1,1);
            }
            final JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }

    private String calculateCheckAndMateHash(Board board) {
        if(board.currentPlayer().isInCheckMate()){
            return "#";
        }
        else if (board.currentPlayer().isInCheck()){
            return "+";
        }
        return "";
    }

    public static class DataModel extends DefaultTableModel{
        private final List<Row> values;
        private static final String[] NAMES = {"White","Black"};
        public DataModel(){
            this.values = new ArrayList<>();
        }

        public void clear(){
            this.values.clear();
            setRowCount(0);
        }
        @Override
        public int getRowCount(){
            if(this.values == null){
                return 0;
            }
            return this.values.size();
        }
        @Override
        public int getColumnCount(){
            return NAMES.length;
        }
        @Override
        public Object getValueAt(final int row, final int column){
            final Row currentRow = this.values.get(row);
            if(column == 0){
                return currentRow.getWhitePlayer();
            }
            else if (column == 1){
                return currentRow.getBlackPlayer();
            }
            return null;
        }
        @Override
        public void setValueAt(final Object aValue,
                               final int row,
                               final int column){
            final Row currentRow;
            if(this.values.size()<=row){
                currentRow = new Row();
                this.values.add(currentRow);
            }
            else{
                currentRow = this.values.get(row);
            }
            if(column == 0){
                currentRow.setWhitePlayer((String)aValue);
                fireTableRowsInserted(row,row);
            }
            else if (column == 1){
                currentRow.setBlackPlayer((String)aValue);
                fireTableCellUpdated(row,column);
            }
        }
        @Override
        public Class<?> getColumnClass(final int column){
            return Move.class;
        }
        @Override
        public String getColumnName(final int column){
            return NAMES[column];
        }
    }

    public static class Row{
        private String whitePlayer;
        private String blackPlayer;
        public Row(){
        }
        public String getWhitePlayer(){
            return this.whitePlayer;
        }
        public String getBlackPlayer(){
            return this.blackPlayer;
        }
        public void setWhitePlayer(final String whitePlayer){
            this.whitePlayer = whitePlayer;
        }
        public void setBlackPlayer(final String blackPlayer){
            this.blackPlayer = blackPlayer;
        }
    }
}

package com.chess.engine.board;

import com.chess.engine.alliance.Alliance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardUtil {
    public static int NUM_TILES = 64;
    public static int NUM_TILES_PER_ROW = 8;

    public static final String[] ALGEBREIC_NOTATION = initializeAlgebreicNotation();



    public static final Map<String,Integer> POSITION_AT_COORDINATE = initializePositionToCoordinate();



    public BoardUtil(){
        throw new RuntimeException("Don't instantiate this class");
    }

    public static boolean isValidCoordinate(int CoordinateOfDestination){
        return CoordinateOfDestination >=0 && CoordinateOfDestination < 64;
    }
    private static String[] initializeAlgebreicNotation() {
        String[] result= {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
        return result;
    }

    private static Map<String, Integer> initializePositionToCoordinate() {
        Map<String,Integer> result = new HashMap<>();
        for(int i = 0;i<ALGEBREIC_NOTATION.length;i++){
            result.put(ALGEBREIC_NOTATION[i],i);
        }
        return result;
    }

    public static final boolean[]  FIRST_COLUMN = initColumn(0);
    public static final boolean[]  SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHT_COLUMN = initColumn(7);

    public static final boolean[] EIGHT_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FORTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);

    private static boolean[] initRow(int rowCounter){
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[rowCounter] = true;
            rowCounter++;
        }while(rowCounter % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private static boolean[] initColumn(int columnCounter) {
        final boolean[] column = new boolean[NUM_TILES];
        do{
            column[columnCounter] = true;
            columnCounter +=NUM_TILES_PER_ROW;
        }while(columnCounter < NUM_TILES);
        return column;
    }

    public static int getCoordinateAtPosition(final String position){
        return POSITION_AT_COORDINATE.get(position);
    }
    public static String getPositionAtCoordinate(int coordinate){
        return ALGEBREIC_NOTATION[coordinate];
    }
}

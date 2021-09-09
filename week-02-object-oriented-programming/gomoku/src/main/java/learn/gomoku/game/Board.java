package learn.gomoku.game;

import java.util.List;

public class Board {
    private final char[][] board;

    public Board(int width) {
        this.board = new char[width][width];
    }

    public Board(List<Stone> stones){
        board = new char[15][15];
        for(Stone stone : stones){
            board[stone.getRow()][stone.getColumn()] = stone.isBlack() ? 'X' : 'O';
        }
    }

    public void addStone(Stone stone){
        board[stone.getRow()][stone.getColumn()] = stone.isBlack() ? 'X' : 'O';
    }

    public char[][] getBoard() {
        return board;
    }

    public void printBoard(){
        int row=board.length;
        int col=board[0].length;
        System.out.println("\n   01 02 03 04 05 06 07 08 09 10 11 12 13 14 15");
        for(int i=0; i<row; i++){
            System.out.print( i<9 ? "0"+(i+1) : i+1);
            for(int j=0; j<col; j++){
                System.out.print("  ");
                if(board[i][j]==0){
                    System.out.print("_");
                }
                else{
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}

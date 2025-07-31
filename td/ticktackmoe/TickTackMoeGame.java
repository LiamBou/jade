package td.ticktackmoe;

import java.io.Serializable;

public class TickTackMoeGame implements Serializable {
    char[][] board;

    public TickTackMoeGame() {
        board = new char[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append("|");
            for (int j = 0; j < 4; j++) {
                sb.append(board[i][j]);
                sb.append("|");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean play(int x, int y, char player) {
        System.out.println("Player " + player + " plays at " + x + " " + y);
        if (x < 0 || x > 3 || y < 0 || y > 3) {
            return false;
        }
        if (board[x][y] == ' ') {
            board[x][y] = player;
            return true;
        }
        return false;
    }

    public boolean isWin(char player) {
        // A player wins if he has a line, a column or a diagonal of 3 cells with his symbol
        //TODO
        return false;
    }

    public boolean isFull() {
        return board[0][0] != ' ' && board[0][1] != ' ' && board[0][2] != ' ' && board[0][3] != ' ' &&
                board[1][0] != ' ' && board[1][1] != ' ' && board[1][2] != ' ' && board[1][3] != ' ' &&
                board[2][0] != ' ' && board[2][1] != ' ' && board[2][2] != ' ' && board[2][3] != ' ' &&
                board[3][0] != ' ' && board[3][1] != ' ' && board[3][2] != ' ' && board[3][3] != ' ';
    }
}

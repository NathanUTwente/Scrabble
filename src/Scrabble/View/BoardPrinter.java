package Scrabble.View;
import Scrabble.Model.BoardModel.Board;
import Scrabble.Model.BoardModel.Square;
import Scrabble.Model.BoardModel.Tile;
import Utils.ANSI;


public class BoardPrinter {

    public static void main(String[] args) {
        Board board = new Board();
        createBoard(board);
    }

    public static String createBoard(Board board){
        StringBuilder builder = new StringBuilder();

        builder.append("   ");
        for (int x = 0; x < Board.DIM; x++) {
            builder.append("  " + ((char) (65 + x)) + "  ");
        }
        builder.append("\n");

        builder.append("   ┌");

        for (int x = 0; x < Board.DIM; x++) {
            builder.append("────┰");
        }
        builder.append("────┐");
        builder.append("\n");

        for (int y = 0; y < Board.DIM; y++) {
            builder.append(" " + (y<9? " " : "") + (y+1) + " ");
            builder.append("│");
            for (int x = 0; x < Board.DIM; x++) {
                Square square = board.getSquare(x, y);
                switch (square.getSpecialType()){
                    case CENTRE:
                        builder.append(ANSI.PURPLE_BACKGROUND);
                        break;
                    case TRIPLE_LETTER:
                        builder.append(ANSI.BLUE_BACKGROUND_BRIGHT);
                        break;
                    case TRIPLE_WORD:
                        builder.append(ANSI.RED_BACKGROUND);
                        break;
                    case DOUBLE_LETTER:
                        builder.append(ANSI.BLUE_BACKGROUND);
                        break;
                    case DOUBLE_WORD:
                        builder.append(ANSI.RED_BACKGROUND_BRIGHT);
                    default:

                }
                Tile tile = square.getTile();
                builder.append("  " + (tile != null ? tile.getTileType() : " ") + " ");
                builder.append(ANSI.RESET);
                builder.append("│");
            }
            builder.append("\n   ");
            if (y<Board.DIM -1){
                builder.append("├");
            }
            else {
                builder.append("└");
            }

            for (int x = 0; x < Board.DIM; x++) {
                if(y == Board.DIM -1 && x < Board.DIM -1){
                    builder.append("────┴");
                }

                else if (y == Board.DIM -1 && x == Board.DIM -1){
                    builder.append("────┘");
                }

                else if (y<Board.DIM -1 && x == Board.DIM -1){
                    builder.append("────┤");
                }

                else {
                    builder.append("────┼");
                }
            }
            builder.append("\n");
        }
        builder.append(ANSI.RESET);
        return builder.toString();
    }
}

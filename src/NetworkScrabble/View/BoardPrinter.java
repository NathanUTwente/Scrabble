package NetworkScrabble.View;
import NetworkScrabble.Model.BoardModel.Board;
import NetworkScrabble.Model.BoardModel.Square;
import NetworkScrabble.Model.BoardModel.Tile;
import Utils.ANSI;


public class BoardPrinter {

    public static void main(String[] args) {
        //Testing
        Board board = new Board();
        board.setField( 1, 2, new Tile(Tile.TileType.B, 5));
        board.setField( 1, 0, new Tile(Tile.TileType.A, 5));
        board.setField( 3, 0, new Tile(Tile.TileType.BLANK, 5));
        System.out.println(createBoard(board));
    }

    public static String createBoard(Board board){
        StringBuilder builder = new StringBuilder();

        builder.append("   ");
        for (int x = 0; x < Board.DIM; x++) {
            builder.append("  " + ((char) (65 + x)) + "  ");
        }
        builder.append("\n");

        builder.append("    ┌");

        for (int x = 0; x < Board.DIM -1; x++) {
            builder.append("────┰");
        }
        builder.append("────┐");
        builder.append("\n");

        for (int row = 0; row < Board.DIM; row++) {
            builder.append(" " + (row<9? " " : "") + (row+1) + " ");
            builder.append("│");
            for (int col = 0; col < Board.DIM; col++) {
                Square square = board.getSquare(col, row);
                if (square.getSpecialType() != null) {
                    switch (square.getSpecialType()) {
                        case CENTRE:
                            builder.append(ANSI.PURPLE_BACKGROUND);
                            break;
                        case TRIPLE_LETTER:
                            builder.append(ANSI.GREEN_BACKGROUND);
                            break;
                        case TRIPLE_WORD:
                            builder.append(ANSI.RED_BACKGROUND);
                            break;
                        case DOUBLE_LETTER:
                            builder.append(ANSI.BLUE_BACKGROUND);
                            break;
                        case DOUBLE_WORD:
                            builder.append(ANSI.CYAN_BACKGROUND);
                        default:

                    }
                }
                Tile tile = square.getTile();
                builder.append("  " + (tile != null ? tile.getTileLetter() : " ") + " ");
                builder.append(ANSI.RESET);
                builder.append("│");
            }
            builder.append("\n   ");
            if (row< Board.DIM -1){
                builder.append(" ├");
            }
            else {
                builder.append(" └");
            }

            for (int x = 0; x < Board.DIM; x++) {
                if(row == Board.DIM -1 && x < Board.DIM -1){
                    builder.append("────┴");
                }

                else if (row == Board.DIM -1 && x == Board.DIM -1){
                    builder.append("────┘");
                }

                else if (row< Board.DIM -1 && x == Board.DIM -1){
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

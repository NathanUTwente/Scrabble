package Scrabble.Model.PlayerModels;

import Scrabble.Model.BoardModel.Board;
import Scrabble.View.TextBoardRepresentation;

public class HumanPlayer extends Player{

    /**
     * Creates a new player with a given name, an empty tile deck and 0 points
     *
     * @param name name of player
     */
    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public String[] determineMove(Board board, TextBoardRepresentation tui) {
        String[] move = tui.getMove(this, board);
        return move;
    }
}

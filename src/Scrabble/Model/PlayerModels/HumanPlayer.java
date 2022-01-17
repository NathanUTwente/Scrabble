package Scrabble.Model.PlayerModels;

import Scrabble.Model.BoardModel.Board;

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
    public int[] determineMove(Board board) {
        return new int[0];
    }
}

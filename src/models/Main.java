package models;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Player player = new Player(1);
        Player player = DataManager.loadPlayerData();
        HanoiGame game = new HanoiGame();
        game.setPlayer(player);

        game.startGame(Difficulty.EASY, 0);
        game.endGame();
    }
}

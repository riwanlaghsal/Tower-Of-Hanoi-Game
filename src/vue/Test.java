package vue;
import models.*;
import controlleurs.*;

import javax.swing.*;
import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        Player player = new Player(1);
        HanoiGame game = new HanoiGame();
        game.setPlayer(player);
        game.startGame(Difficulty.EASY, 0);
        HanoiGUI gui = new HanoiGUI(game);
        game.setGui(gui);
        while(true){
            if(game.isGameOver()){
                game.endGame();
                break;
            }
        }
        /*SwingUtilities.invokeLater(() -> {
            try {
                new MainMenu();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });*/
    }
}

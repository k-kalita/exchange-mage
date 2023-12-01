package exchangemage.base;

import exchangemage.actors.Player;
import exchangemage.scenes.Scene;

public class Game {
    private static Game game;

    public static Game getGame() {
        if (game == null) {
            game = new Game();
        }
        return game;
    }

    public Scene getScene() {
        return null;
    }

    public Player getPlayer() {
        return null;
    }
}

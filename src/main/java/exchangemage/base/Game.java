package exchangemage.base;

import java.util.Objects;

import exchangemage.actors.Player;
import exchangemage.scenes.Scene;

public class Game {
    private Scene scene;
    private Player player;

    public Scene getScene() {return this.scene;}

    public void setScene(Scene scene) {
        Objects.requireNonNull(scene, "Scene cannot be null");
        this.scene = scene;
    }

    public Player getPlayer() {return this.player;}

    public void setPlayer(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        this.player = player;
    }
}

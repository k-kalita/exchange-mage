package exchangemage.base;

import java.util.Objects;

/** Locator for the {@link Game} instance currently in use. */
public class GameLocator {
    /** The current {@link Game} instance. */
    private static Game game;

    /**
     * Initializes the locator with the given {@link Game} instance.
     *
     * @param game the Game instance to use
     * @throws NullPointerException if game is null
     */
    public static void init(Game game) {
        Objects.requireNonNull(game, "Game cannot be null");
        GameLocator.game = game;
    }

    /**
     * @return the current {@link Game} instance
     * @throws IllegalStateException if the locator has not been initialized
     */
    public static Game getGame() {
        if (game == null)
            throw new IllegalStateException("GameLocator has not been initialized");
        return game;
    }
}

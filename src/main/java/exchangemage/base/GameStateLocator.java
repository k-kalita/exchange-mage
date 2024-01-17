package exchangemage.base;

import java.util.Objects;

/** Locator for the {@link GameState} instance currently in use. */
public class GameStateLocator {
    /** The current {@link GameState} instance. */
    private static GameState gameState = null;

    /**
     * Initializes the locator with the given {@link GameState} instance.
     *
     * @param gameState the GameState instance to use
     * @throws NullPointerException if gameState is null
     */
    public static void init(GameState gameState) {
        Objects.requireNonNull(gameState, "GameState cannot be null");
        GameStateLocator.gameState = gameState;
    }

    /**
     * @return the current {@link GameState} instance
     * @throws IllegalStateException if the locator has not been initialized
     */
    public static GameState getGameState() {
        if (gameState == null)
            throw new IllegalStateException("GameStateLocator has not been initialized");
        return gameState;
    }
}

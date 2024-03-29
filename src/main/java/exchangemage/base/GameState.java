package exchangemage.base;

import exchangemage.actors.Player;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.scenes.Scene;


/**
 * An interface of an auxiliary class used as a centralised gateway for accessing the most important
 * elements of the current game state (e.g. the {@link Player}, the current {@link Scene}, the
 * {@link Effect} currently being resolved, etc.).
 * <br><br>
 * The instance of GameState currently in use can be obtained through the {@link GameStateLocator}
 * class (provided that it has been initialised).
 *
 * @see Game
 */
public interface GameState {
    /** @return the {@link Player} of the current {@link Game} */
    Player getPlayer();

    /** @return the current {@link Scene} in the {@link Game} */
    Scene getScene();

    /** @return the {@link EffectPlayer} used by the current {@link Scene} */
    EffectPlayer getEffectPlayer();

    /**
     * @return the {@link TargetingManager} used by the {@link EffectPlayer} of the current
     * {@link Scene}
     */
    TargetingManager getTargetingManager();

    /**
     * @return the {@link Effect} currently being resolved by the {@link EffectPlayer} of the
     * current {@link Scene} or <code>null</code> if there is no effect in resolution
     */
    Effect<?> getEffectInResolution();

    /**
     * @return the {@link Effect} currently being evaluated by the {@link EffectPlayer} of the
     * current {@link Scene} or <code>null</code> if there is no effect in evaluation
     */
    Effect<?> getEffectInEvaluation();
}

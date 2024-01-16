package exchangemage.base;

import exchangemage.actors.Player;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.effects.triggers.conditions.Condition;
import exchangemage.scenes.Scene;

/**
 * An auxiliary class used as a centralised interface for accessing the most important components of
 * the game (e.g. the {@link Player}, the current {@link Scene}, the {@link Effect} currently being
 * resolved, etc.).
 * <br><br>
 * All objects which need to access other elements of the current game state (e.g.
 * {@link Condition}s of {@link PersistentEffect}s referencing the target of the effect which
 * might trigger them) should do so through the getter methods of this class.
 *
 * @see Game
 */
public class GameState {
    /** @return the {@link Player} of the current {@link Game} */
    public static Player getPlayer() {return Game.getGame().getPlayer();}

    /** @return the current {@link Scene} in the {@link Game} */
    public static Scene getScene() {return Game.getGame().getScene();}

    /** @return the {@link EffectPlayer} used by the current {@link Scene} */
    public static EffectPlayer getEffectPlayer() {return getScene().getEffectPlayer();}

    /**
     * @return the {@link TargetingManager} used by the {@link EffectPlayer} of the current
     * {@link Scene}
     */
    public static TargetingManager getTargetingManager() {
        return getEffectPlayer().getTargetingManager();
    }

    /**
     * @return the {@link Effect} currently being resolved by the {@link EffectPlayer} of the
     * current {@link Scene} or <code>null</code> if there is no effect in resolution
     */
    public static Effect<?> getEffectInResolution() {
        return getEffectPlayer().getEffectInResolution();
    }

    /**
     * @return the {@link Effect} currently being evaluated by the {@link EffectPlayer} of the
     * current {@link Scene} or <code>null</code> if there is no effect in evaluation
     */
    public static Effect<?> getEffectInEvaluation() {
        return getEffectPlayer().getEffectInEvaluation();
    }
}

package exchangemage.base;

import exchangemage.actors.Player;
import exchangemage.effects.base.Effect;
import exchangemage.effects.base.EffectPlayer;
import exchangemage.effects.base.PersistentEffect;
import exchangemage.effects.targeting.Targetable;
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
    /**
     * Returns the {@link Player} of the current {@link Game}.
     *
     * @return the player
     * @see Player
     */
    public static Player getPlayer() {return Game.getGame().getPlayer();}

    /**
     * Returns the current {@link Scene} in the {@link Game}.
     *
     * @return the current scene
     * @see Scene
     */
    public static Scene getScene() {return Game.getGame().getScene();}

    /**
     * Returns the {@link EffectPlayer} used by the current {@link Scene}.
     *
     * @return the current effect player
     * @see EffectPlayer
     */
    public static EffectPlayer getEffectPlayer() {return getScene().getEffectPlayer();}

    /**
     * Returns the {@link TargetingManager} used by the {@link EffectPlayer} of the current
     * {@link Scene}.
     *
     * @return the current targeting manager
     * @see TargetingManager
     */
    public static TargetingManager getTargetingManager() {
        return getEffectPlayer().getTargetingManager();
    }

    /**
     * Returns the {@link Effect} currently being resolved by the {@link EffectPlayer} of the
     * current {@link Scene}.
     *
     * @return the current effect or <code>null</code> if no effect is currently being resolved
     * @see Effect
     */
    public static Effect getCurrentEffect() {return getEffectPlayer().getCurrentEffect();}

    /**
     * Returns the {@link Targetable} currently targeted by the {@link Effect} being resolved
     *
     * @return the current target or <code>null</code> if no effect is currently being resolved
     * @see Targetable
     */
    public static Targetable getCurrentTarget() {return getCurrentEffect().getTarget();}
}

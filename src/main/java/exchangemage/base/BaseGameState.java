package exchangemage.base;

import exchangemage.actors.Player;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.scenes.Scene;

/**
 * Base implementation of the {@link GameState} interface. Retrieves game state elements from the
 * current {@link Game} retrieved through the {@link GameLocator} class.
 */
public class BaseGameState implements GameState {
    /** @return the {@link Player} of the current {@link Game} */
    @Override
    public Player getPlayer() {return GameLocator.getGame().getPlayer();}

    /** @return the current {@link Scene} in the {@link Game} */
    @Override
    public Scene getScene() {return GameLocator.getGame().getScene();}

    /** @return the {@link EffectPlayer} used by the current {@link Scene} */
    @Override
    public EffectPlayer getEffectPlayer() {return getScene().getEffectPlayer();}

    /**
     * @return the {@link TargetingManager} used by the {@link EffectPlayer} of the current
     * {@link Scene}
     */
    @Override
    public TargetingManager getTargetingManager() {return getEffectPlayer().getTargetingManager();}

    /**
     * @return the {@link Effect} currently being resolved by the {@link EffectPlayer} of the
     * current {@link Scene} or <code>null</code> if there is no effect in resolution
     */
    @Override
    public Effect<?> getEffectInResolution() {return getEffectPlayer().getEffectInResolution();}

    /**
     * @return the {@link Effect} currently being evaluated by the {@link EffectPlayer} of the
     * current {@link Scene} or <code>null</code> if there is no effect in evaluation
     */
    @Override
    public Effect<?> getEffectInEvaluation() {return getEffectPlayer().getEffectInEvaluation();}
}

package exchangemage.effects.triggers.conditions;

import exchangemage.base.GameState;
import exchangemage.effects.base.Effect;
import exchangemage.effects.base.PersistentEffect;
import exchangemage.actors.Player;
import exchangemage.actors.Actor;
import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * Base class for all conditions. Conditions represent statements about the current game state
 * which can be evaluated to a boolean value. As part of {@link ConditionalTrigger}s they can be
 * used to determine whether a given {@link Effect} should be triggered based on criteria such as
 * the {@link Player}'s health, the statuses affecting the targeted {@link Actor}, the type of the
 * effect currently in resolution (in case of {@link PersistentEffect}s' activation), etc.
 *
 * @see ConditionalTrigger
 * @see GameState
 * @see Effect
 */
public interface Condition {
    /**
     * Evaluates the statement represented by this condition to a boolean value.
     *
     * @return <code>true</code> if the condition is fulfilled, <code>false</code> otherwise
     */
    public boolean isFulfilled();
}

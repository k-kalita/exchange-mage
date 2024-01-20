package exchangemage.effects.triggers;

import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;

/**
 * Triggers act as evaluators of certain requirements which must be met in order for an
 * {@link Effect} to be enqueued into the resolution queue of the {@link EffectPlayer} and/or
 * be resolved and executed.
 *
 * @see Effect
 * @see EffectPlayer
 */
@FunctionalInterface
public interface Trigger {
    /** @return <code>true</code> if the trigger is activated, <code>false</code> otherwise. */
    boolean isActivated();
}

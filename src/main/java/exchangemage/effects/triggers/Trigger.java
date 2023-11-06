package exchangemage.effects.triggers;

import exchangemage.effects.base.Effect;
import exchangemage.effects.base.EffectPlayer;

/**
 * Triggers act as evaluators of certain requirements which must be met in order for an
 * {@link Effect} to be enqueued into the resolution queue of the {@link EffectPlayer} and/or
 * be resolved and executed.
 *
 * @see Effect
 * @see EffectPlayer
 */
public interface Trigger {
    /**
     * Returns whether the {@link Trigger} is activated (i.e. whether the requirements of the
     * trigger are met).
     *
     * @return <code>true</code> if the trigger is activated, <code>false</code> otherwise.
     *
     * @see Effect
     */
    boolean isActivated();
}

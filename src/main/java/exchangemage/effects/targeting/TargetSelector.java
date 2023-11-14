package exchangemage.effects.targeting;

import exchangemage.effects.base.Effect;
import exchangemage.effects.base.EffectPlayer;

/**
 * An abstract base class for all classes used to select a target for an {@link Effect}. Its
 * {@link #chooseTarget()} method is called by the {@link EffectPlayer} when the effect is enqueued.
 *
 * @see Effect
 * @see EffectPlayer
 * @see Targetable
 */
public abstract class TargetSelector {
    private Targetable target;

    /**
     * Chooses a target for this selector's {@link Effect}. This method is called when the effect
     * is enqueued. The return value of this method indicates whether an appropriate target could
     * have been selected.
     *
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise.
     *
     * @see Effect
     * @see EffectPlayer
     * @see Targetable
     */
    public abstract boolean chooseTarget();

    /**
     * Returns whether a target has been selected.
     *
     * @return <code>true</code> if a target has been selected, <code>false</code> otherwise.
     *
     * @see Targetable
     */
    public boolean hasTarget() { return target != null; }

    /**
     * Sets the target to the given {@link Targetable} object.
     *
     * @param target the target to set.
     *
     * @see Targetable
     */
    public void setTarget(Targetable target) {
        if (target == null)
            throw new IllegalArgumentException("Cannot set null target.");
        this.target = target;
    }

    /**
     * Returns the selected target.
     *
     * @return the target selected by this {@link TargetSelector}.
     *
     * @throws IllegalStateException if no target has been selected.
     *
     * @see Targetable
     */
    public Targetable getTarget() {
        if (!this.hasTarget())
            throw new IllegalStateException("No target has been selected.");
        return this.target;
    }
}

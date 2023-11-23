package exchangemage.effects.targeting;

import exchangemage.effects.base.Effect;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link TargetSelector} which always selects the same target for its {@link Effect}
 * (e.g. always selecting the player as the target of a healing effect).
 *
 * @see TargetSelector
 * @see Effect
 * @see Targetable
 */
public class ConstantTargetSelector extends TargetSelector {
    /**
     * Functional interface used to return the target of the {@link ConstantTargetSelector}.
     */
    private final TargetGetter targetGetter;

    /**
     * Creates a new {@link ConstantTargetSelector} with the given {@link TargetGetter}.
     *
     * @param targetGetter implementation of the {@link TargetGetter} functional interface used to
     *                     return this {@link TargetSelector}'s target.
     *
     * @see TargetGetter
     * @see TargetSelector
     * @see Targetable
     */
    public ConstantTargetSelector(TargetGetter targetGetter) {
        if (targetGetter == null)
            throw new IllegalArgumentException(
                    "Target getter of constant target selector cannot be null."
            );
        this.targetGetter = targetGetter;
    }

    /**
     * Returns the target of the {@link ConstantTargetSelector} by calling the
     * {@link TargetGetter#getTarget()} method of the {@link TargetGetter} implementation.
     *
     * @return a set containing a single {@link Targetable} object returned by the TargetGetter
     *
     * @see TargetGetter
     * @see Targetable
     */
    @Override
    public Set<Targetable> getActiveTargetables() {
        return new HashSet<>() {{ add(targetGetter.getTarget()); }};
    }

    /**
     * Chooses the target returned by the {@link TargetGetter#getTarget()} method if the provided
     * set of active {@link Targetable}s contains it.
     *
     * @param activeTargetables the set of active targetables to choose from.
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     *
     * @see TargetGetter
     * @see Targetable
     */
    @Override
    public boolean selectTarget(Set<Targetable> activeTargetables) {
        if (activeTargetables.contains(this.targetGetter.getTarget())) {
            this.target = this.targetGetter.getTarget();
            return true;
        }
        return false;
    }

    /**
     * Validates the given {@link Targetable} object by checking if it is the same as the target
     * returned by the {@link TargetGetter#getTarget()} method of this selector's
     * {@link TargetGetter}.
     *
     * @param target the target to validate.
     *
     * @throws InvalidTargetException if the given target does not match the target returned by
     * the TargetGetter.
     *
     * @see TargetGetter
     * @see Targetable
     */
    @Override
    protected void validateTarget(Targetable target) {
        if (target != this.targetGetter.getTarget())
            throw new InvalidTargetException(
                    "Invalid target for constant target selector. Does not match target getter."
            );
    }
}

package exchangemage.effects.targeting.selectors;

import exchangemage.effects.base.Effect;
import exchangemage.effects.targeting.TargetGetter;
import exchangemage.effects.targeting.Targetable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link TargetSelector} which always selects the same target for its {@link Effect}
 * (e.g. always selecting the player as the target of a healing effect).
 *
 * @param <T> the type of the {@link Targetable} objects selected by this selector
 * @see TargetSelector
 * @see Effect
 * @see Targetable
 */
public class ConstantTargetSelector<T extends Targetable> extends TargetSelector<T> {
    /**
     * Functional interface used to return the target of the {@link ConstantTargetSelector}.
     */
    private final TargetGetter<T> targetGetter;

    /**
     * Creates a new {@link ConstantTargetSelector} with given {@link TargetGetter} and target
     * class.
     *
     * @param targetGetter implementation of the {@link TargetGetter} functional interface used to
     *                     return this {@link TargetSelector}'s target
     * @param targetClass  the class of the {@link Targetable} objects selected by this selector
     * @throws NullPointerException if the given target getter is <code>null</code>
     * @see TargetGetter
     * @see TargetSelector
     * @see Targetable
     */
    public ConstantTargetSelector(TargetGetter<T> targetGetter, Class<T> targetClass) {
        super(targetClass);
        Objects.requireNonNull(
                targetGetter,
                "Target getter of constant target selector cannot be null."
        );
        this.targetGetter = targetGetter;
    }

    /**
     * Chooses the target returned by the {@link TargetGetter#getTarget()} method if the provided
     * set of forbidden targets does not contain it.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     * @throws NullPointerException   if the given set of forbidden targets is <code>null</code>
     * @see TargetGetter
     * @see Targetable
     */
    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        Objects.requireNonNull(forbiddenTargets, "Forbidden targets cannot be null.");

        if (forbiddenTargets.contains(this.targetGetter.getTarget()))
            return false;

        this.target = this.targetGetter.getTarget();
        return true;
    }

    /**
     * Validates the given {@link Targetable} object by checking if it is the same as the target
     * returned by the {@link TargetGetter#getTarget()} method of this selector's
     * {@link TargetGetter}.
     *
     * @param target the target to validate.
     * @throws InvalidTargetException if the given target does not match the target returned by
     *                                the TargetGetter.
     * @see TargetGetter
     * @see Targetable
     */
    @Override
    protected void validateTarget(T target) {
        if (target != this.targetGetter.getTarget())
            throw new InvalidTargetException(
                    "Invalid target for constant target selector. Does not match target getter."
            );
    }
}

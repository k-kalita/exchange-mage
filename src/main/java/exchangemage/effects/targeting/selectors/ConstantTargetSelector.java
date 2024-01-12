package exchangemage.effects.targeting.selectors;

import java.util.Objects;
import java.util.Set;

import exchangemage.effects.Effect;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.conditions.getters.SubjectGetter;

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
    private final SubjectGetter<T> targetGetter;

    /**
     * Creates a new {@link ConstantTargetSelector} with given {@link SubjectGetter} and target
     * class.
     *
     * @param targetGetter implementation of the {@link SubjectGetter} functional interface used to
     *                     return this {@link TargetSelector}'s target
     * @param targetClass  the class of the {@link Targetable} objects selected by this selector
     * @throws NullPointerException if the given target getter is <code>null</code>
     * @see SubjectGetter
     * @see TargetSelector
     * @see Targetable
     */
    public ConstantTargetSelector(SubjectGetter<T> targetGetter, Class<T> targetClass) {
        super(targetClass);
        Objects.requireNonNull(
                targetGetter,
                "Target getter of constant target selector cannot be null."
        );
        this.targetGetter = targetGetter;
    }

    /**
     * Chooses the target returned by the {@link SubjectGetter#getSubject()} method if the provided
     * set of forbidden targets does not contain it.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     * @throws NullPointerException   if the given set of forbidden targets is <code>null</code>
     * @see SubjectGetter
     * @see Targetable
     */
    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        Objects.requireNonNull(forbiddenTargets, "Forbidden targets cannot be null.");

        if (forbiddenTargets.contains(this.targetGetter.getSubject()))
            return false;

        this.target = this.targetGetter.getSubject();
        return true;
    }

    /**
     * Validates the given {@link Targetable} object by checking if it is the same as the target
     * returned by the {@link SubjectGetter#getSubject()} ()} method of this selector's
     * {@link SubjectGetter}.
     *
     * @param target the target to validate.
     * @throws InvalidTargetException if the given target does not match the target returned by
     *                                the TargetGetter.
     * @see SubjectGetter
     * @see Targetable
     */
    @Override
    protected void validateTarget(T target) {
        if (target != this.targetGetter.getSubject())
            throw new InvalidTargetException(
                    "Invalid target for constant target selector. Does not match target getter."
            );
    }
}

package exchangemage.effects.targeting;

/**
 * A functional interface used to return a {@link Targetable} object.
 *
 * @param <T> the type of the targetable object returned by the getter.
 * @see Targetable
 * @see ConstantTargetSelector
 */
@FunctionalInterface
public interface TargetGetter<T> {
    /**
     * Returns a {@link Targetable} object. Used by {@link ConstantTargetSelector}s to select
     * their target.
     *
     * @return a {@link Targetable} object.
     * @see Targetable
     * @see ConstantTargetSelector
     */
    T getTarget();
}

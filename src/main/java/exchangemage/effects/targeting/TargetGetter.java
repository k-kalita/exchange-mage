package exchangemage.effects.targeting;

/**
 * A functional interface used to return a {@link Targetable} object.
 *
 * @see Targetable
 * @see ConstantTargetSelector
 */
@FunctionalInterface
public interface TargetGetter {
    /**
     * Returns a {@link Targetable} object. Used by @link ConstantTargetSelector}s to select
     * their target.
     *
     * @return a {@link Targetable} object.
     *
     * @see Targetable
     * @see ConstantTargetSelector
     */
    public Targetable getTarget();
}

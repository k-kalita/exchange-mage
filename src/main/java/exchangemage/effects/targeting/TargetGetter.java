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
     * Returns a {@link Targetable} object. Used to select a target by
     * {@link ConstantTargetSelector}s.
     *
     * @return a {@link Targetable} object.
     */
    public Targetable getTarget();
}

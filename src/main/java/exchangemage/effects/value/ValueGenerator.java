package exchangemage.effects.value;

/**
 * A functional interface used by {@link ValueEffect}s to generate values. The use of this
 * interface allows for creating value effects with non-deterministic values, generated at the
 * time of resolution.
 *
 * @see ValueEffect
 * @see ValueModifier
 */
@FunctionalInterface
public interface ValueGenerator {
    /** @return a value to be used by a {@link ValueEffect} */
    int generate();
}

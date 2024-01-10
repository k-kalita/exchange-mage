package exchangemage.effects.value;

import exchangemage.effects.Effect;

/**
 * A functional interface used by {@link Effect}s which modify the resolution of
 * {@link ValueEffect}s. During effect resolution the value held by a value effect affected by
 * modifier(s) is passed through all of them, in the order they were added to the effect, before
 * being used.
 *
 * @see ValueEffect
 * @see ValueGenerator
 */
@FunctionalInterface
public interface ValueModifier {
    /**
     * Returns a modified value to be used by a {@link ValueEffect}.
     *
     * @param value the value to be modified
     * @return the modified value
     */
    int modify(int value);
}

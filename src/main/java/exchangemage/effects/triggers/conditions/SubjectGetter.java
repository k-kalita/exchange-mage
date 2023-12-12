package exchangemage.effects.triggers.conditions;

import exchangemage.base.GameState;

/**
 * Base interface for all classes which are used to retrieve the subject of a
 * {@link ComparisonCondition} from the current game state to be compared by a
 * {@link SubjectComparator}.
 *
 * @param <T> the type of the subject retrieved by this getter
 * @see ComparisonCondition
 * @see GameState
 */
@FunctionalInterface
public interface SubjectGetter<T> {
    /**
     * Returns the subject of the {@link ComparisonCondition} this getter belongs to.
     *
     * @return the subject of the comparison
     * @see ComparisonCondition
     */
    public T getSubject();
}

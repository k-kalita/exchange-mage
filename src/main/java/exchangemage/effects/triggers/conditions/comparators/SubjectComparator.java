package exchangemage.effects.triggers.conditions.comparators;

import exchangemage.base.GameState;
import exchangemage.effects.triggers.conditions.ComparisonCondition;
import exchangemage.effects.triggers.conditions.getters.SubjectGetter;

/**
 * Base interface for all classes which are used to evaluate {@link ComparisonCondition}s by
 * comparing a subject retrieved from the current {@link GameState} by a {@link SubjectGetter}
 * against a given value/object/type.
 *
 * @param <T> the type of the subject retrieved by the subject getter
 * @see ComparisonCondition
 */
@FunctionalInterface
public interface SubjectComparator<T> {
    /**
     * Evaluates whether the given subject fulfills the condition imposed by this comparator.
     *
     * @param subject the subject to be compared
     * @return <code>true</code> if the condition is fulfilled, <code>false</code> otherwise
     */
    boolean compare(T subject);
}

package exchangemage.effects.triggers.conditions.getters;

import exchangemage.base.GameState;
import exchangemage.effects.triggers.conditions.ComparisonCondition;
import exchangemage.effects.triggers.conditions.comparators.SubjectComparator;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.selectors.ConstantTargetSelector;

/**
 * Base interface for all classes which are used to retrieve the subject of a
 * {@link ComparisonCondition} from the current game state to be compared by a
 * {@link SubjectComparator}.
 * <br><br>
 * Implementations of this interface parametrized to types which extend {@link Targetable} is also
 * used by the {@link ConstantTargetSelector}s to retrieve their target.
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

package exchangemage.effects.triggers.getters;

import exchangemage.base.GameState;
import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.conditions.Condition;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.selectors.ConstantTargetSelector;

/**
 * Base interface for all classes which are used to retrieve the subject of a
 * {@link ConditionalTrigger} from the current game state to be tested against a given
 * {@link Condition}.
 * <br><br>
 * Implementations of this interface parametrized to types which extend {@link Targetable} are also
 * used by the {@link ConstantTargetSelector}s to retrieve their targets.
 *
 * @param <T> the type of the subject retrieved by this getter
 * @see ConditionalTrigger
 * @see GameState
 */
@FunctionalInterface
public interface SubjectGetter<T> {
    /** @return the subject of the {@link ConditionalTrigger} or {@link ConstantTargetSelector} */
    public T getSubject();
}

package exchangemage.effects.triggers.conditions;

import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.getters.SubjectGetter;


/**
 * Base interface for all classes which represent the requirements of a {@link ConditionalTrigger}.
 * Activation of conditional triggers is based on the evaluation of the subject retrieved by a
 * {@link SubjectGetter} against their conditions.
 *
 * @param <T> the type of the subject accepted by this condition
 * @see ConditionalTrigger
 */
@FunctionalInterface
public interface Condition<T> {
    /**
     * Evaluates whether the given subject fulfills the requirements represented by this condition.
     *
     * @param subject the subject to be compared
     * @return <code>true</code> if the condition is fulfilled, <code>false</code> otherwise
     */
    boolean evaluate(T subject);
}

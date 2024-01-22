package exchangemage.effects.triggers.conditions;

import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.getters.SubjectGetter;


/**
 * Base interface for all classes which represent the requirements of a {@link ConditionalTrigger}.
 * Activation of conditional triggers is based on the evaluation of the subject retrieved by a
 * {@link SubjectGetter} against their conditions.
 *
 * @see ConditionalTrigger
 */
@FunctionalInterface
public interface Condition {
    /**
     * An exception thrown when the class of subject provided for evaluation does not match the
     * class of subject expected by the condition.
     */
    class SubjectMismatchException extends IllegalArgumentException {
        public SubjectMismatchException() {
            super("The subject type does not match the type of subject expected by the condition.");
        }
    }

    /**
     * Evaluates whether the given subject fulfills the requirements represented by this condition.
     *
     * @param subject the subject to be evaluated
     * @return <code>true</code> if the condition is fulfilled, <code>false</code> otherwise
     */
    boolean evaluate(Object subject);
}

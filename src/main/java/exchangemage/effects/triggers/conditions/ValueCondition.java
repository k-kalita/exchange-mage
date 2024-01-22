package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A {@link Condition} fulfilled if the subject is equal to the target value.
 *
 * @see ConditionalTrigger
 */
public class ValueCondition implements Condition {
    /** The value the subject is compared to. */
    private final Object targetValue;

    /**
     * @param targetValue the value the subject is compared to.
     * @throws NullPointerException if the target value is null.
     */
    public ValueCondition(Object targetValue) {
        Objects.requireNonNull(targetValue,
                               "Target value of ValueCondition cannot be null.");
        this.targetValue = targetValue;
    }

    /**
     * @param subject the subject to be evaluated
     * @return <code>true</code> if the subject is equal to the target value, <code>false</code>
     * otherwise.
     * @throws SubjectMismatchException if the subject is not of the same type as the target value.
     */
    @Override
    public boolean evaluate(Object subject) {
        if (subject == null)
            return false;
        if (!subject.getClass().equals(targetValue.getClass()))
            throw new SubjectMismatchException();
        return subject.equals(targetValue);
    }
}

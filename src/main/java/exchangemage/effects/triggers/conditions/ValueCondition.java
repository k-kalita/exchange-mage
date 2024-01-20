package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A {@link Condition} fulfilled if the subject is equal to the target value.
 *
 * @param <T> the type of the subject accepted by this condition
 * @see ConditionalTrigger
 */
public class ValueCondition<T> implements Condition<T> {
    /** The value the subject is compared to. */
    private final T targetValue;

    /**
     * @param targetValue the value the subject is compared to.
     * @throws NullPointerException if the target value is null.
     */
    public ValueCondition(T targetValue) {
        Objects.requireNonNull(targetValue,
                               "Target value of ValueCondition cannot be null.");
        this.targetValue = targetValue;
    }

    /**
     * @param subject the subject to be evaluated
     * @return <code>true</code> if the subject is equal to the target value, <code>false</code>
     * otherwise.
     */
    @Override
    public boolean evaluate(T subject) {
        if (subject == null)
            return false;
        return subject.equals(targetValue);
    }
}

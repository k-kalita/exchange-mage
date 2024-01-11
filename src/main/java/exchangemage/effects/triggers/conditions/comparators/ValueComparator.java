package exchangemage.effects.triggers.conditions.comparators;

import java.util.Objects;

import exchangemage.effects.triggers.conditions.ComparisonCondition;

/**
 * A {@link SubjectComparator} used to compare a subject's value to a given value.
 *
 * @param <T> the type of the subject being compared.
 * @see SubjectComparator
 * @see ComparisonCondition
 */
public class ValueComparator<T> implements SubjectComparator<T> {
    /**
     * The value the subject is compared to.
     */
    private final T targetValue;

    /**
     * Creates a new {@link ValueComparator} with the given target value.
     *
     * @param targetValue the value the subject is compared to.
     * @throws NullPointerException if the target value is null.
     */
    public ValueComparator(T targetValue) {
        Objects.requireNonNull(targetValue,
                               "Target value of ValueComparator cannot be null.");
        this.targetValue = targetValue;
    }

    /**
     * Checks if the subject is equal to the target value.
     *
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is equal to the target value, <code>false</code>
     * otherwise.
     */
    @Override
    public boolean compare(T subject) {
        if (subject == null)
            return false;
        return subject.equals(targetValue);
    }
}

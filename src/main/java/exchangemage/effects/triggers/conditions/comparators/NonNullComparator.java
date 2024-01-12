package exchangemage.effects.triggers.conditions.comparators;

import exchangemage.effects.triggers.conditions.ComparisonCondition;

/**
 * A simple {@link SubjectComparator} which returns true if the provided subject is not
 * <code>null</code>.
 *
 * @param <T> the type of subject this comparator accepts
 * @see ComparisonCondition
 */
public class NonNullComparator<T> implements SubjectComparator<T> {
    /**
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is not <code>null</code>, <code>false</code>
     * otherwise
     */
    @Override
    public boolean compare(T subject) {return subject != null;}
}

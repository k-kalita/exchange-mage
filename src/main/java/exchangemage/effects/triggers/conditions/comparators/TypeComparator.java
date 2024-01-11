package exchangemage.effects.triggers.conditions.comparators;

import java.util.Objects;

import exchangemage.effects.triggers.conditions.ComparisonCondition;

/**
 * A {@link SubjectComparator} used to compare a subject's type to a given subclass of that type.
 *
 * @param <T> the type of the subject being compared.
 * @see SubjectComparator
 * @see ComparisonCondition
 */
public class TypeComparator<T> implements SubjectComparator<T> {
    /**
     * The class the subject's type is compared to.
     */
    Class<? extends T> targetType;

    /**
     * Creates a new {@link TypeComparator} which the given target type.
     *
     * @param targetType the class the subject's type is compared to.
     */
    public TypeComparator(Class<? extends T> targetType) {
        Objects.requireNonNull(targetType, "Target type of TypeComparator cannot be null.");
        this.targetType = targetType;
    }

    /**
     * Checks if the subject is an instance of the target type.
     *
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is an instance of the target type,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean compare(T subject) {
        if (subject == null)
            return false;
        return targetType.isInstance(subject);
    }
}

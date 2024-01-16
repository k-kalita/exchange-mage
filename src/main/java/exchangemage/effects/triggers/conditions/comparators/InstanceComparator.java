package exchangemage.effects.triggers.conditions.comparators;

import java.util.Objects;

import exchangemage.effects.triggers.conditions.ComparisonCondition;
import exchangemage.effects.triggers.conditions.getters.SubjectGetter;

/**
 * A {@link SubjectComparator} used to compare a subject to a given instance of that subject's type.
 *
 * @param <T> the type of the subject being compared.
 * @see SubjectComparator
 * @see ComparisonCondition
 */
public class InstanceComparator<T> implements SubjectComparator<T> {
    /** A {@link SubjectGetter} used to get the instance the subject is compared to. */
    private final SubjectGetter<T> targetGetter;

    /**
     * @param targetGetter the getter used to retrieve the instance the subject is compared to.
     * @throws NullPointerException if the target getter is null.
     * @see SubjectGetter
     */
    public InstanceComparator(SubjectGetter<T> targetGetter) {
        Objects.requireNonNull(targetGetter,
                               "Target getter of InstanceComparator cannot be null.");
        this.targetGetter = targetGetter;
    }

    /**
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is the same instance as the target instance,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean compare(T subject) {
        if (subject == null)
            return false;
        return subject == targetGetter.getSubject();
    }
}

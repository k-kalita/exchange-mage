package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A {@link Condition} fulfilled if the subject is an instance of the specified type.
 *
 * @see ConditionalTrigger
 */
public class TypeCondition implements Condition {
    /** The class the subject's type is compared against. */
    Class<?> targetType;

    /**
     * @param targetType the class the subject's type is compared against
     * @throws NullPointerException if the target type is <code>null</code>
     */
    public TypeCondition(Class<?> targetType) {
        Objects.requireNonNull(targetType, "Target type of TypeCondition cannot be null.");
        this.targetType = targetType;
    }

    /**
     * @param subject the subject to be evaluated
     * @return <code>true</code> if the subject is an instance of the target type,
     * <code>false</code> otherwise
     * @throws SubjectMismatchException if the subject's type does not share a common superclass
     *                                  with the target type
     */
    @Override
    public boolean evaluate(Object subject) {
        if (subject == null)
            return false;
        if (!commonSuperclass(subject.getClass(), targetType))
            throw new SubjectMismatchException();
        return targetType.isInstance(subject);
    }

    /**
     * @param a the first class to be compared
     * @param b the second class to be compared
     * @return <code>true</code> if the two classes share a common superclass, <code>false</code>
     * otherwise
     */
    private boolean commonSuperclass(Class<?> a, Class<?> b) {
        if (a == null || b == null)
            return false;
        if (a.isAssignableFrom(b))
            return true;
        if (b.isAssignableFrom(a))
            return true;
        return commonSuperclass(a.getSuperclass(), b.getSuperclass());
    }
}

package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A {@link Condition} fulfilled if the subject is an instance of the specified type.
 *
 * @param <T> the type of the subject this condition accepts
 * @see ConditionalTrigger
 */
public class TypeCondition<T> implements Condition<T> {
    /** The class the subject's type is compared against. */
    Class<? extends T> targetType;

    /**
     * @param targetType the class the subject's type is compared against
     * @throws NullPointerException if the target type is <code>null</code>
     */
    public TypeCondition(Class<? extends T> targetType) {
        Objects.requireNonNull(targetType, "Target type of TypeCondition cannot be null.");
        this.targetType = targetType;
    }

    /**
     * @param subject the subject to be evaluated
     * @return <code>true</code> if the subject is an instance of the target type,
     * <code>false</code> otherwise
     */
    @Override
    public boolean evaluate(T subject) {
        if (subject == null)
            return false;
        return targetType.isInstance(subject);
    }
}

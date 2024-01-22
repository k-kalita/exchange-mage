package exchangemage.effects.triggers.conditions;

import java.util.Objects;

import exchangemage.effects.triggers.ConditionalTrigger;
import exchangemage.effects.triggers.getters.SubjectGetter;

/**
 * A {@link Condition} fulfilled if the provided subject is the same instance as the one retrieved
 * by a specified {@link SubjectGetter}.
 *
 * @see ConditionalTrigger
 */
public class InstanceCondition implements Condition {
    /** A {@link SubjectGetter} used to get the instance the subject is compared to. */
    private final SubjectGetter<?> targetGetter;

    /**
     * @param targetGetter the getter used to retrieve the instance the subject is compared to.
     * @throws NullPointerException if the target getter is null.
     * @see SubjectGetter
     */
    public InstanceCondition(SubjectGetter<?> targetGetter) {
        Objects.requireNonNull(targetGetter,
                               "Target getter of InstanceCondition cannot be null.");
        this.targetGetter = targetGetter;
    }

    /**
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is the same instance as the target instance,
     * <code>false</code> otherwise.
     */
    @Override
    public boolean evaluate(Object subject) {
        if (subject == null)
            return false;
        return subject == targetGetter.getSubject();
    }
}

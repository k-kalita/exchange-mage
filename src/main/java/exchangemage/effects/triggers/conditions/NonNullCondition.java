package exchangemage.effects.triggers.conditions;

import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A simple {@link Condition} fulfilled if the provided subject is not <code>null</code>.
 *
 * @param <T> the type of subject this comparator accepts
 * @see ConditionalTrigger
 */
public class NonNullCondition<T> implements Condition<T> {
    /**
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is not <code>null</code>, <code>false</code>
     * otherwise
     */
    @Override
    public boolean evaluate(T subject) {return subject != null;}
}

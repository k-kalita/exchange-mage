package exchangemage.effects.triggers.conditions;

import exchangemage.effects.triggers.ConditionalTrigger;

/**
 * A simple {@link Condition} fulfilled if the provided subject is not <code>null</code>.
 *
 * @see ConditionalTrigger
 */
public class NonNullCondition implements Condition {
    /**
     * @param subject the subject to be compared
     * @return <code>true</code> if the subject is not <code>null</code>, <code>false</code>
     * otherwise
     */
    @Override
    public boolean evaluate(Object subject) {return subject != null;}
}

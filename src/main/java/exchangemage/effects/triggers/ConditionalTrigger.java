package exchangemage.effects.triggers;

import java.util.Objects;

import exchangemage.effects.Effect;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * A {@link Trigger} which is activated if and only if a given {@link Condition} is fulfilled.
 * <br><br>
 * Used by {@link Effect}s which trigger based on criteria pertaining to the current game state.
 *
 * @see Trigger
 * @see Condition
 */
public class ConditionalTrigger implements Trigger {
    /** The condition to be checked when evaluating activation of this trigger. */
    private final Condition condition;

    /**
     * @param condition the {@link Condition} to be checked when evaluating activation of this
     *                  trigger
     * @throws NullPointerException if the condition is null
     */
    public ConditionalTrigger(Condition condition) {
        Objects.requireNonNull(condition, "Trigger condition cannot be null.");
        this.condition = condition;
    }

    /** @return <code>true</code> if the condition is fulfilled, <code>false</code> otherwise */
    @Override
    public boolean isActivated() {return condition.isFulfilled();}
}

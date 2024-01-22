package exchangemage.effects.triggers;

import exchangemage.effects.value.ValueEffect;
import exchangemage.effects.triggers.getters.EffectValueGetter;
import exchangemage.effects.triggers.conditions.NumericValueCondition;

/**
 * A {@link ConditionalTrigger} activated if the value carried by the {@link ValueEffect} currently
 * in resolution satisfies the specified {@link NumericValueCondition}.
 *
 * @see ValueEffect
 */
public class EffectValueTrigger extends ConditionalTrigger {
    /**
     * @param comparator the {@link NumericValueCondition} against which the value carried by the
     *                   {@link ValueEffect} currently in resolution is evaluated
     * @param state      the {@link ValueEffect.ValueState} in which to retrieve the value from the
     *                   effect
     */
    public EffectValueTrigger(NumericValueCondition comparator,
                              ValueEffect.ValueState state) {
        super(new EffectValueGetter(state), comparator);
    }
}

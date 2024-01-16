package exchangemage.effects.triggers.conditions;

import exchangemage.effects.triggers.conditions.comparators.NumericValueComparator;
import exchangemage.effects.triggers.conditions.getters.EffectValueGetter;
import exchangemage.effects.value.ValueEffect;

/**
 * A {@link ComparisonCondition} fulfilled if the value carried by the {@link ValueEffect} currently
 * in resolution satisfies the specified {@link NumericValueComparator}.
 *
 * @see ValueEffect
 */
public class EffectValueCondition extends ComparisonCondition<Integer> {
    /**
     * @param comparator the {@link NumericValueComparator} used to compare the value carried by the
     *                   {@link ValueEffect} currently in resolution
     * @param state the {@link ValueEffect.ValueState} in which to retrieve the value from the
     *              effect
     */
    public EffectValueCondition(NumericValueComparator<Integer> comparator,
                                ValueEffect.ValueState state) {
        super(new EffectValueGetter(state), comparator);
    }
}

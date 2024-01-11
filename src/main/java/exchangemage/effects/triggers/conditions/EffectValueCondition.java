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
     * Creates a new effect value condition with the specified {@link NumericValueComparator}.
     *
     * @param comparator the comparator used to test the value of the current value effect
     * @param state the {@link ValueEffect.ValueState} in which to retrieve the value from the
     *              effect
     */
    public EffectValueCondition(NumericValueComparator<Integer> comparator,
                                ValueEffect.ValueState state) {
        super(new EffectValueGetter(state), comparator);
    }
}

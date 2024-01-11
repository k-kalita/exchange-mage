package exchangemage.effects.triggers.conditions.getters;

import java.util.Objects;

import exchangemage.effects.value.ValueEffect;

/**
 * A {@link SubjectGetter} which returns the value carried by the {@link ValueEffect} currently in
 * resolution using the specified {@link ValueEffect.ValueState}.
 *
 * @see ValueEffect
 */
public class EffectValueGetter implements SubjectGetter<Integer> {
    /**
     * The state in which to retrieve the value of the {@link ValueEffect} currently in resolution.
     */
    private final ValueEffect.ValueState state;

    /**
     * Creates a new effect value getter with the specified {@link ValueEffect.ValueState}.
     *
     * @param state the state in which to retrieve the value from the effect
     */
    public EffectValueGetter(ValueEffect.ValueState state) {
        Objects.requireNonNull(state, "Value state cannot be null.");
        this.state = state;
    }

    /**
     * Returns the value carried by the {@link ValueEffect} currently in resolution using the
     * specified {@link ValueEffect.ValueState} or <code>null</code> if no such effect is in
     * resolution.
     *
     * @return current value of a value effect or <code>null</code> if none is in resolution
     * @see ValueEffect
     */
    @Override
    public Integer getSubject() {
        var getter = new EffectInResolutionGetter<>(ValueEffect.class);
        ValueEffect<?> effect = getter.getSubject();
        return effect != null ? state.getValue(effect) : null;
    }
}

package exchangemage.effects.triggers.conditions.getters;

import java.util.Objects;

import exchangemage.base.GameState;
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
     * @param state the {@link ValueEffect.ValueState} in which to retrieve the value of the
     *              {@link ValueEffect} currently in resolution.
     */
    public EffectValueGetter(ValueEffect.ValueState state) {
        Objects.requireNonNull(state, "Value state cannot be null.");
        this.state = state;
    }

    /**
     * @return value carried by the {@link ValueEffect} currently in the specified state or
     * <code>null</code> if no such effect is in resolution.
     * @see ValueEffect.ValueState
     */
    @Override
    public Integer getSubject() {
        var effect = GameState.getEffectInResolution();
        return effect instanceof ValueEffect ? state.getValue((ValueEffect<?>) effect) : null;
    }
}

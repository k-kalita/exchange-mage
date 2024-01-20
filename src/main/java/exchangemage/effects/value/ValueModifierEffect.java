package exchangemage.effects.value;

import java.util.Objects;

import exchangemage.effects.Effect;
import exchangemage.effects.targeting.selectors.ConstantTargetSelector;
import exchangemage.effects.triggers.getters.EffectSubclassGetter;
import exchangemage.effects.triggers.getters.EffectInResolutionGetter;

/**
 * An {@link Effect} which adds a {@link ValueModifier} to the {@link ValueEffect} currently in
 * resolution.
 */
@SuppressWarnings("rawtypes")
public class ValueModifierEffect extends Effect<ValueEffect> {
    /** The {@link ValueModifier} to apply to the effect in resolution */
    private final ValueModifier valueModifier;

    /**
     * @param description   the description of this effect
     * @param valueModifier the {@link ValueModifier} which this effect applies to the effect in
     *                      resolution
     * @throws NullPointerException if the value modifier is <code>null</code>
     */
    public ValueModifierEffect(String description, ValueModifier valueModifier) {
        super(description,
              () -> true,
              new ConstantTargetSelector<>(
                      new EffectSubclassGetter<>(ValueEffect.class, new EffectInResolutionGetter()),
                      ValueEffect.class
              ),
              ResolutionMode.IMMEDIATE);
        Objects.requireNonNull(valueModifier, "Value modifier cannot be null");
        this.valueModifier = valueModifier;
    }

    /**
     * @param description the description of this effect
     * @param valueDelta  the delta to apply to the value of the effect in resolution
     */
    public ValueModifierEffect(String description, int valueDelta) {
        this(description, value -> value + valueDelta);
    }

    /** adds the {@link #valueModifier} to the {@link ValueEffect} in resolution */
    @Override
    public void execute() {getTarget().addValueModifier(this.valueModifier);}
}

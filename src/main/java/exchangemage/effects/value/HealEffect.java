package exchangemage.effects.value;

import exchangemage.actors.Actor;
import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.triggers.Trigger;

/**
 * A {@link ValueEffect} which heals its target.
 *
 * @param <T> the type of {@link Actor} chosen by this effect's {@link TargetSelector}
 * @see Actor#heal
 * @see ValueEffect
 */
public class HealEffect<T extends Actor> extends ValueEffect<T> {
    /**
     * Constructs a new heal effect with the specified {@link ValueGenerator}, {@link Trigger},
     * {@link TargetSelector}, and {@link ResolutionMode}.
     *
     * @param valueGenerator the value generator used to generate the value of the effect
     * @param trigger        the trigger of the effect
     * @param targetSelector the target selector of the effect
     * @param resolutionMode the resolution mode of the effect
     * @throws NullPointerException if any of the arguments are <code>null</code>
     */
    public HealEffect(ValueGenerator valueGenerator,
                      Trigger trigger,
                      TargetSelector<T> targetSelector,
                      ResolutionMode resolutionMode) {
        super(valueGenerator, trigger, targetSelector, resolutionMode);
    }

    /**
     * Constructs a new heal effect with a basic {@link ValueGenerator} which always returns the
     * specified value.
     *
     * @param value          the value held by the effect
     * @param trigger        the {@link Trigger} of the effect
     * @param targetSelector the {@link TargetSelector} of the effect
     * @param resolutionMode the {@link ResolutionMode} of the effect
     */
    public HealEffect(int value,
                      Trigger trigger,
                      TargetSelector<T> targetSelector,
                      ResolutionMode resolutionMode) {
        this(() -> value, trigger, targetSelector, resolutionMode);
    }

    /**
     * Constructs a new heal effect with a basic {@link ValueGenerator} which always returns the
     * specified value and a constant {@link Trigger} which always returns <code>true</code>.
     *
     * @param value          the value held by the effect
     * @param targetSelector the {@link TargetSelector} of the effect
     * @param resolutionMode the {@link ResolutionMode} of the effect
     */
    public HealEffect(int value,
                      TargetSelector<T> targetSelector,
                      ResolutionMode resolutionMode) {
        this(value, () -> true, targetSelector, resolutionMode);
    }

    /**
     * Heals the target by an amount equal to the modified value of the effect.
     *
     * @see Actor#heal
     */
    @Override
    public void execute() {getTarget().heal(getModifiedValue());}
}

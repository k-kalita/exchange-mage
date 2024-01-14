package exchangemage.effects.value;

import exchangemage.actors.Actor;
import exchangemage.effects.EffectPlayer;
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
     * @param description    the effect's description
     * @param valueGenerator the effect's {@link ValueGenerator}
     * @param trigger        the effect's {@link Trigger}, used by the {@link EffectPlayer} to
     *                       determine whether it should be resolved
     * @param targetSelector the effect's {@link TargetSelector}, used to choose its target
     * @param resolutionMode the effect's {@link ResolutionMode}, used by the effect player to
     *                       determine how the effect should be resolved
     * @throws NullPointerException if value generator, trigger, target selector, or resolution
     *                              mode is <code>null</code>
     */
    public HealEffect(String description,
                      ValueGenerator valueGenerator,
                      Trigger trigger,
                      TargetSelector<T> targetSelector,
                      ResolutionMode resolutionMode) {
        super(description, valueGenerator, trigger, targetSelector, resolutionMode);
    }

    /**
     * @param description    the effect's description
     * @param value          the value held by the effect
     * @param trigger        the effect's {@link Trigger}, used by the {@link EffectPlayer} to
     *                       determine whether it should be resolved
     * @param targetSelector the effect's {@link TargetSelector}, used to choose its target
     * @param resolutionMode the effect's {@link ResolutionMode}, used by the effect player to
     *                       determine how the effect should be resolved
     */
    public HealEffect(String description,
                      int value,
                      Trigger trigger,
                      TargetSelector<T> targetSelector,
                      ResolutionMode resolutionMode) {
        this(description, () -> value, trigger, targetSelector, resolutionMode);
    }

    /**
     * @param description    the effect's description
     * @param value          the value held by the effect
     * @param targetSelector the effect's {@link TargetSelector}, used to choose its target
     * @param resolutionMode the effect's {@link ResolutionMode}, used by the effect player to
     *                       determine how the effect should be resolved
     */
    public HealEffect(String description,
                      int value,
                      TargetSelector<T> targetSelector,
                      ResolutionMode resolutionMode) {
        this(description, value, () -> true, targetSelector, resolutionMode);
    }

    /**
     * Heals the target by an amount equal to the modified value of the effect.
     *
     * @see Actor#heal
     */
    @Override
    public void execute() {getTarget().heal(getModifiedValue());}
}

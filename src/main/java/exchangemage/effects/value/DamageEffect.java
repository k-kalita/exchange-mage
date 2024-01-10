package exchangemage.effects.value;

import exchangemage.actors.Actor;
import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.triggers.Trigger;

/**
 * A {@link ValueEffect} which deals damage to its target.
 *
 * @param <T> the type of {@link Actor} chosen by this effect's {@link TargetSelector}
 * @see Actor#takeDamage
 * @see ValueEffect
 */
public class DamageEffect<T extends Actor> extends ValueEffect<T> {
    /**
     * Constructs a new damage effect with the specified {@link ValueGenerator}, {@link Trigger},
     * {@link TargetSelector}, and {@link ResolutionMode}.
     *
     * @param valueGenerator the value generator used to generate the value of the effect
     * @param trigger        the trigger of the effect
     * @param targetSelector the target selector of the effect
     * @param resolutionMode the resolution mode of the effect
     * @throws NullPointerException if any of the arguments are <code>null</code>
     */
    public DamageEffect(ValueGenerator valueGenerator,
                        Trigger trigger,
                        TargetSelector<T> targetSelector,
                        ResolutionMode resolutionMode) {
        super(valueGenerator, trigger, targetSelector, resolutionMode);
    }

    /**
     * Deals damage to the target equal to the modified value of the effect.
     *
     * @see Actor#takeDamage
     */
    @Override
    public void execute() {getTarget().takeDamage(getModifiedValue());}
}

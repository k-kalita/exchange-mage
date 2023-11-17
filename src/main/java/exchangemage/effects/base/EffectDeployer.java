package exchangemage.effects.base;

import java.util.Set;

import exchangemage.effects.targeting.TargetSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;

/**
 * An abstract base class for {@link Effect}s used to deploy another effect/set of effects. (e.g.
 * an effect used to add a persistent effect to the scene/actors or assign the same target
 * to an underlying set of effects).
 *
 * @see Effect
 */
public abstract class EffectDeployer extends Effect {
    /**
     * Constructs an {@link EffectDeployer} with the given {@link Trigger}s and
     * {@link TargetSelector}.
     *
     * @param activationTrigger activation trigger of the effect
     * @param evaluationTrigger evaluation trigger of the effect
     * @param targetSelector    target selector of the effect
     */
    public EffectDeployer(Trigger activationTrigger,
                          Trigger evaluationTrigger,
                          TargetSelector targetSelector) {
        super(activationTrigger, evaluationTrigger, targetSelector);
    }

    /**
     * Returns the {@link Effect}(s) this {@link EffectDeployer} is a wrapper for.
     *
     * @return an array of effects
     *
     * @see Effect
     */
    public abstract Effect[] getEffects();

    /**
     * Concrete implementations of the {@link EffectDeployer} base class should override this
     * method to handle the process of choosing a target(s) for the underlying {@link Effect}(s).
     *
     * @param activeTargetables the set of active targetables to choose the target from
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     */
    @Override
    public abstract boolean chooseTarget(Set<Targetable> activeTargetables);

    /**
     * Sets the source of the {@link EffectDeployer} and all the {@link Effect}(s) this
     * EffectDeployer is a wrapper for.
     *
     * @param source the source of the effect
     * @return this effect
     */
    @Override
    public Effect setSource(EffectSource source) {
        super.setSource(source);

        for (Effect effect : this.getEffects())
            effect.setSource(source);

        return this;
    }
}

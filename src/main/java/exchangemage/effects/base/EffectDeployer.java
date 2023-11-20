package exchangemage.effects.base;

import java.util.Set;
import java.util.List;

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
     * Constructs an {@link EffectDeployer} with given {@link Trigger}s, {@link TargetSelector} and
     * {@link ResolutionMode}.
     *
     * @param activationTrigger activation trigger of the effect
     * @param resolutionTrigger resolution trigger of the effect
     * @param targetSelector    target selector of the effect
     *
     * @see Effect
     * @see Trigger
     * @see TargetSelector
     */
    public EffectDeployer(Trigger activationTrigger,
                          Trigger resolutionTrigger,
                          TargetSelector targetSelector,
                          ResolutionMode resolutionMode) {
        super(activationTrigger, resolutionTrigger, targetSelector, resolutionMode);
    }

    /**
     * Returns the {@link Effect}(s) this {@link EffectDeployer} is a wrapper for.
     *
     * @return a list of effects stored by this deployer.
     *
     * @see Effect
     */
    public abstract List<Effect> getEffects();

    /**
     * Concrete implementations of the {@link EffectDeployer} base class should override this
     * method to handle the process of choosing a target(s) for the underlying {@link Effect}(s).
     *
     * @param activeTargetables the set of active targetables to choose the target from
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     *
     * @see Targetable
     * @see Effect
     */
    @Override
    public abstract boolean chooseTarget(Set<Targetable> activeTargetables);

    /**
     * Sets the source of the {@link EffectDeployer} and all the {@link Effect}(s) this deployer
     * is a wrapper for to the given {@link EffectSource} object.
     *
     * @param source the source of the effect
     * @return this effect
     *
     * @see Effect
     * @see EffectSource
     */
    @Override
    public Effect setSource(EffectSource source) {
        super.setSource(source);

        for (Effect effect : this.getEffects())
            effect.setSource(source);

        return this;
    }
}

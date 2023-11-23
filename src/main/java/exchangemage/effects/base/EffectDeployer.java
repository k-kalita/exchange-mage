package exchangemage.effects.base;

import java.util.Set;
import java.util.List;

import exchangemage.effects.targeting.TargetSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;

/**
 * An abstract base class for all {@link Effect}s used to deploy another effect/set of effects.
 * (e.g. an effect used to add a persistent effect to the scene/actors or assign the same target
 * to an underlying set of effects).
 *
 * @see Effect
 */
public abstract class EffectDeployer extends Effect {
    /**
     * Constructs an {@link EffectDeployer} with given {@link Trigger}, {@link TargetSelector} and
     * {@link ResolutionMode}.
     *
     * @param trigger           trigger of the effect
     * @param targetSelector    target selector of the effect
     * @param resolutionMode    resolution mode of the effect
     * @see Trigger
     * @see TargetSelector
     * @see ResolutionMode
     */
    public EffectDeployer(Trigger trigger,
                          TargetSelector targetSelector,
                          ResolutionMode resolutionMode) {
        super(trigger, targetSelector, resolutionMode);
    }

    /**
     * Returns the {@link Effect}(s) this {@link EffectDeployer} is a wrapper for.
     *
     * @return a list of effects stored by this deployer
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
     * @see Targetable
     * @see Effect
     */
    @Override
    public abstract boolean selectTarget(Set<Targetable> activeTargetables);

    /**
     * Concrete implementations of the {@link EffectDeployer} base class should override this
     * method to handle the logic of setting a source for themselves and the underlying
     * {@link Effect}(s) they package.
     *
     * @param source the source of the effect
     * @see Effect
     * @see EffectSource
     */
    @Override
    public abstract void setSource(EffectSource source);
}

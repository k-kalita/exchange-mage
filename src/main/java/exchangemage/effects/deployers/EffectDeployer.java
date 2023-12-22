package exchangemage.effects.deployers;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import exchangemage.effects.Effect;
import exchangemage.effects.EffectSource;
import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;

/**
 * An abstract base class for all {@link Effect}s used to deploy another effect/set of effects.
 * (e.g. an effect used to add a persistent effect to the scene/actors or assign the same target
 * to an underlying set of effects).
 *
 * @param <T> the type of target chosen by the deployer's {@link TargetSelector}
 * @see Effect
 */
public abstract class EffectDeployer<T extends Targetable> extends Effect<T> {
    /**
     * A list of {@link Effect}s stored by this {@link EffectDeployer}.
     */
    protected List<? extends Effect<?>> effects;

    /**
     * Constructs an {@link EffectDeployer} with given {@link Trigger}, {@link TargetSelector},
     * {@link ResolutionMode} and list of {@link Effect}s.
     *
     * @param effects        effects stored by the deployer
     * @param trigger        trigger of the effect
     * @param targetSelector target selector of the effect
     * @param resolutionMode resolution mode of the effect
     * @throws NullPointerException     if the effects list is null
     * @throws IllegalArgumentException if the effects list is empty
     * @see Effect
     * @see Trigger
     * @see TargetSelector
     * @see ResolutionMode
     */
    public EffectDeployer(List<? extends Effect<?>> effects,
                          Trigger trigger,
                          TargetSelector<T> targetSelector,
                          ResolutionMode resolutionMode) {
        super(trigger, targetSelector, resolutionMode);

        Objects.requireNonNull(effects,
                               "Effects list of an EffectDeployer cannot be null.");
        if (effects.isEmpty())
            throw new IllegalArgumentException(
                    "Effects list of an EffectDeployer cannot be empty."
            );

        this.effects = new ArrayList<>(effects);
    }

    /**
     * Returns the {@link Effect}(s) this {@link EffectDeployer} is a wrapper for.
     *
     * @return a list of effects stored by this deployer
     * @see Effect
     */
    public List<? extends Effect<?>> getEffects() {return this.effects;}

    /**
     * Concrete implementations of the {@link EffectDeployer} base class should override this
     * method to handle the process of choosing a target(s) for the deployer and the underlying
     * {@link Effect}(s) (if necessary).
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     * @see Targetable
     * @see Effect
     */
    @Override
    public abstract boolean selectTarget(Set<Targetable> forbiddenTargets);

    /**
     * Concrete implementations of the {@link EffectDeployer} base class should override this
     * method to handle the logic of setting a source for themselves and the underlying
     * {@link Effect}(s) (if necessary).
     *
     * @param source the source of the effect
     * @see Effect
     * @see EffectSource
     */
    @Override
    public abstract void setSource(EffectSource source);
}

package exchangemage.effects.deployers;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import exchangemage.effects.Effect;
import exchangemage.effects.EffectSource;
import exchangemage.effects.EffectPlayer;
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
    /** A list of {@link Effect}s stored by this {@link EffectDeployer}. */
    protected List<? extends Effect<?>> effects;

    /**
     * @param description    description of the deployer
     * @param effects        {@link Effect}s stored by the deployer
     * @param trigger        the deployer's {@link Trigger}, used by the {@link EffectPlayer} to
     *                       determine whether it should be resolved
     * @param targetSelector the deployer's {@link TargetSelector}, used to choose its target
     * @param resolutionMode the deployer's {@link ResolutionMode}, used by the effect player to
     *                       determine how the deployer should be resolved
     * @throws NullPointerException     if the effects list, trigger, target selector or resolution
     *                                  mode is <code>null</code>
     * @throws IllegalArgumentException if the effects list is empty
     */
    public EffectDeployer(String description,
                          List<? extends Effect<?>> effects,
                          Trigger trigger,
                          TargetSelector<T> targetSelector,
                          ResolutionMode resolutionMode) {
        super(description, trigger, targetSelector, resolutionMode);

        Objects.requireNonNull(effects,
                               "Effects list of an EffectDeployer cannot be null.");
        if (effects.isEmpty())
            throw new IllegalArgumentException(
                    "Effects list of an EffectDeployer cannot be empty."
            );

        this.effects = new ArrayList<>(effects);
    }

    /** @return the {@link Effect}(s) this {@link EffectDeployer} is a wrapper for */
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

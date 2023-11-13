package exchangemage.effects.base;

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
     * Calls the {@link TargetSelector#chooseTarget()} method of the {@link TargetSelector} to
     * choose a target for the {@link Effect}. Then sets the same target for all the
     * {@link Effect}(s) this {@link EffectDeployer} is a wrapper for.
     *
     * @see TargetSelector
     * @see Targetable
     */
    @Override
    public void chooseTarget() {
        super.chooseTarget();

        for (Effect effect : this.getEffects())
            effect.setTarget(this.getTarget());
    }

    /**
     * Sets the target of the {@link EffectDeployer} in its {@link TargetSelector}, then calls
     * the {@link Effect#setTarget(Targetable)} method of all the {@link Effect}(s) this
     * EffectDeployer is a wrapper for.
     * <br><br>
     * This method should only be called by effect wrappers and decorators. To choose a target for
     * the effect, use the {@link Effect#chooseTarget()} method.
     *
     * @param target the target to set
     * @return this effect
     *
     * @see Targetable
     * @see TargetSelector
     */
    @Override
    public Effect setTarget(Targetable target) {
        super.setTarget(target);

        for (Effect effect : this.getEffects())
            effect.setTarget(target);

        return this;
    }

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

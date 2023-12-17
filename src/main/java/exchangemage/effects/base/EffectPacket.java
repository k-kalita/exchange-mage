package exchangemage.effects.base;

import exchangemage.base.GameState;
import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;

import java.util.List;
import java.util.Set;

/**
 * An {@link EffectDeployer} implementation used to represent a list of {@link Effect}s which target
 * the same {@link Targetable} (e.g. an effect which deals damage and applies a status effect to
 * the same enemy).
 * <br><br>
 * With the use of {@link ResolutionMode#IMMEDIATE} resolution mode, an {@link EffectPacket} can
 * be used to represent a special type of {@link SequentialEffect} consisting of a series of
 * potentially triggered effects with the same target (e.g. <i>deal 2 damage to the enemy, if its
 * health is above 50%, deal further 2 damage</i>).
 *
 * @param <T> the type of target chosen by the deployer's {@link TargetSelector} and the underlying
 * effects
 * @see Effect
 * @see EffectDeployer
 */
public class EffectPacket<T extends Targetable> extends EffectDeployer<T> {
    /**
     * Constructs an {@link EffectPacket} with given {@link Effect}s, {@link Trigger},
     * {@link TargetSelector} and {@link ResolutionMode}.
     *
     * @param effects        effects to be stored in this effect packet
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
    public EffectPacket(List<Effect<T>> effects,
                        Trigger trigger,
                        TargetSelector<T> targetSelector,
                        ResolutionMode resolutionMode) {
        super(effects, trigger, targetSelector, resolutionMode);
    }

    /**
     * Calls on the {@link EffectPlayer} to evaluate the {@link Effect}s stored in this
     * {@link EffectPacket}.
     *
     * @see EffectPlayer
     */
    @Override
    public void execute() {
        this.effects.forEach(effect -> GameState.getEffectPlayer().evaluateEffect(effect));
    }

    /**
     * Selects a target for this {@link EffectPacket} from the given set of active
     * {@link Targetable}s and assigns it to all the {@link Effect}s stored in this effect deployer.
     *
     * @param activeTargetables the set of active targetables to choose the target from
     * @return true if a target was selected and successfully assigned to all the effects
     * @see TargetSelector
     * @see Targetable
     */
    @Override
    public boolean selectTarget(Set<Targetable> activeTargetables) {
        if (!getTargetSelector().selectTarget(activeTargetables))
            return false;

        this.effects.forEach(
                effect -> effect.getTargetSelector().setTarget(getTargetSelector().getTarget())
        );

        return true;
    }

    /**
     * Sets the given {@link EffectSource} as the source of the {@link EffectPacket} and all the
     * {@link Effect}s stored in it.
     *
     * @param source the source of the effect
     * @see EffectSource
     * @see Effect
     */
    @Override
    public void setSource(EffectSource source) {
        this.source = source;
        this.effects.forEach(effect -> effect.setSource(source));
    }
}

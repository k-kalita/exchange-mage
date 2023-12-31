package exchangemage.effects.deployers;

import java.util.List;
import java.util.Set;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.scenes.Scene;
import exchangemage.effects.targeting.selectors.SceneSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;

/**
 * An {@link EffectDeployer} implementation used to represent a set of {@link Effect}s which resolve
 * immediately one after another so that the next effect can potentially take into account the
 * results of the previous one (e.g. <i>heal 2 points, if your health is below 50%, heal draw a
 * card</i>).
 *
 * @see Effect
 * @see EffectDeployer
 */
public class SequentialEffect extends EffectDeployer<Scene> {
    /**
     * Constructs a {@link SequentialEffect} with given {@link Effect}s, {@link Trigger} and
     * {@link ResolutionMode}.
     *
     * @param effects        effects to be stored in this sequential effect
     * @param trigger        trigger of the effect
     * @param resolutionMode resolution mode of the effect
     * @throws NullPointerException     if the effects list is null
     * @throws IllegalArgumentException if any of the effects stored in the sequential effect
     *                                  have a resolution mode other than
     *                                  {@link ResolutionMode#IMMEDIATE} or if the effects list is
     *                                  empty
     * @see Trigger
     * @see ResolutionMode
     */
    public SequentialEffect(List<Effect<? extends Targetable>> effects,
                            Trigger trigger,
                            ResolutionMode resolutionMode) {
        super(effects, trigger, new SceneSelector(), resolutionMode);

        effects.forEach(effect -> {
            if (effect.getResolutionMode() != ResolutionMode.IMMEDIATE)
                throw new IllegalArgumentException(
                        "Cannot create sequential effect with non-immediate effects."
                );
        });
    }


    /**
     * Calls on the {@link EffectPlayer} to evaluate the {@link Effect}s stored in this
     * {@link SequentialEffect}.
     *
     * @see Effect
     * @see EffectPlayer
     */
    @Override
    public void execute() {
        this.effects.forEach(effect -> GameState.getEffectPlayer().evaluateEffect(effect));
    }

    /**
     * Calls on the {@link SceneSelector} of this {@link SequentialEffect} to choose the current
     * {@link Scene} as the target.
     * <br><br>
     * The {@link Effect}s stored in sequential effects select their targets individually at
     * evaluation.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     * @see Targetable
     * @see Effect
     */
    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        return getTargetSelector().selectTarget(forbiddenTargets);
    }

    /**
     * Sets given {@link EffectSource} as the source of the {@link SequentialEffect} and all the
     * {@link Effect}s stored in it.
     *
     * @param source the source of the effect
     * @see Effect
     * @see EffectSource
     */
    @Override
    public void setSource(EffectSource source) {
        this.source = source;
        this.effects.forEach(effect -> effect.setSource(this));
    }
}

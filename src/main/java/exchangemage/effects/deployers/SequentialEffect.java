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
 * results of the previous one (e.g. <i>heal 2 points, if your health is below 50%, draw a
 * card</i>).
 *
 * @see Effect
 * @see EffectDeployer
 */
public class SequentialEffect extends EffectDeployer<Scene> {
    /**
     * @param effects        {@link Effect}s to be stored in the sequential effect
     * @param trigger        the sequential effect's {@link Trigger}, used by the
     *                       {@link EffectPlayer} to determine whether it should be resolved
     * @param resolutionMode the sequential effect's {@link ResolutionMode}, used by the effect
     *                       player to determine how the effect should be resolved
     * @throws NullPointerException     if the effects list, trigger or resolution mode are
     *                                  <code>null</code>
     * @throws IllegalArgumentException if any of the effects stored in the sequential effect
     *                                  have a resolution mode other than
     *                                  {@link ResolutionMode#IMMEDIATE} or if the effects list is
     *                                  empty
     */
    public SequentialEffect(List<Effect<? extends Targetable>> effects,
                            Trigger trigger,
                            ResolutionMode resolutionMode) {
        super(generateDescription(effects), effects, trigger, new SceneSelector(), resolutionMode);

        effects.forEach(effect -> {
            if (effect.getResolutionMode() != ResolutionMode.IMMEDIATE)
                throw new IllegalArgumentException(
                        "Cannot create sequential effect with non-immediate effects."
                );
        });
    }

    /**
     * @param effects {@link Effect}s to be stored in the sequential effect
     * @return the {@link SequentialEffect}'s description based on the descriptions of the effects
     * stored within it
     */
    private static String generateDescription(List<? extends Effect<?>> effects) {
        StringBuilder description = new StringBuilder();
        description.append("Sequential effect containing:\n");
        effects.forEach(effect -> description.append(effect.getDescription()).append("\n"));
        return description.toString();
    }


    /**
     * Calls on the {@link EffectPlayer} to evaluate the {@link Effect}s stored in this
     * {@link SequentialEffect}.
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
     */
    @Override
    public void setSource(EffectSource source) {
        this.source = source;
        this.effects.forEach(effect -> effect.setSource(source));
    }
}

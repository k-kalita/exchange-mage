package exchangemage.effects.deployers;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.targeting.selectors.SceneSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;
import exchangemage.scenes.Scene;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * An {@link EffectDeployer} used to represent a persistent environmental/actor-specific
 * power/status/modifier, storing within it effects which can be triggered by external
 * occurrences (e.g. effects which activate each time an enemy takes damage or when a specific
 * type of card is played).
 * <br><br>
 * Persistent effects can be triggered during the resolution of other effects. The order in which
 * they are activated is determined by their assigned {@link EffectPlayer.EffectResolutionStage}.
 * Upon execution, a persistent effect will call the {@link EffectPlayer#evaluateEffect} method
 * of the {@link EffectPlayer} on each of its stored effects.
 *
 * @see Effect
 * @see EffectPlayer
 * @see EffectPlayer.EffectResolutionStage
 */
public class PersistentEffect extends EffectDeployer<Scene> {
    /**
     * The activation stage of the {@link PersistentEffect}. Dictates the stage of the
     * {@link Effect} resolution process in which the persistent effect can be triggered.
     */
    private final EffectPlayer.EffectResolutionStage activationStage;

    /**
     * Constructs a {@link PersistentEffect} with given stored {@link Effect}s, activation stage and
     * {@link Trigger}.
     *
     * @param effects         effects stored within the PersistentEffect
     * @param activationStage activation stage of the persistent effect
     * @param trigger         trigger of the persistent effect
     * @throws NullPointerException     if the effects list is null
     * @throws IllegalArgumentException if the effects list is empty
     * @see Effect
     * @see EffectPlayer.EffectResolutionStage
     * @see Trigger
     */
    public PersistentEffect(List<Effect<?>> effects,
                            EffectPlayer.EffectResolutionStage activationStage,
                            Trigger trigger) {
        super(effects,
              trigger,
              new SceneSelector(),
              ResolutionMode.IMMEDIATE);

        this.effects.forEach(effect -> effect.setSource(this));
        this.activationStage = activationStage;
    }

    /**
     * Sets given {@link EffectSource} as the source of the {@link PersistentEffect}. Sets the
     * persistent effect as the source of all the stored {@link Effect}s.
     *
     * @param source the source of the effect
     * @throws NullPointerException if the source is null
     * @see EffectSource
     */
    @Override
    public void setSource(EffectSource source) {
        Objects.requireNonNull(source, "Cannot set null source for persistent effect.");
        this.source = source;
        effects.forEach(effect -> effect.setSource(this));
    }

    /**
     * Returns the activation stage of the {@link PersistentEffect}.
     *
     * @return the activation stage of the persistent effect
     * @see EffectPlayer.EffectResolutionStage
     * @see PersistentEffect
     */
    public EffectPlayer.EffectResolutionStage getActivationStage() {return this.activationStage;}

    /**
     * Calls the {@link EffectPlayer#evaluateEffect} method for all of its stored {@link Effect}s
     * which managed to find a target when the {@link PersistentEffect#selectTarget} method was
     * called.
     *
     * @see EffectPlayer
     * @see PersistentEffect
     */
    @Override
    public void execute() {
        getEffects().stream().filter(Effect::hasTarget)
                .forEach(effect -> GameState.getEffectPlayer().evaluateEffect(effect));
    }

    /**
     * Select the current {@link Scene} as the target for the {@link PersistentEffect} (All
     * persistent effects use the {@link SceneSelector} as their {@link TargetSelector}) and
     * immediately after, select targets for all the {@link Effect}s stored within it.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if at least one of the stored effects successfully found a target,
     * <code>false</code> otherwise
     * @see Targetable
     * @see Effect
     */
    @Override
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        if (!getTargetSelector().selectTarget(forbiddenTargets))
            return false;

        Stream<Boolean> results = getEffects().stream().map(
                effect -> GameState.getTargetingManager().selectTarget(effect)
        );

        return results.anyMatch(Boolean::booleanValue);
    }
}

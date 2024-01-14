package exchangemage.effects.deployers;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import exchangemage.base.GameState;
import exchangemage.effects.Effect;
import exchangemage.effects.EffectPlayer;
import exchangemage.effects.EffectSource;
import exchangemage.effects.targeting.selectors.TargetSelector;
import exchangemage.effects.targeting.selectors.SceneSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.triggers.Trigger;
import exchangemage.scenes.Scene;

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
     * @param description     the persistent effect's description
     * @param effects         {@link Effect}s to be stored within the persistent effect
     * @param activationStage the persistent effect's {@link #activationStage}
     * @param trigger         the persistent effect's {@link Trigger}, used by the
     *                        {@link EffectPlayer} to determine whether it should be resolved
     * @throws NullPointerException     if the effects list, activation stage or trigger are
     *                                  <code>null</code>
     * @throws IllegalArgumentException if the effects list is empty
     * @see EffectPlayer.EffectResolutionStage
     */
    public PersistentEffect(String description,
                            List<Effect<?>> effects,
                            EffectPlayer.EffectResolutionStage activationStage,
                            Trigger trigger) {
        super(description,
              effects,
              trigger,
              new SceneSelector(),
              ResolutionMode.IMMEDIATE);
        Objects.requireNonNull(activationStage, "Activation stage cannot be null.");
        this.activationStage = activationStage;
    }

    /**
     * Sets given {@link PersistentEffectsHolder} as the source of the {@link PersistentEffect} and
     * the effects stored within it.
     *
     * @param effectHolder the persistent effect holder this effect is assigned to
     * @throws NullPointerException     if the source is null
     * @throws IllegalArgumentException if the provided {@link EffectSource} is not a persistent
     *                                  effect holder
     * @see EffectSource
     * @see PersistentEffectsHolder
     */
    @Override
    public void setSource(EffectSource effectHolder) {
        Objects.requireNonNull(effectHolder, "Cannot set null source for effect.");
        if (!(effectHolder instanceof PersistentEffectsHolder))
            throw new IllegalArgumentException("Effect source must be a persistent effect holder.");
        this.source = effectHolder;
        effects.forEach(effect -> effect.setSource(effectHolder));
    }

    /** @return the {@link #activationStage} of the persistent effect */
    public EffectPlayer.EffectResolutionStage getActivationStage() {return this.activationStage;}

    /**
     * Calls the {@link EffectPlayer#evaluateEffect} method for all of its stored {@link Effect}s
     * which managed to find a target when the {@link PersistentEffect#selectTarget} method was
     * called.
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
     * @see TargetSelector
     * @see Targetable
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

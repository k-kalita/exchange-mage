package exchangemage.effects.base;

import exchangemage.effects.targeting.TargetSelector;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.ConstValueTrigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * An {@link Effect} which represents a persistent environmental/actor-specific power or status,
 * storing within it effects which can be activated by external occurrences (e.g. effects which
 * activate each time an enemy takes damage or when a specific type of card is played).
 * <br><br>
 * Persistent effects can be activated during the resolution of other effects. The order in which
 * they are activated is determined by their assigned {@link EffectPlayer.EffectResolutionStage}.
 * Upon execution, a persistent effect will call the {@link EffectPlayer#evaluateEffect} method
 * of the {@link EffectPlayer} to evaluate the resolution of all its stored effects.
 *
 * @see Effect
 * @see EffectPlayer
 * @see EffectPlayer.EffectResolutionStage
 */
public class PersistentEffect extends EffectDeployer {
    /**
     * {@link Effect}s which will be deployed upon the activation of the {@link PersistentEffect}.
     */
    private final List<Effect> effects;
    /**
     * The activation stage of the {@link PersistentEffect}. Dictates the stage of the
     * {@link Effect} resolution process in which the persistent effect can be activated.
     */
    private final EffectPlayer.EffectResolutionStage activationStage;

    /**
     * Constructs a {@link PersistentEffect} with given stored {@link Effect}s, activation stage,
     * {@link Trigger}s, and {@link TargetSelector}.
     *
     * @param effects         effects stored within the PersistentEffect
     * @param activationStage activation stage of the persistent effect
     * @param targetSelector  target selector of the persistent effect
     * @throws NullPointerException     if the effects list is null
     * @throws IllegalArgumentException if the effects list is empty
     * @see Effect
     * @see EffectPlayer.EffectResolutionStage
     * @see Trigger
     * @see TargetSelector
     */
    public PersistentEffect(List<Effect> effects,
                            EffectPlayer.EffectResolutionStage activationStage,
                            Trigger activationTrigger,
                            TargetSelector targetSelector) {
        super(
                activationTrigger,
                new ConstValueTrigger(true),
                targetSelector,
                ResolutionMode.IMMEDIATE
        );

        Objects.requireNonNull(effects,
                               "Cannot create persistent effect with null effects list.");

        if (effects.isEmpty())
            throw new IllegalArgumentException(
                    "Cannot create persistent effect with empty effects list."
            );

        this.effects = new ArrayList<>(effects);
        this.activationStage = activationStage;
    }

    /**
     * Sets given {@link EffectSource} as the source of the {@link PersistentEffect}. And sets the
     * persistent effect as the source of all the stored {@link Effect}s.
     *
     * @param source the source of the effect
     * @throws NullPointerException if the source is null
     *
     * @see EffectSource
     */
    @Override
    public void setSource(EffectSource source) {
        Objects.requireNonNull(source, "Cannot set null source for persistent effect.");
        super.setSource(source);
        effects.forEach(effect -> effect.setSource(this));
    }

    /**
     * Returns the activation stage of the {@link PersistentEffect}.
     *
     * @return the activation stage of the persistent effect
     * @see EffectPlayer.EffectResolutionStage
     * @see PersistentEffect
     */
    public EffectPlayer.EffectResolutionStage getActivationStage() {return activationStage;}

    /**
     * Calls the {@link EffectPlayer#evaluateEffect} method of the {@link EffectPlayer} for each
     * of its stored {@link Effect}s.
     *
     * @see EffectPlayer
     * @see PersistentEffect
     */
    @Override
    public void execute() {
        getEffects().forEach(effect -> EffectPlayer.getEffectPlayer().evaluateEffect(effect));
    }

    /**
     * Returns the {@link Effect}(s) stored within this {@link PersistentEffect}.
     *
     * @return a list of effects stored by this persistent effect
     * @see Effect
     */
    @Override
    public List<Effect> getEffects() {return this.effects;}

    /**
     * Chooses a target for the {@link PersistentEffect} and, immediately after, for all the
     * {@link Effect}s stored within it.
     *
     * @param activeTargetables the set of active targetables to choose the target from
     * @return <code>true</code> if the target choosing process was successful, <code>false</code>
     * otherwise
     * @see Targetable
     * @see Effect
     */
    @Override
    public boolean chooseTarget(Set<Targetable> activeTargetables) {
        boolean result = getTargetSelector().chooseTarget(activeTargetables);
        if (!result) return false;
        getEffects().forEach(
                effect -> EffectPlayer.getTargetingManager().setActiveEffect(effect).chooseTarget()
        );
        return true;
    }
}

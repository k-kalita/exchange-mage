package exchangemage.effects.base;

import exchangemage.base.Game;
import exchangemage.effects.targeting.TargetSelector;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.ConstValueTrigger;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link Effect} which represents a persistent environmental/actor-specific effect, storing
 * within it effects which can be activated by external occurrences (e.g. effects which activate
 * each time an enemy takes damage or when a specific type of card is played).
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
public class PersistentEffect extends Effect {
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
     * @param effects effects stored within the PersistentEffect
     * @param activationStage activation stage of the persistent effect
     * @param targetSelector target selector of the persistent effect
     *
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
        this.effects = new ArrayList<>(effects);
        this.activationStage = activationStage;
    }

    /**
     * Returns the activation stage of the {@link PersistentEffect}.
     *
     * @return the activation stage of the persistent effect
     *
     * @see EffectPlayer.EffectResolutionStage
     * @see PersistentEffect
     */
    public EffectPlayer.EffectResolutionStage getActivationStage() { return activationStage; }

    /**
     * Calls the {@link EffectPlayer#evaluateEffect} method of the {@link EffectPlayer} for each
     * of its stored {@link Effect}s.
     *
     * @see EffectPlayer
     * @see PersistentEffect
     */
    @Override
    public void execute() {
        effects.forEach(effect -> EffectPlayer.getEffectPlayer().evaluateEffect(effect));
    }
}

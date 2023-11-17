package exchangemage.effects.base;

import exchangemage.base.Game;
import exchangemage.effects.targeting.TargetSelector;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.ConstValueTrigger;

/**
 * An {@link Effect} which represents a persistent environmental/actor-specific effect, storing
 * within it effects which can be activated by external occurrences (e.g. effects which activate
 * each time an enemy takes damage or when a specific type of card is played).
 * <br><br>
 * Persistent effects can be activated during the resolution of other effects. The order in which
 * they are activated is determined by their assigned {@link EffectPlayer.EffectResolutionStage}.
 * Upon execution, a persistent effect will enqueue all of its effects into the resolution queue
 * of the {@link EffectPlayer}.
 *
 * @see Effect
 * @see EffectPlayer
 * @see EffectPlayer.EffectResolutionStage
 */
public class PersistentEffect extends Effect {
    private final Effect[] effects;
    private final EffectPlayer.EffectResolutionStage activationStage;

    /**
     * Constructs a {@link PersistentEffect} with given {@link Effect}s, activation stage and
     * {@link TargetSelector}.
     *
     * @param effects effects of the persistent effect
     * @param activationStage activation stage of the persistent effect
     * @param targetSelector target selector of the persistent effect
     */
    public PersistentEffect(Effect[] effects,
                            EffectPlayer.EffectResolutionStage activationStage,
                            Trigger activationTrigger,
                            TargetSelector targetSelector) {
        super(activationTrigger, new ConstValueTrigger(true), targetSelector);
        this.effects = effects;
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
     * Calls the {@link EffectPlayer#enqueueEffect(Effect)} method of the
     * {@link EffectPlayer} for each of its stored {@link Effect}s. This will result in all the
     * effects whose activation {@link Trigger}s are activated being enqueued into the resolution
     * queue.
     *
     * @see EffectPlayer
     * @see PersistentEffect
     */
    @Override
    public void execute() {
        EffectPlayer effectPlayer = Game.getGame().getScene().getEffectPlayer();

        for (Effect effect : effects)
            effectPlayer.enqueueEffect(effect);
    }
}

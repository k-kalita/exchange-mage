package exchangemage.effects.base;

import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.TargetSelector;

/**
 * Base class for all effects in the game. Effects represent all atomic actions that affect scenes,
 * actors and their decks, as well as alerts used to trigger {@link PersistentEffect}s.
 * <br><br>
 * All effects possess a {@link Trigger} component used to determine whether the effect should be
 * activated and a {@link TargetSelector} component used to determine the target of the effect.
 * <br><br>
 * The most important types of effects (which all other effects extend) are:
 * <ul>
 *     <li>{@link ValueEffect} - used to apply changes which can be defined with a single numeric
 *     value (e.g. dealing damage, healing).</li>
 *     <li>{@link PersistentEffect} - a wrapper effect used to represent persistent
 *     environmental/actor-specific effects, storing within them effects which can be activated
 *     by external occurrences (e.g. effects which activate each time an enemy takes damage or
 *     when a specific type of card is played).</li>
 *     <li>{@link AlertEffect} - used to represent an information about a particular event
 *     which does not have any effect on its own but might be used trigger certain persistent
 *     effects (e.g. end/start of combat/an actor's turn).</li>
 *     <li>{@link EffectDeployer} - a wrapper affect used to deploy another effect/set of
 *     effects (e.g. an effect used to add a persistent effect to the scene/actors or assign
 *     the same target to an underlying set of effects).</li>
 * </ul>
 *
 * @see PersistentEffect
 * @see Trigger
 * @see TargetSelector
 */
public abstract class Effect {
    private EffectSource source;
    private Trigger activationTrigger;
    private Trigger evaluationTrigger;
    private TargetSelector targetSelector;

    /**
     * Constructs an {@link Effect} with the given {@link EffectSource}, {@link Trigger}s and
     * {@link TargetSelector}.
     *
     * @param source source of the effect
     * @param activationTrigger activation trigger of the effect
     * @param evaluationTrigger evaluation trigger of the effect
     * @param targetSelector target selector of the effect
     */
    public Effect(EffectSource source,
                  Trigger activationTrigger,
                  Trigger evaluationTrigger,
                  TargetSelector targetSelector) {
        this.source = source;
        this.activationTrigger = activationTrigger;
        this.evaluationTrigger = evaluationTrigger;
        this.targetSelector = targetSelector;
    }

    /**
     * Checks if the activation {@link Trigger} of the {@link Effect} is activated. This method
     * is used by the {@link EffectPlayer} to determine whether an effect should be enqueued into
     * the resolution queue.
     *
     * @return <code>true</code> if the activation trigger of the effect is activated,
     * <code>false</code> otherwise
     *
     * @see Trigger
     * @see Effect
     * @see EffectPlayer
     */
    public boolean isActivated() { return this.activationTrigger.isActivated(); }

    /**
     * Checks if the evaluation {@link Trigger} of the {@link Effect} is activated. This method
     * is used by the {@link EffectPlayer} to determine whether an effect in the resolution queue
     * should be resolved.
     *
     * @return <code>true</code> if the evaluation trigger of the effect is activated,
     * <code>false</code> otherwise
     *
     * @see Trigger
     * @see Effect
     * @see EffectPlayer
     */
    public boolean canEvaluate() { return this.evaluationTrigger.isActivated(); }

    /**
     * Executes the {@link Effect}. This method is called by the {@link EffectPlayer} at the end
     * of the resolution process.
     *
     * @see Effect
     * @see EffectPlayer
     */
    public abstract void execute();

    /**
     * Calls the {@link TargetSelector#chooseTarget()} method of the {@link TargetSelector} to
     * choose a target for the {@link Effect}.
     *
     * @see TargetSelector
     * @see Effect
     */
    public void chooseTarget() { this.targetSelector.chooseTarget(); }

    // --------------------------------- getters and setters ---------------------------------- //

    /**
     * Returns the {@link EffectSource} of the {@link Effect}.
     *
     * @return the source of the effect
     *
     * @see EffectSource
     * @see Effect
     */
    public EffectSource getSource() { return this.source; }

    /**
     * Sets the {@link EffectSource} of the {@link Effect}.
     *
     * @param source the source of the effect
     * @return this effect
     *
     * @see EffectSource
     * @see Effect
     */
    public Effect setSource(EffectSource source) {
        this.source = source;
        return this;
    }

    /**
     * Returns the activation {@link Trigger} of the {@link Effect}.
     *
     * @return the activation trigger of the effect
     *
     * @see Trigger
     * @see Effect
     */
    public Trigger getActivationTrigger() { return this.activationTrigger; }

    /**
     * Sets the activation {@link Trigger} of the {@link Effect}.
     *
     * @param activationTrigger the activation trigger of the effect
     * @return this effect
     *
     * @see Trigger
     * @see Effect
     */
    public Effect setActivationTrigger(Trigger activationTrigger) {
        this.activationTrigger = activationTrigger;
        return this;
    }

    /**
     * Returns the evaluation {@link Trigger} of the {@link Effect}.
     *
     * @return the evaluation trigger of the effect
     *
     * @see Trigger
     * @see Effect
     */
    public Trigger getEvaluationTrigger() { return this.evaluationTrigger; }

    /**
     * Sets the evaluation {@link Trigger} of the {@link Effect}.
     *
     * @param evaluationTrigger the evaluation trigger of the effect
     * @return this effect
     *
     * @see Trigger
     * @see Effect
     */
    public Effect setEvaluationTrigger(Trigger evaluationTrigger) {
        this.evaluationTrigger = evaluationTrigger;
        return this;
    }

    /**
     * Returns the {@link TargetSelector} of the {@link Effect}.
     *
     * @return the target selector of the effect
     *
     * @see TargetSelector
     * @see Effect
     */
    public TargetSelector getTargetSelector() { return this.targetSelector; }

    /**
     * Sets the {@link TargetSelector} of the {@link Effect}.
     *
     * @param targetSelector the target selector of the effect
     * @return this effect
     *
     * @see TargetSelector
     * @see Effect
     */
    public Effect setTargetSelector(TargetSelector targetSelector) {
        this.targetSelector = targetSelector;
        return this;
    }
}

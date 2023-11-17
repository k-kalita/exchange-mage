package exchangemage.effects.base;

import java.util.Set;
import java.util.HashSet;

import exchangemage.base.Observer;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.Targetable;
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
 *     <li>{@link EffectDeployer} - a wrapper effect used to deploy another effect/set of
 *     effects (e.g. an effect used to add a persistent effect to the scene/actors or assign
 *     the same target to an underlying set of effects).</li>
 * </ul>
 *
 * @see PersistentEffect
 * @see Trigger
 * @see TargetSelector
 */
public abstract class Effect implements EffectSource, Targetable {
    /**
     * The {@link EffectSource} of the {@link Effect}. Indicates the origin of the effect (e.g. the
     * card it was played from or the {@link PersistentEffect} that packaged it).
     */
    private EffectSource source;
    /**
     * The activation {@link Trigger} of the {@link Effect}. Indicates the conditions under which
     * this effect can be enqueued into the resolution queue of the {@link EffectPlayer}.
     */
    private Trigger activationTrigger;
    /**
     * The evaluation {@link Trigger} of the {@link Effect}. Indicates the conditions under which
     * this effect can be resolved from the resolution queue of the {@link EffectPlayer}.
     */
    private Trigger evaluationTrigger;
    /**
     * The {@link TargetSelector} of the {@link Effect}. Used to choose the target of the effect.
     */
    private TargetSelector targetSelector;
    /**
     * A set of {@link Observer}s of the {@link Effect}.
     */
    private final Set<Observer> observers;

    /**
     * Constructs an {@link Effect} with the given {@link Trigger}s and {@link TargetSelector}.
     *
     * @param activationTrigger activation trigger of the effect
     * @param evaluationTrigger evaluation trigger of the effect
     * @param targetSelector target selector of the effect
     */
    public Effect(Trigger activationTrigger,
                  Trigger evaluationTrigger,
                  TargetSelector targetSelector) {
        this.source = null;
        this.observers = new HashSet<>();
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
     * Checks if the {@link TargetSelector} of the {@link Effect} has a target by calling its
     * {@link TargetSelector#hasTarget()} method.
     *
     * @return <code>true</code> if the target selector of the effect has a target,
     * <code>false</code> otherwise
     *
     * @see TargetSelector
     */
    public boolean hasTarget() { return this.targetSelector.hasTarget(); }

    /**
     * Calls the {@link TargetSelector#chooseTarget} method of this {@link Effect}'s
     * {@link TargetSelector} to choose a target. Return value indicates whether it was possible to
     * select a valid target.
     *
     * @param activeTargetables the set of active targetables to choose the target from
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise
     *
     * @see TargetSelector
     * @see Targetable
     * @see Effect
     */
    public boolean chooseTarget(Set<Targetable> activeTargetables) {
        return this.targetSelector.chooseTarget(activeTargetables);
    }

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
     * @throws IllegalArgumentException if the given source is <code>null</code>
     *
     * @see EffectSource
     * @see Effect
     */
    public Effect setSource(EffectSource source) {
        if (source == null)
            throw new IllegalArgumentException("Cannot set null effect source.");

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

    /**
     * Returns the target of the {@link Effect} from its {@link TargetSelector}.
     *
     * @return the target of the effect
     *
     * @see Targetable
     * @see TargetSelector
     */
    public Targetable getTarget() { return this.targetSelector.getTarget(); }

    // --------------------------------- observable methods ----------------------------------- //

    @Override
    public void addObserver(Observer observer) {
        validateObserver(observer);
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        validateObserver(observer);
        if (!this.observers.contains(observer))
            throw new IllegalArgumentException("Cannot remove observer that has not been added.");

        this.observers.remove(observer);
    }

    @Override
    public Set<Observer> getObservers() { return this.observers; }
}

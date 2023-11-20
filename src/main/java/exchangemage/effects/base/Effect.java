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
 *     <li>{@link NotificationEffect} - used to represent an information about a particular event
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
     * The activation {@link Trigger} of the {@link Effect}. Represents the conditions under which
     * this effect can be enqueued into the resolution queue of the {@link EffectPlayer}.
     */
    private final Trigger activationTrigger;
    /**
     * The resolution {@link Trigger} of the {@link Effect}. Represents the conditions under which
     * this effect can be resolved from the resolution queue of the {@link EffectPlayer}.
     */
    private final Trigger resolutionTrigger;
    /**
     * The {@link TargetSelector} of the {@link Effect}. Used to choose the target of the effect.
     */
    private final TargetSelector targetSelector;
    /**
     * The {@link ResolutionMode} of the {@link Effect}. Indicates to the {@link EffectPlayer}
     * what should be done with the effect upon its activation.
     */
    private final ResolutionMode resolutionMode;
    /**
     * A set of {@link Observer}s of the {@link Effect}.
     */
    private final Set<Observer> observers;

    /**
     * An enum indicating to the {@link EffectPlayer} what should be done with an {@link Effect}
     * upon its activation.
     *
     * @see Effect
     * @see EffectPlayer
     * @see Trigger
     */
    public enum ResolutionMode {
        /**
         * Indicates that the {@link Effect} should be resolved immediately upon activation, before
         * any other effects in the resolution queue and/or the effect currently being resolved.
         * This resolution mode is used by all {@link PersistentEffect}s and the those effects
         * stored within them which intervene in the resolution process of the effect that
         * activated their wrapper (e.g. effects which modify the damage of an attack).
         *
         * @see Effect
         * @see EffectPlayer
         */
        IMMEDIATE,
        /**
         * Indicates that the {@link Effect} should be enqueued into the resolution queue of the
         * {@link EffectPlayer} and resolved only when all previously enqueued effects have been
         * resolved. This resolution mode is used by all effects which do not take part in modifying
         * or intervening in the resolution process of other effects.
         *
         * @see Effect
         * @see EffectPlayer
         */
        ENQUEUE
    }

    /**
     * Constructs an {@link Effect} with given {@link Trigger}s, {@link TargetSelector} and
     * {@link ResolutionMode}.
     *
     * @param activationTrigger activation trigger of the effect
     * @param resolutionTrigger resolution trigger of the effect
     * @param targetSelector target selector of the effect
     * @param resolutionMode resolution mode of the effect
     */
    public Effect(Trigger activationTrigger,
                  Trigger resolutionTrigger,
                  TargetSelector targetSelector,
                  ResolutionMode resolutionMode) {
        this.source = null;
        this.observers = new HashSet<>();
        this.activationTrigger = activationTrigger;
        this.resolutionTrigger = resolutionTrigger;
        this.targetSelector = targetSelector;
        this.resolutionMode = resolutionMode;
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
     * Checks if the resolution {@link Trigger} of the {@link Effect} is activated. This method
     * is used by the {@link EffectPlayer} to determine whether an effect in the resolution queue
     * should be resolved.
     *
     * @return <code>true</code> if the resolution trigger of the effect is activated,
     * <code>false</code> otherwise
     *
     * @see Trigger
     * @see Effect
     * @see EffectPlayer
     */
    public boolean canResolve() { return this.resolutionTrigger.isActivated(); }

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
     * Returns the resolution {@link Trigger} of the {@link Effect}.
     *
     * @return the resolution trigger of the effect
     *
     * @see Trigger
     * @see Effect
     */
    public Trigger getResolutionTrigger() { return this.resolutionTrigger; }

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
     * Returns the target of the {@link Effect} from its {@link TargetSelector}.
     *
     * @return the target of the effect
     *
     * @see Targetable
     * @see TargetSelector
     */
    public Targetable getTarget() { return this.targetSelector.getTarget(); }

    /**
     * Returns the {@link ResolutionMode} of the {@link Effect}.
     *
     * @return the resolution mode of the effect
     *
     * @see ResolutionMode
     */
    public ResolutionMode getResolutionMode() { return this.resolutionMode; }

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

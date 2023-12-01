package exchangemage.effects.base;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import exchangemage.scenes.Scene;
import exchangemage.actors.Actor;
import exchangemage.cards.Card;
import exchangemage.cards.Deck;
import exchangemage.base.Observer;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.TargetSelector;

/**
 * Base class for all effects in the game. Effects represent all isolated, indivisible actions that
 * affect {@link Scene}s, {@link Actor}s, their {@link Deck}s, etc. and can in combination and/or
 * with use of decorators make up more complex interactions. Crucially, a list of effects is used
 * to represent the rules and impacts of all {@link Card}s in the game.
 * <br><br>
 * All effects possess a {@link Trigger} component used to determine whether the effect should be
 * resolved by the {@link EffectPlayer}, a {@link ResolutionMode} dictating to the EffectPlayer
 * the way in which the effect should be resolved, and a {@link TargetSelector} component used to
 * determine the target of the effect.
 * <br><br>
 * The most important types of effects (which all other effects extend) are:
 * <ul>
 *     <li>{@link ValueEffect} - used to apply changes which can be defined with a single numeric
 *     value (e.g. dealing damage, healing).</li>
 *     <li>{@link EffectDeployer} - a wrapper effect used to deploy another effect/set of
 *     effects (e.g. an effect used to add a status effect to an actor or assign the same target
 *     to an underlying set of effects).</li>
 *     <li>{@link PersistentEffect} - an important type of EffectDeployer used to represent
 *     persistent environmental/actor-specific modifiers/powers/statuses, storing within them
 *     effects which can be triggered by external occurrences (e.g. effects which activate each
 *     time an enemy takes damage or when a specific type of card is played).</li>
 *     <li>{@link NotificationEffect} - used to represent an information about a particular event
 *     which does not have any effect on its own but might be used to trigger certain persistent
 *     effects (e.g. end/start of combat/an actor's turn).</li>
 * </ul>
 *
 * @see EffectPlayer
 * @see Trigger
 * @see TargetSelector
 */
public abstract class Effect implements EffectSource, Targetable {
    /**
     * The {@link EffectSource} of the {@link Effect}. Indicates the origin of the effect (e.g. the
     * card it was played from or the {@link PersistentEffect} that deployed it).
     */
    protected EffectSource source;

    /**
     * The {@link Trigger} of the {@link Effect}. Represents the conditions which must be met for
     * given effect to be resolved by the {@link EffectPlayer}.
     */
    private final Trigger trigger;

    /**
     * The {@link ResolutionMode} of the {@link Effect}. Indicates to the {@link EffectPlayer}
     * what should be done with the effect upon its activation.
     */
    private final ResolutionMode resolutionMode;

    /**
     * The {@link TargetSelector} of the {@link Effect}. Used to choose the target of the effect.
     */
    private final TargetSelector targetSelector;

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
         * <br><br>
         * This resolution mode is used by all {@link PersistentEffect}s and those effects stored
         * within them which intervene in the resolution process of the effect that activated
         * their wrapper (e.g. effects which modify the damage of an attack). It is also employed
         * when certain effect(s) must be resolved before following effects can be evaluated (e.g.
         * constituent effects of {@link SequentialEffect}s).
         *
         * @see Effect
         * @see EffectPlayer
         */
        IMMEDIATE,
        /**
         * Indicates that the {@link Effect} should be enqueued into the resolution queue of the
         * {@link EffectPlayer} and resolved only when all previously enqueued effects have been
         * resolved.
         * <br><br>
         * This resolution mode is used by all effects which do not take part in modifying
         * or intervening in the resolution process of other effects, and do not need to be
         * resolved before following effects can be evaluated.
         *
         * @see Effect
         * @see EffectPlayer
         */
        ENQUEUE
    }

    /**
     * Constructs an {@link Effect} with given {@link Trigger}, {@link TargetSelector} and
     * {@link ResolutionMode}.
     *
     * @param trigger        trigger of the effect
     * @param targetSelector target selector of the effect
     * @param resolutionMode resolution mode of the effect
     * @throws NullPointerException if any of the provided parameters are <code>null</code>
     * @see Trigger
     * @see TargetSelector
     * @see ResolutionMode
     */
    public Effect(Trigger trigger,
                  TargetSelector targetSelector,
                  ResolutionMode resolutionMode) {
        Objects.requireNonNull(trigger, "Cannot create effect with null trigger.");
        Objects.requireNonNull(targetSelector,
                               "Cannot create effect with null target selector.");
        Objects.requireNonNull(resolutionMode,
                               "Cannot create effect with null resolution mode.");

        this.source = null;
        this.observers = new HashSet<>();
        this.trigger = trigger;
        this.targetSelector = targetSelector;
        this.resolutionMode = resolutionMode;
    }

    // ------------------------------- evaluation & resolution -------------------------------- //

    /**
     * Checks if the {@link Trigger} of the {@link Effect} is activated. This method is used by the
     * {@link EffectPlayer} to determine whether an effect should be resolved.
     *
     * @return <code>true</code> if the trigger of the effect is activated, <code>false</code>
     * otherwise
     * @see Trigger
     * @see Effect
     * @see EffectPlayer
     */
    public boolean isTriggered() {return this.trigger.isActivated();}

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
     * @see TargetSelector
     */
    public boolean hasTarget() {return this.targetSelector.hasTarget();}

    /**
     * Calls the {@link TargetSelector#selectTarget} method of this {@link Effect}'s
     * {@link TargetSelector} to choose a target. Return value indicates whether it was possible to
     * select a valid target.
     *
     * @param activeTargetables the set of active targetables to choose the target from
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise
     * @see TargetSelector
     * @see Targetable
     * @see Effect
     */
    public boolean selectTarget(Set<Targetable> activeTargetables) {
        return this.targetSelector.selectTarget(activeTargetables);
    }

    // --------------------------------- getters and setters ---------------------------------- //

    /**
     * Returns the {@link EffectSource} of the {@link Effect}.
     *
     * @return the source of the effect
     * @see EffectSource
     * @see Effect
     */
    public EffectSource getSource() {return this.source;}

    /**
     * Sets the {@link EffectSource} of the {@link Effect}.
     *
     * @param source the source of the effect
     * @throws NullPointerException if the given source is <code>null</code>
     * @see EffectSource
     * @see Effect
     */
    public void setSource(EffectSource source) {
        Objects.requireNonNull(source, "Cannot set null source of effect.");
        this.source = source;
    }

    /**
     * Returns the {@link Trigger} of the {@link Effect}.
     *
     * @return the trigger of the effect
     * @see Trigger
     */
    public Trigger getTrigger() {return this.trigger;}

    /**
     * Returns the {@link ResolutionMode} of the {@link Effect}.
     *
     * @return the resolution mode of the effect
     * @see ResolutionMode
     */
    public ResolutionMode getResolutionMode() {return this.resolutionMode;}

    /**
     * Returns the {@link TargetSelector} of the {@link Effect}.
     *
     * @return the target selector of the effect
     * @see TargetSelector
     */
    public TargetSelector getTargetSelector() {return this.targetSelector;}

    /**
     * Returns the target of the {@link Effect} from its {@link TargetSelector}.
     *
     * @return the target of the effect
     * @see Targetable
     * @see TargetSelector
     */
    public Targetable getTarget() {return this.targetSelector.getTarget();}

    // --------------------------------- observable methods ----------------------------------- //

    @Override
    public void addObserver(Observer observer) {
        Objects.requireNonNull(observer, "Cannot add null observer.");
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        Objects.requireNonNull(observer, "Cannot remove null observer.");
        if (!this.observers.contains(observer))
            throw new IllegalArgumentException("Cannot remove observer that has not been added.");

        this.observers.remove(observer);
    }

    @Override
    public Set<Observer> getObservers() {return this.observers;}
}

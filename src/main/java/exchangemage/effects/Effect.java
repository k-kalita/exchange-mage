package exchangemage.effects;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import exchangemage.effects.deployers.EffectDeployer;
import exchangemage.effects.deployers.PersistentEffect;
import exchangemage.effects.deployers.PersistentEffectsHolder;
import exchangemage.effects.deployers.SequentialEffect;
import exchangemage.effects.value.ValueEffect;
import exchangemage.scenes.Scene;
import exchangemage.actors.Actor;
import exchangemage.cards.Card;
import exchangemage.cards.Deck;
import exchangemage.base.Observer;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.targeting.Targetable;
import exchangemage.effects.targeting.selectors.TargetSelector;

/**
 * Base class for all effects in the game. Effects represent all isolated, indivisible actions that
 * affect {@link Scene}s, {@link Actor}s, their {@link Deck}s, etc. and can in combination and/or
 * with the use of decorators make up more complex interactions. Crucially, a list of effects is
 * used to represent the rules and impacts of all {@link Card}s in the game.
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
 * @param <T> the type of the target chosen by the effect's target selector
 * @see EffectPlayer
 * @see Trigger
 * @see TargetSelector
 */
public abstract class Effect<T extends Targetable> implements Targetable {
    /**
     * An enum indicating to the {@link EffectPlayer} what should be done with an {@link Effect}
     * upon its successful evaluation.
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
        ENQUEUE,
        /**
         * Indicates that the {@link Effect} should be enqueued onto the top of the resolution queue
         * of the {@link EffectPlayer} and resolved immediately after the effect currently being
         * resolved (unless other effects with this or {@link #IMMEDIATE} resolution mode are
         * evaluated in the meantime).
         * <br><br>
         * This resolution mode is used by effects which should be resolved immediately after the
         * effect that activated them (e.g. {@link NotificationEffect}s triggered by an
         * {@link Actor} being damaged).
         *
         * @see Effect
         * @see EffectPlayer
         */
        ENQUEUE_ON_TOP;
    }

    /** Description providing basic information about the {@link Effect}. */
    private final String description;

    /**
     * The {@link EffectSource} of the {@link Effect}. Indicates the origin of the effect (e.g. the
     * card it was played from or the {@link PersistentEffectsHolder} from which it was deployed).
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

    /** The {@link TargetSelector} of the {@link Effect}. Used to choose its target. */
    private final TargetSelector<T> targetSelector;

    /** A set of {@link Observer}s of the {@link Effect}. */
    private final Set<Observer> observers;

    /**
     * @param description    description of the effect
     * @param trigger        the effect's {@link Trigger}, used by the {@link EffectPlayer} to
     *                       determine whether it should be resolved
     * @param targetSelector the effect's {@link TargetSelector}, used to choose its target
     * @param resolutionMode the effect's {@link ResolutionMode}, used by the effect player to
     *                       determine how the effect should be resolved
     * @throws NullPointerException if the trigger, target selector or resolution mode is
     *                              <code>null</code>
     */
    public Effect(String description,
                  Trigger trigger,
                  TargetSelector<T> targetSelector,
                  ResolutionMode resolutionMode) {
        Objects.requireNonNull(trigger, "Cannot create effect with null trigger.");
        Objects.requireNonNull(targetSelector,
                               "Cannot create effect with null target selector.");
        Objects.requireNonNull(resolutionMode,
                               "Cannot create effect with null resolution mode.");

        this.description = description != null ? description : "";
        this.source = null;
        this.observers = new HashSet<>();
        this.trigger = trigger;
        this.targetSelector = targetSelector;
        this.resolutionMode = resolutionMode;
    }

    /** @return the description of the {@link Effect} */
    @Override
    public String toString() {return this.description;}

    // ------------------------------- evaluation & resolution -------------------------------- //

    /**
     * Checks if the {@link Trigger} of the {@link Effect} is activated. This method is used by the
     * {@link EffectPlayer} to determine whether an effect should be resolved.
     *
     * @return <code>true</code> if the trigger of the effect is activated, <code>false</code>
     * otherwise
     */
    public boolean isTriggered() {return this.trigger.isActivated();}

    /**
     * Executes the {@link Effect}. This method is called by the {@link EffectPlayer} at the end
     * of the resolution process.
     */
    public abstract void execute();

    /**
     * @return <code>true</code> if the {@link TargetSelector} of the {@link Effect} has a target,
     * <code>false</code> otherwise
     */
    public boolean hasTarget() {return this.targetSelector.hasTarget();}

    /**
     * Calls the {@link TargetSelector#selectTarget} method of this {@link Effect}'s
     * {@link TargetSelector} to choose a target. Return value indicates whether it was possible to
     * select a valid target.
     *
     * @param forbiddenTargets the set of forbidden targets to exclude from the selection process
     * @return <code>true</code> if a target has been successfully selected, <code>false</code>
     * otherwise
     * @see TargetSelector
     * @see Targetable
     * @see Effect
     */
    public boolean selectTarget(Set<Targetable> forbiddenTargets) {
        return this.targetSelector.selectTarget(forbiddenTargets);
    }

    /**
     * Called by the {@link EffectPlayer} after the {@link Effect} has been resolved and executed
     * to reset it back to its initial, ready-for-evaluation state.
     * <br><br>
     * By default, this method clears the {@link TargetSelector} of the {@link Effect}.
     * Inheriting classes should extend this logic if necessary.
     */
    public void reset() {this.targetSelector.clearTarget();}

    // --------------------------------- getters and setters ---------------------------------- //

    /** @return the description of the {@link Effect} */
    public String getDescription() {return this.description;}

    /** @return the {@link EffectSource} of the {@link Effect} */
    public EffectSource getSource() {return this.source;}

    /**
     * Sets the {@link #source} of the {@link Effect}.
     *
     * @param source the {@link EffectSource} to set
     * @throws NullPointerException if the given source is <code>null</code>
     */
    public void setSource(EffectSource source) {
        Objects.requireNonNull(source, "Cannot set null source of effect.");
        this.source = source;
    }

    /** @return the {@link Trigger} of the {@link Effect} */
    public Trigger getTrigger() {return this.trigger;}

    /** @return the {@link ResolutionMode} of the {@link Effect} */
    public ResolutionMode getResolutionMode() {return this.resolutionMode;}

    /** @return the {@link TargetSelector} of the {@link Effect} */
    public TargetSelector<T> getTargetSelector() {return this.targetSelector;}

    /** @return the target of the {@link Effect} from its {@link TargetSelector} */
    public T getTarget() {return this.targetSelector.getTarget();}

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

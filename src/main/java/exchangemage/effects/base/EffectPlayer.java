package exchangemage.effects.base;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import exchangemage.base.GameState;
import exchangemage.effects.targeting.TargetingManager;
import exchangemage.scenes.Scene;
import exchangemage.cards.Card;
import exchangemage.effects.triggers.Trigger;
import exchangemage.effects.triggers.conditions.Condition;

/**
 * EffectPlayer is responsible for managing the process of playing {@link Card}s and resolving
 * their {@link Effect}s.
 * <br><br>
 * The crucial functionality of this class revolves around evaluating the activation of effects,
 * choosing their targets with the help of the {@link TargetingManager} and resolving them in
 * accordance with their {@link Effect.ResolutionMode}.
 * <br><br>
 * Three most important concepts necessary for understanding the functionality of the effect player
 * are:
 * <ul>
 *     <li>
 *         <b>Effect evaluation</b> - the process of determining whether an effect is activated
 *         and whether it can select a valid target. This process is handled by the
 *         {@link #evaluateEffect} method.
 *         <br><br>
 *         If an effect is activated and has found a valid target, it is resolved in accordance
 *         with its {@link Effect.ResolutionMode}.
 *     </li>
 *     <br>
 *     <li>
 *         <b>Effect resolution</b> - if an effect passes the evaluation stage, it enters the
 *         resolution process. This process is handled by the {@link #resolveEffect} method.
 *         <br><br>
 *         During the resolution process, the activation of {@link PersistentEffect}s (which may
 *         potentially interrupt or modify the execution of the effect being resolved) is
 *         evaluated in accordance with their {@link EffectResolutionStage}.<br>
 *         The resolution process ends with the execution of the effect.
 *     </li>
 *     <br>
 *     <li>
 *         <b>Card resolution</b> - a {@link Card} is considered in resolution when its effects are
 *         currently being evaluated and/or resolved (or during the evaluation/resolution of
 *         effects which interrupt or modify the execution of the card's effects).
 *     </li>
 * </ul>
 *
 * @see Effect
 * @see PersistentEffect
 * @see EffectResolutionStage
 * @see TargetingManager
 */
public class EffectPlayer {
    /**
     * The {@link Card} whose {@link Effect}s are currently being evaluated and/or resolved (or
     * <code>null</code> if no card is being resolved).
     */
    private Card cardInResolution = null;

    /**
     * The {@link Effect} currently being resolved (or <code>null</code> if no effect is being
     * resolved).
     */
    private Effect<?> effectInResolution = null;

    /**
     * The {@link Effect} currently being evaluated (or <code>null</code> if no effect is being
     * resolved).
     */
    private Effect<?> effectInEvaluation = null;

    /**
     * The {@link TargetingManager} used to manage the process of choosing a target for
     * {@link Effect}s enqueued into the resolution queue.
     */
    private final TargetingManager targetingManager = new TargetingManager();

    /**
     * The queue of {@link Effect}s to be resolved.
     */
    private final Queue<Effect<?>> resolutionQueue = new LinkedList<>();


    /**
     * EffectResolutionStage is an enum representing the stages of the resolution process of an
     * {@link Effect} and is used to define the order in which the activation of
     * {@link PersistentEffect}s is resolved.
     * <br><br>
     * The stages are:
     * <ul>
     *     <li>{@link #ACTIVATION} - if the conditions of a persistent effect's {@link Trigger}
     *     depend on the initial, unmodified version of the effect being resolved and do not
     *     modify the effect itself (e.g. <i>Whenever an effect with a damage of 1 is played,
     *     give the target bleed 3</i>) the persistent effect's activation should be resolved in
     *     this stage.</li>
     *     <li>{@link #MODIFICATION} - if the conditions of a persistent effect's trigger depend
     *     on the initial, unmodified version of the effect being resolved and modify the effect
     *     itself (e.g. <i>Whenever an effect with a damage of 1 is played, increase its damage
     *     by 1</i>) the persistent effect's activation should be resolved in this stage.</li>
     *     <li>{@link #RESOLUTION} - if the conditions of a persistent effect's trigger depend on
     *     the modified version of the effect being resolved and/or their activation may act as a
     *     final intervention in the resolution process (e.g. <i>Whenever an effect would deal 2
     *     or more damage to this entity, reduce the damage to 1</i>) the persistent effect's
     *     activation should be resolved in this stage.</li>
     *     <li>{@link #RESPONSE} - if the conditions of a persistent effect's trigger depend on
     *     the final version of the effect being resolved and do not modify the effect itself
     *     (e.g. <i>Whenever you deal 3 or more damage to an entity, heal 1</i>) the persistent
     *     effect's activation should be resolved in this stage.</li>
     * </ul>
     *
     * @see Effect
     * @see PersistentEffect
     * @see Trigger
     * @see EffectPlayer
     */
    public enum EffectResolutionStage {
        /**
         * The initial stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s which do not modify the {@link Effect} being resolved and
         * whose {@link Trigger} {@link Condition}s depend on its initial, unmodified version.
         *
         * @see Effect
         * @see PersistentEffect
         */
        ACTIVATION,
        /**
         * The second stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s which modify the {@link Effect} being resolved and whose
         * {@link Trigger} {@link Condition}s depend on its initial, unmodified version.
         *
         * @see Effect
         * @see PersistentEffect
         */
        MODIFICATION,
        /**
         * The third stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s whose {@link Trigger} {@link Condition}s depend on the
         * final version of the {@link Effect} being resolved and/or whose activation may act as
         * a final intervention in the resolution process.
         *
         * @see Effect
         * @see PersistentEffect
         */
        RESOLUTION,
        /**
         * The final stage of the resolution process. Used for resolving the activation of
         * {@link PersistentEffect}s which do not modify the {@link Effect} being resolved and
         * whose {@link Trigger} {@link Condition}s depend on its final version, acting as a
         * response to the resolution of the effect.
         *
         * @see Effect
         * @see PersistentEffect
         */
        RESPONSE;

        /**
         * Sorts a list of {@link PersistentEffect}s by their trigger stage in ascending order (as
         * defined by the {@link EffectResolutionStage} enum).
         *
         * @param effects the list of persistent effects to sort
         * @return the sorted list of persistent effects
         * @see PersistentEffect
         */
        public static List<PersistentEffect> sortPersistentEffects(List<PersistentEffect> effects) {
            effects.sort(Comparator.comparing(PersistentEffect::getActivationStage));
            return effects;
        }
    }

    /**
     * Checks whether there is an {@link Effect} currently being resolved by the
     * {@link EffectPlayer}.
     *
     * @return <code>true</code> if there is an effect currently being resolved, <code>false</code>
     * otherwise
     * @see Effect
     */
    public boolean effectInResolution() {return this.effectInResolution != null;}

    /**
     * Checks whether there is an {@link Effect} currently being evaluated by the
     * {@link EffectPlayer}.
     *
     * @return <code>true</code> if there is an effect currently being evaluated, <code>false</code>
     * otherwise
     * @see Effect
     */
    public boolean effectInEvaluation() {return this.effectInEvaluation != null;}

    // -------------------------------- effect resolution ------------------------------------- //

    /**
     * Evaluates whether given {@link Effect} is triggered and if it is able to select a valid
     * target. If so, handles the resolution in accordance with the effect's
     * {@link Effect.ResolutionMode}.
     *
     * @param effect the effect to evaluate
     * @throws NullPointerException  if the given effect is null
     * @throws IllegalStateException if the resolution mode of the effect is not recognized
     * @see Effect
     * @see Trigger
     * @see TargetingManager
     * @see Effect.ResolutionMode
     */
    public void evaluateEffect(Effect<?> effect) {
        Objects.requireNonNull(effect, "Effect to evaluate cannot be null.");

        if (effectInEvaluation())
            throw new IllegalStateException("There is already an effect in evaluation.");

        this.effectInEvaluation = effect;
        if (!effect.isTriggered())
            return;
        if (!this.targetingManager.selectTarget(effect))
            return;
        this.effectInEvaluation = null;

        switch (effect.getResolutionMode()) {
            case ENQUEUE -> enqueueEffect(effect);
            case IMMEDIATE -> resolveEffectImmediately(effect);
            default -> throw new IllegalStateException(
                    "Effect resolution mode not recognized: " + effect.getResolutionMode()
            );
        }
    }

    /**
     * Enqueues given {@link Effect} into the resolution queue.
     *
     * @param effect the effect to enqueue
     * @throws NullPointerException  if the effect is null
     * @throws IllegalStateException if the effect has no target
     * @see Effect
     * @see TargetingManager
     */
    private void enqueueEffect(Effect<?> effect) {
        Objects.requireNonNull(effect, "Effect to enqueue cannot be null.");

        if (!effect.hasTarget())
            throw new IllegalStateException("Cannot enqueue effect with no target.");

        this.resolutionQueue.add(effect);
    }

    /**
     * Resolves given {@link Effect} and evaluates the activation of {@link PersistentEffect}s
     * present in the {@link Scene} in accordance with their activation stage.
     *
     * @param effect the effect to resolve
     * @throws NullPointerException  if the given effect is null
     * @throws IllegalStateException if there is already an effect being resolved or if the given
     *                               effect has no target
     * @see Effect
     * @see PersistentEffect
     * @see EffectResolutionStage
     */
    private void resolveEffect(Effect<?> effect) {
        Objects.requireNonNull(effect, "Effect to resolve cannot be null.");

        if (effectInResolution())
            throw new IllegalStateException("There is already an effect being resolved.");
        if (!effect.hasTarget())
            throw new IllegalStateException("Effect to resolve has no target.");

        this.effectInResolution = effect;
        EffectResolutionStage
                .sortPersistentEffects(new ArrayList<>(GameState.getScene().getPersistentEffects()))
                .forEach(this::evaluateEffect);
        effect.execute();
        this.effectInResolution = null;
    }

    /**
     * Resolves given {@link Effect} immediately, regardless of whether there is another effect
     * being resolved or not. If a resolution of another effect is interrupted by this method, it
     * is resumed after the resolution process of this effect is finished.
     * <br><br>
     * This method is used to resolve effects with the {@link Effect.ResolutionMode#IMMEDIATE}
     * resolution mode.
     *
     * @param effect the effect to resolve
     * @throws NullPointerException if the given effect is null
     * @see Effect
     * @see Effect.ResolutionMode
     */
    private void resolveEffectImmediately(Effect<?> effect) {
        Objects.requireNonNull(effect, "Effect to resolve immediately cannot be null.");
        Effect<?> currentEffect = this.effectInResolution;
        this.effectInResolution = null;
        resolveEffect(effect);
        this.effectInResolution = currentEffect;
    }

    /**
     * Resolves all currently enqueued {@link Effect}s (along with any persistent effects activated
     * in the process).
     *
     * @see Effect
     * @see PersistentEffect
     */
    public void resolveQueue() {
        while (!this.resolutionQueue.isEmpty())
            resolveEffect(this.resolutionQueue.poll());
    }

    /**
     * Calls the {@link #evaluateEffect} method on all {@link Effect}s of the given {@link Card}
     * to determine their targets and activation. Then resolves all enqueued effects.
     *
     * @param card the card to play
     * @throws NullPointerException if the given card is null
     * @see Effect
     * @see Card
     */
    public void playCard(Card card) {
        Objects.requireNonNull(card, "Card to play cannot be null.");
        this.cardInResolution = card;
        card.getEffects().forEach(this::evaluateEffect);
        this.resolveQueue();
        this.cardInResolution = null;
    }

    // ------------------------------------ getters ------------------------------------------- //

    /**
     * Returns the {@link TargetingManager} used by the {@link EffectPlayer} of the current
     * {@link Scene} in the game.
     *
     * @return the targeting manager of the current effect player
     * @see TargetingManager
     */
    public TargetingManager getTargetingManager() {return this.targetingManager;}

    /**
     * Returns the {@link Card} whose {@link Effect}s are currently being resolved (or
     * <code>null</code> if no card is being resolved).
     *
     * @return the current card being resolved
     * @see Card
     */
    public Card getCardInResolution() {return this.cardInResolution;}

    /**
     * Returns the current {@link Effect} being resolved.
     *
     * @return the current effect being resolved or <code>null</code> if no effect is being resolved
     * @see Effect
     */
    public Effect<?> getEffectInResolution() {return this.effectInResolution;}

    /**
     * Returns the current {@link Effect} being evaluated.
     *
     * @return the current effect being evaluated or <code>null</code> if no effects is being
     * evaluated
     * @see Effect
     */
    public Effect<?> getEffectInEvaluation() {return this.effectInEvaluation;}
}
